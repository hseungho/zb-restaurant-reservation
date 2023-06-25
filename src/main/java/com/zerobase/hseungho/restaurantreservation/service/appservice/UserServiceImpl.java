package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.domain.User;
import com.zerobase.hseungho.restaurantreservation.service.dto.external.user.SignUp;
import com.zerobase.hseungho.restaurantreservation.service.dto.internal.user.UserDto;
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

    private final PasswordEncoder encoder;

    @Override
    public boolean checkUserIdAvailable(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    @Override
    public boolean checkNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname) && nickname.length() < 15;
    }

    @Override
    public UserDto signUp(SignUp.Request request) {
        validateSignUpRequest(request);

        User newUser = User.createDefaultEntity(
                request.getUserId(),
                encoder.encode(request.getPassword()),
                request.getNickname()
        );

        return UserDto.fromEntity(newUser);
    }

    private void validateSignUpRequest(SignUp.Request request) {
        if (!checkNicknameAvailable(request.getUserId())) {
            // Cannot use this user id exception
        }
        if (!checkNicknameAvailable(request.getNickname())) {
            // Cannot use this nickname exception
        }
        if (!isAvailablePassword(request.getUserId(), request.getPassword())) {
            // Cannot use this password exception
        }
    }

    private boolean isAvailablePassword(String userId, String password) {
        String regex = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{9,12}$";
        Matcher matcher = Pattern.compile(regex).matcher(password);

        return matcher.matches() && !password.contains(userId) && !password.contains(" ");
    }

}
