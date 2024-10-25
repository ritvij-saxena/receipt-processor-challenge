package com.fetch_rewards_challenge.receipt_processor.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
@Valid
public class User {
    private String id;

    @NotBlank(message = "First Name cannot be Blank")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last Name cannot be Blank")
    @JsonProperty("last_name")
    private String lastName;
    private Integer totalReceiptSubmitted;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = UUID.randomUUID().toString();
        this.totalReceiptSubmitted = 0;
    }
}
