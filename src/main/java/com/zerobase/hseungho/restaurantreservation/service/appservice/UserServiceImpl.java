package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.InternalServerErrorException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.NotFoundException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.UnauthorizedException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.security.SecurityHolder;
import com.zerobase.hseungho.restaurantreservation.global.security.jwt.JwtComponent;
import com.zerobase.hseungho.restaurantreservation.global.util.Generator;
import com.zerobase.hseungho.restaurantreservation.global.util.ValidUtils;
import com.zerobase.hseungho.restaurantreservation.service.domain.restaurant.Restaurant;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.*;
import com.zerobase.hseungho.restaurantreservation.service.repository.RestaurantRepository;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtComponent jwtComponent;

    @Override
    @Transactional(readOnly = true)
    public boolean checkUserIdAvailable(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Override
    @Transactional
    public UserDto signUp(SignUp.Request request) {
        validateSignUpRequest(request);

        return UserDto.fromEntity(
                userRepository.save(
                        User.create(
                                request.getUserId(),
                                passwordEncoder.encode(request.getPassword()),
                                request.getNickname()
                        )
                )
        );
    }

    @Override
    @Transactional
    public TokenDto login(Login.Request request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new UnauthorizedException(ErrorCodeType.NOT_FOUND_USER));

        validateLoginRequest(request, user);

        user.login();

        return TokenDto.of(
                jwtComponent.generateAccessToken(user.getId(), user.getType()),
                jwtComponent.generateRefreshToken(user.getId(), user.getType()),
                user.getLoggedInAt()
        );
    }

    @Override
    @Transactional
    public UserDto registerPartner() {
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));

        validateRegisterPartnerRequest(user);

        user.setType(UserType.ROLE_PARTNER);

        return UserDto.fromEntity(user);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenDto refreshToken(String refreshToken) {
        try {
            String token = validateRefreshTokenRequest(refreshToken);

            User user = ((User) jwtComponent.getAuthentication(token).getPrincipal());

            return TokenDto.of(
                    jwtComponent.generateAccessToken(user.getId(), user.getType()),
                    jwtComponent.generateRefreshToken(user.getId(), user.getType()),
                    user.getLoggedInAt()
            );
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCodeType.UNAUTHORIZED_REFRESH_TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new UnauthorizedException(ErrorCodeType.UNAUTHORIZED_REFRESH_TOKEN_INVALID);
        }
    }

    @Override
    @Transactional
    public UserDto updatePassword(UpdatePassword.Request request) {
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));

        validateUpdatePasswordRequest(request, user);

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));

        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto findProfile() {
        return UserDto.fromEntity(SecurityHolder.getUser());
    }

    @Override
    @Transactional
    public UserDto updateProfile(UpdateProfile.Request request) {
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));

        validateUpdateProfileRequest(user, request);

        user.update(request.getNickname());

        return UserDto.fromEntity(user);
    }

    @Override
    @Transactional
    public UserDto resign() {
        User user = userRepository.findById(SecurityHolder.getIdOfUser())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));

        validateResignRequest(user);

        user.resign(generateDelUserNickname());

        return UserDto.fromEntity(user);
    }

    private String generateDelUserNickname() {
        AtomicInteger i = new AtomicInteger(10);
        while (i.getAndDecrement() > 0) {
            String userNickname = Generator.generateDelUserNickname();
            if(this.checkNicknameAvailable(userNickname)) {
                return userNickname;
            }
        }
        throw new InternalServerErrorException(ErrorCodeType.INTERNAL_SERVER_ERROR_GENERATE_DEL_USER_NICKNAME);

    }
    private void validateResignRequest(User user) {
        if (user.isResigned()) {
            // 이미 탈퇴한 유저입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESIGN_ALREADY);
        }
        if (user.isPartner()) {
            Restaurant restaurant = restaurantRepository.findByManager(user).orElse(null);
            if (restaurant == null) return;
            if (!restaurant.isDeleted()) {
                // 아직 매장을 갖고 있는 파트너 유저는 탈퇴할 수 없습니다.
                throw new BadRequestException(ErrorCodeType.BAD_REQUEST_RESIGN_HAVING_RESTAURANT);
            }
        }
    }

    private void validateUpdateProfileRequest(User user, UpdateProfile.Request request) {
        if (!ValidUtils.hasTexts(request.getNickname())) {
            // 수정할 정보를 모두 입력해주세요.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_UPDATE_PROFILE_BLANK);
        }
        if (Objects.equals(request.getNickname(), user.getNickname())) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_UPDATE_PROFILE_SAME_NICKNAME);
        }
        if (!checkNicknameAvailable(request.getNickname())) {
            // 사용할 수 없는 닉네임입니다.
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_UPDATE_PROFILE_NICKNAME_DUPLICATED);
        }
        if (!(request.getNickname().length() < 15)) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_UPDATE_PROFILE_NICKNAME_LENGTH);
        }
    }

    private void validateUpdatePasswordRequest(UpdatePassword.Request request, User user) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            // 현재 비밀번호가 일치하지 않습니다.
            throw new UnauthorizedException(ErrorCodeType.UNAUTHORIZED_UPDATE_PASSWORD_INVALID_CUR_PASSWORD);
        }
        if (isUnavailablePassword(user.getUserId(), request.getNewPassword())) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_UPDATE_PASSWORD_INVALID_PASSWORD);
        }
    }

    private String validateRefreshTokenRequest(String refreshToken) {
        if (!ValidUtils.hasTexts(refreshToken)) {
            throw new UnauthorizedException(ErrorCodeType.UNAUTHORIZED_REFRESH_TOKEN_BLANK);
        }
        String token = jwtComponent.separatePrefix(refreshToken);
        if (!ValidUtils.hasTexts(token)) {
            throw new UnauthorizedException(ErrorCodeType.UNAUTHORIZED_REFRESH_TOKEN_INVALID);
        }
        return token;
    }

    private void validateRegisterPartnerRequest(User user) {
        if (user.isPartner()) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_PARTNER_ALREADY);
        }
    }

    private void validateLoginRequest(Login.Request request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(ErrorCodeType.UNAUTHORIZED_LOGIN_REQUESTED_VALUE);
        }
        if (user.isResigned()) {
            throw new UnauthorizedException(ErrorCodeType.UNAUTHORIZED_LOGIN_ALREADY_RESIGNED_USER);
        }
    }

    private void validateSignUpRequest(SignUp.Request request) {
        if (!ValidUtils.hasTexts(request.getUserId(), request.getPassword(), request.getNickname())) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SIGN_UP_BLANK);
        }
        if (!checkUserIdAvailable(request.getUserId())) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SIGN_UP_USER_ID_DUPLICATED);
        }
        if (!checkNicknameAvailable(request.getNickname())) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SIGN_UP_NICKNAME_DUPLICATED);
        }
        if (!(request.getNickname().length() < 15)) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SIGN_UP_NICKNAME_LENGTH);
        }
        if (isUnavailablePassword(request.getUserId(), request.getPassword())) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SIGN_UP_PASSWORD);
        }
    }

    private boolean isUnavailablePassword(String userId, String password) {
        String regex = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z]).{8,100}$";
        Matcher matcher = Pattern.compile(regex).matcher(password);
        return !matcher.matches() || password.contains(userId) || password.contains(" ");
    }

}
