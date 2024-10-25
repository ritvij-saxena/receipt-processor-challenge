package com.fetch_rewards_challenge.receipt_processor.service.impl;

import com.fetch_rewards_challenge.receipt_processor.model.User;
import com.fetch_rewards_challenge.receipt_processor.repository.UserRepository;
import com.fetch_rewards_challenge.receipt_processor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public User getUser(String userId) {
        return userRepository.getUser(userId);
    }
}
