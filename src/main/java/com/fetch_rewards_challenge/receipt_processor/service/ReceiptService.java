package com.fetch_rewards_challenge.receipt_processor.service;

import com.fetch_rewards_challenge.receipt_processor.model.Receipt;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public interface ReceiptService {
    String processReceipt(Receipt receipt);  // Process receipt async, return id sync
    BigDecimal getPoints(String id); // Get points for the given receipt ID
    Receipt getReceiptById(String id); // Get receipt for the given receipt ID
    boolean isProcessing(String id); // Check if the receipt is still processing

}
