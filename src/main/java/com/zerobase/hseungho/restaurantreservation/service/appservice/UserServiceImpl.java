package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.NotFoundException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.util.ValidUtils;
import com.zerobase.hseungho.restaurantreservation.service.domain.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.Login;
import com.zerobase.hseungho.restaurantreservation.service.dto.SignUp;
import com.zerobase.hseungho.restaurantreservation.service.dto.TokenDto;
import com.zerobase.hseungho.restaurantreservation.service.dto.UserDto;
import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean checkUserIdAvailable(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    @Override
    public boolean checkNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Override
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
    public TokenDto login(Login.Request request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new NotFoundException(ErrorCodeType.NOT_FOUND_USER));

        validateLoginRequest(request, user);


        return null;
    }

    private void validateLoginRequest(Login.Request request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // miss match password exception
        }
        if (!user.isResigned()) {
            // already resign id exception
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
