package com.fetch_rewards_challenge.receipt_processor.utils;


import java.util.HashMap;
import java.util.Map;

public class BonusRewardManager {
    private static final Map<Integer, Integer> newUserRewards;

    static {
        newUserRewards = new HashMap<>();
        newUserRewards.put(0, 1000);
        newUserRewards.put(1, 500);
        newUserRewards.put(2, 250);
    }

    public static Integer getRewardForUserReceipts(Integer totalReceiptSubmitted) {
        return newUserRewards.getOrDefault(totalReceiptSubmitted, 0);
    }
}
