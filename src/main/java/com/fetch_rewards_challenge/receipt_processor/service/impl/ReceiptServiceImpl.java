package com.fetch_rewards_challenge.receipt_processor.service.impl;

import com.fetch_rewards_challenge.receipt_processor.exception.InvalidReceiptException;
import com.fetch_rewards_challenge.receipt_processor.model.Item;
import com.fetch_rewards_challenge.receipt_processor.model.Receipt;
import com.fetch_rewards_challenge.receipt_processor.repository.ReceiptRepository;
import com.fetch_rewards_challenge.receipt_processor.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;

    @Autowired
    public ReceiptServiceImpl(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public CompletableFuture<String> processReceipt(Receipt receipt) {
        // Check if items are present in the receipt
        if (receipt.getItems() == null || receipt.getItems().isEmpty()) {
            throw new InvalidReceiptException("Receipt must contain at least one item.");
        }

        String receiptId = UUID.randomUUID().toString();
        receiptRepository.setProcessingState(receiptId, true);

        BigDecimal points = calculatePoints(receipt);

        receiptRepository.saveReceipt(receiptId, receipt, points);
        receiptRepository.setProcessingState(receiptId, false);

        return CompletableFuture.completedFuture(receiptId);
    }

    @Override
    public BigDecimal getPoints(String id) {
        return receiptRepository.getPoints(id);
    }

    @Override
    public boolean isProcessing(String id) {
        return receiptRepository.isProcessing(id);
    }

    @Override
    public Receipt getReceiptById(String id) {
        return receiptRepository.getReceipt(id);
    }

    @Async
    public BigDecimal calculatePoints(Receipt receipt) {
        BigDecimal points = BigDecimal.ZERO;
        points = points.add(calculateRetailerPoints(receipt.getRetailer()));
        points = points.add(calculateTotalPoints(receipt.getTotal()));
        points = points.add(calculateItemPoints(receipt.getItems()));
        points = points.add(calculateDayPoints(receipt.getPurchaseDate()));
        points = points.add(calculateTimePoints(receipt.getPurchaseTime()));
        return points;
    }

    // Calculate points based on retailer name
    private BigDecimal calculateRetailerPoints(String retailer) {
        return BigDecimal.valueOf(retailer.replaceAll("[^a-zA-Z0-9]", "").length());
    }

    // Calculate points based on the total amount
    private BigDecimal calculateTotalPoints(String total) {
        BigDecimal totalAmount = new BigDecimal(total);
        BigDecimal points = BigDecimal.ZERO;

        if (totalAmount.stripTrailingZeros().scale() <= 0) {
            points = points.add(BigDecimal.valueOf(50));
        }
        if (totalAmount.remainder(BigDecimal.valueOf(0.25)).compareTo(BigDecimal.ZERO) == 0) {
            points = points.add(BigDecimal.valueOf(25));
        }

        return points;
    }

    // Calculate points based on items
    private BigDecimal calculateItemPoints(List<Item> items) {
        BigDecimal points = BigDecimal.ZERO;

        if (items != null && !items.isEmpty()) {
            points = points.add(BigDecimal.valueOf((items.size() / 2) * 5));
            for (Item item : items) {
                points = points.add(calculateItemDescriptionPoints(item));
            }
        }

        return points;
    }

    private BigDecimal calculateItemDescriptionPoints(Item item) {
        String description = item.getShortDescription().trim();
        BigDecimal price = new BigDecimal(item.getPrice());
        BigDecimal points = BigDecimal.ZERO;

        if (description.length() % 3 == 0) {
            points = points.add(price.multiply(BigDecimal.valueOf(0.2)).setScale(0, BigDecimal.ROUND_UP));
        }

        return points;
    }

    private BigDecimal calculateDayPoints(String purchaseDate) {
        String day = purchaseDate.split("-")[2];
        return (Integer.parseInt(day) % 2 != 0) ? BigDecimal.valueOf(6) : BigDecimal.ZERO;
    }

    private BigDecimal calculateTimePoints(String purchaseTime) {
        String[] timeParts = purchaseTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        return (hour >= 14 && hour < 16) ? BigDecimal.valueOf(10) : BigDecimal.ZERO;
    }
}
