package com.fetch_rewards_challenge.receipt_processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receipt {
    @NotBlank(message = "Retailer name cannot be empty")
    private String retailer;

    @NotBlank(message = "Purchase date cannot be empty")
    private String purchaseDate;

    @NotBlank(message = "Purchase time cannot be empty")
    private String purchaseTime;

    @NotNull(message = "Items cannot be null")
    @Valid  // Validate each Item in the list
    private List<Item> items;

    @NotBlank(message = "Total cannot be empty")
    private String total;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Receipt Details:\n");
        sb.append("Retailer: ").append(retailer).append("\n");
        sb.append("Purchase Date: ").append(purchaseDate).append("\n");
        sb.append("Purchase Time: ").append(purchaseTime).append("\n");
        sb.append("Items:\n");
        for (Item item : items) {
            sb.append("  - ").append(item).append("\n");
        }
        sb.append("Total: ").append(total).append("\n");
        return sb.toString();
    }
}
