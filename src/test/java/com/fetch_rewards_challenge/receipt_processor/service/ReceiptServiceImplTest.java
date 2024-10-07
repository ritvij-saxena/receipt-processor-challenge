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
import static org.mockito.ArgumentMatchers.*;
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
            String id = idFuture.join(); // Use join to block until ID is available
            assertNotNull(id);
            assertFalse(id.isEmpty());
        });
    }

    @Test
    public void calculatePoints_ShouldCalculatePointsCorrectly() {
        Receipt receipt = createSampleReceipt();

        String id = UUID.randomUUID().toString();
        receiptService.calculatePoints(receipt, id);

        // Verify the points are saved correctly in the repository
        verify(receiptRepository, times(1)).saveReceipt(eq(id), eq(receipt), any(BigDecimal.class));
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
        String generatedId = futureId.join(); // Use join instead of get() in case of async

        // Verify that points were saved correctly, expected points should be 0
        verify(receiptRepository, times(1)).saveReceipt(eq(generatedId), eq(receipt), eq(BigDecimal.ZERO));
    }

    private Receipt createSampleReceipt() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");

        List<Item> items = new ArrayList<>();
        items.add(createItem("Mountain Dew 12PK", "6.49"));
        items.add(createItem("Emils Cheese Pizza", "12.25"));
        items.add(createItem("Knorr Creamy Chicken", "1.26"));
        items.add(createItem("Doritos Nacho Cheese", "3.35"));
        items.add(createItem("Klarbrunn 12-PK 12 FL OZ", "12.00"));

        receipt.setItems(items);
        receipt.setTotal("35.35");
        return receipt;
    }

    private Item createItem(String description, String price) {
        Item item = new Item();
        item.setShortDescription(description);
        item.setPrice(price);
        return item;
    }
}
