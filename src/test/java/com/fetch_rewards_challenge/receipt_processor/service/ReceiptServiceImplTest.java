package com.fetch_rewards_challenge.receipt_processor.service;

import com.fetch_rewards_challenge.receipt_processor.exception.InvalidReceiptException;
import com.fetch_rewards_challenge.receipt_processor.model.Item;
import com.fetch_rewards_challenge.receipt_processor.model.Receipt;
import com.fetch_rewards_challenge.receipt_processor.repository.ReceiptRepository;
import com.fetch_rewards_challenge.receipt_processor.service.impl.ReceiptServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

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

        List<Item> items = Arrays.asList(
                new Item("Mountain Dew 12PK", "6.49"),
                new Item("Emils Cheese Pizza", "12.25"),
                new Item("Knorr Creamy Chicken", "1.26"),
                new Item("Doritos Nacho Cheese", "3.35"),
                new Item("   Klarbrunn 12-PK 12 FL OZ  ", "12.00")
        );
        receipt.setItems(items);
        receipt.setTotal("35.35");

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
    public void calculatePoints_ShouldThrowInvalidReceiptException_WhenNoItems() {
        // Create a receipt with no items
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");
        receipt.setItems(Collections.emptyList());  // No items
        receipt.setTotal("0.00");

        // Expect an InvalidReceiptException to be thrown
        InvalidReceiptException exception = assertThrows(InvalidReceiptException.class, () -> {
            receiptService.processReceipt(receipt);
        });

        // Assert the exception message
        assertEquals("Receipt must contain at least one item.", exception.getMessage());
    }


}
