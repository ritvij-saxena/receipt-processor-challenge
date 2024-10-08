package com.fetch_rewards_challenge.receipt_processor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointsResponse {
    private BigDecimal points;
}
