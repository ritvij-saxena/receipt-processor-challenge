package com.fetch_rewards_challenge.receipt_processor.service;

import com.fetch_rewards_challenge.receipt_processor.model.Item;
import com.fetch_rewards_challenge.receipt_processor.model.Receipt;
import com.fetch_rewards_challenge.receipt_processor.repository.ReceiptRepository;
import com.fetch_rewards_challenge.receipt_processor.service.impl.ReceiptServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReceiptServiceImplTest {

    private ReceiptServiceImpl receiptService;

    @Mock
    private ReceiptRepository receiptRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        receiptService = new ReceiptServiceImpl(receiptRepository);
    }

    @Test
    public void processReceipt_ShouldReturnId() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("35.35");
        receipt.setItems(new ArrayList<>());

        CompletableFuture<String> idFuture = receiptService.processReceipt(receipt);

        // Assert that the ID is returned
        assertNotNull(idFuture);
        assertDoesNotThrow(() -> {
            String id = idFuture.join();
            assertNotNull(id);
            assertFalse(id.isEmpty());
        });
    }

    @Test
    public void calculatePoints_ShouldReturnZero_WhenNoItems() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("35.35");
        receipt.setItems(new ArrayList<>()); // No items

        String id = UUID.randomUUID().toString();

        // Simulate the async processing
        CompletableFuture<String> futureId = receiptService.processReceipt(receipt);

        // Wait for the result
        String generatedId = futureId.join();
        assertNotNull(generatedId);

        verify(receiptRepository).saveReceipt(anyString(), eq(receipt), eq(BigDecimal.ZERO));
    }
}
