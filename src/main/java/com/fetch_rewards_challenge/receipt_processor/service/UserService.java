package com.fetch_rewards_challenge.receipt_processor.service;

import com.fetch_rewards_challenge.receipt_processor.model.User;

import java.util.Map;

public interface UserService {
    User addUser(User user);
    User getUser(String userId);
}
