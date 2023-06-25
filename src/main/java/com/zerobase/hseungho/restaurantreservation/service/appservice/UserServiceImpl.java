package com.zerobase.hseungho.restaurantreservation.service.appservice;

import com.zerobase.hseungho.restaurantreservation.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean checkUserIdAvailable(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    @Override
    public boolean checkNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

}
