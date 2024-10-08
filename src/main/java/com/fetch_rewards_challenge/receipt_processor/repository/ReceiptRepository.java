package com.fetch_rewards_challenge.receipt_processor.repository;

import com.fetch_rewards_challenge.receipt_processor.model.Receipt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class ReceiptRepository {

    private final ConcurrentHashMap<String, BigDecimal> receiptPoints = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Receipt> receipts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> processingStates = new ConcurrentHashMap<>();

    // Save receipt and its points
    public String saveReceipt(String id, Receipt receipt, BigDecimal points) {
        log.info("In " + getClass().getName() + ":" + "saveReceipt");
        receipts.put(id, receipt);
        receiptPoints.put(id, points);
        return id;
    }

    // Retrieve points by receipt ID
    public BigDecimal getPoints(String id) {
        log.info("In " + getClass().getName() + ":" + "getPoints");
        return receiptPoints.get(id);
    }

    // Retrieve receipt by ID
    public Receipt getReceipt(String id) {
        log.info("In " + getClass().getName() + ":" + "getReceipt");
        return receipts.get(id);
    }

    // Set processing state of a receipt
    public void setProcessingState(String id, boolean isProcessing) {
        log.info("In " + getClass().getName() + ":" + "setProcessingState");
        processingStates.put(id, isProcessing);
    }

    // Check if a receipt is still processing
    public boolean isProcessing(String id) {
        log.info("In " + getClass().getName() + ":" + "isProcessing");
        return processingStates.getOrDefault(id, false);
    }
}
