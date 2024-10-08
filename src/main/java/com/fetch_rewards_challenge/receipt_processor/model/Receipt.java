package com.fetch_rewards_challenge.receipt_processor.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class Receipt {
    
    @NotBlank(message = "Retailer cannot be blank")
    private String retailer;

    @NotBlank(message = "Purchase date cannot be blank")
    private String purchaseDate;

    @NotNull
    @NotBlank(message = "Purchase time cannot be blank")
    private String purchaseTime;

    @NotNull(message = "Items cannot be null")
    @Size(min = 1, message = "At least one item is required")
    @Valid  // Ensures validation for the nested Item list
    private List<Item> items;

    @NotBlank(message = "Total amount cannot be blank")
    private String total;
}
