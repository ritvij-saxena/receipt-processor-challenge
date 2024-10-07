package com.fetch_rewards_challenge.receipt_processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @NotBlank(message = "Short description cannot be empty")
    private String shortDescription;

    @NotBlank(message = "Price cannot be empty")
    private String price;

    @Override
    public String toString() {
        return "Item: " + shortDescription.trim() + ", Price: " + price;
    }
}
