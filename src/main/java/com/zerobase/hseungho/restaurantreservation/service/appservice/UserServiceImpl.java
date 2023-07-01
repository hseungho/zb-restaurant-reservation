package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.NotFoundException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.UnauthorizedException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.security.SecurityHolder;
import com.zerobase.hseungho.restaurantreservation.global.security.jwt.JwtComponent;
import com.zerobase.hseungho.restaurantreservation.global.util.ValidUtils;
import com.zerobase.hseungho.restaurantreservation.service.domain.user.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.Login;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.SignUp;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.TokenDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.user.UserDto;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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

        User newUser = User.createDefaultEntity(
                request.getUserId(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname()
        );

        return UserDto.fromEntity(userRepository.save(newUser));
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
        String id = SecurityHolder.getIdOfUser();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));

        validateRegisterPartnerRequest(user);

        user.setType(UserType.ROLE_PARTNER);

        return UserDto.fromEntity(user);
    }

    @Override
    @Transactional
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
        if (!isAvailablePassword(request.getUserId(), request.getPassword())) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST_SIGN_UP_PASSWORD);
        }
    }

    private boolean isAvailablePassword(String userId, String password) {
        String regex = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z]).{8,100}$";
        Matcher matcher = Pattern.compile(regex).matcher(password);
        return matcher.matches() && !password.contains(userId) && !password.contains(" ");
    }

}
