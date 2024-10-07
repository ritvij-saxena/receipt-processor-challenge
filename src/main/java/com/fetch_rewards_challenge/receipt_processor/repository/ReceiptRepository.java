package com.fetch_rewards_challenge.receipt_processor.repository;

import com.fetch_rewards_challenge.receipt_processor.model.Receipt;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ReceiptRepository {

    private final ConcurrentHashMap<String, BigDecimal> receiptPoints = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Receipt> receipts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> processingStates = new ConcurrentHashMap<>();

    // Save receipt and its points
    public void saveReceipt(String id, Receipt receipt, BigDecimal points) {
        receipts.put(id, receipt);
        receiptPoints.put(id, points);
    }

    // Retrieve points by receipt ID
    public BigDecimal getPoints(String id) {
        return receiptPoints.get(id);
    }

    // Retrieve receipt by ID
    public Receipt getReceipt(String id) {
        return receipts.get(id);
    }

    // Set processing state of a receipt
    public void setProcessingState(String id, boolean isProcessing) {
        processingStates.put(id, isProcessing);
    }

    // Check if a receipt is still processing
    public boolean isProcessing(String id) {
        return processingStates.getOrDefault(id, false);
    }
}
