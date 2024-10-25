package com.fetch_rewards_challenge.receipt_processor.controller;

import com.fetch_rewards_challenge.receipt_processor.model.User;
import com.fetch_rewards_challenge.receipt_processor.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/adduser")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user) {
        User createdUser = userService.addUser(user);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "user added successfully");
        response.put("userId", createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/getuser/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") String userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "No user found with userId=" + userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
