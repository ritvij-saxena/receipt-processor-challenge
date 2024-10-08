package com.fetch_rewards_challenge.receipt_processor.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class Item {

    @NotBlank(message = "Short description cannot be blank")
    private String shortDescription;

    @NotBlank(message = "Price cannot be blank")
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Price must be a valid decimal value with two decimal places")
    private String price;
}
