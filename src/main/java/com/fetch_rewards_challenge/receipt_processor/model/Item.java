package com.fetch_rewards_challenge.receipt_processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @NotBlank(message = "Short description cannot be empty")
    private String shortDescription;

    @NotNull(message = "Price cannot be null")
    private String price;

    @Override
    public String toString() {
        return "Item: " + shortDescription.trim() + ", Price: " + String.format("%.2f", price);
    }

}
