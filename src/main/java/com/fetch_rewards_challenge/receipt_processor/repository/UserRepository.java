package com.fetch_rewards_challenge.receipt_processor.repository;


import com.fetch_rewards_challenge.receipt_processor.model.User;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {

    private static final ConcurrentHashMap<String, User> users;

    static {
        users = new ConcurrentHashMap<>();
    }

    public synchronized User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public synchronized User updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User or User ID cannot be null");
        }

        synchronized (UserRepository.class) {
            // Check if the user exists in the repository
            User existingUser = users.get(user.getId());
            if (existingUser == null) {
                throw new IllegalArgumentException("User not found with ID: " + user.getId());
            }

            // Update the existing user with new details
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setTotalReceiptSubmitted(user.getTotalReceiptSubmitted());

            // Save the updated user back into the repository
            users.put(existingUser.getId(), existingUser);
        }

        return user;
    }


    public User getUser(String userId) {
        return users.getOrDefault(userId, null);
    }
}
