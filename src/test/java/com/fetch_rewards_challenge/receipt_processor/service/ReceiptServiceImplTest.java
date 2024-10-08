package com.fetch_rewards_challenge.receipt_processor.service;

import com.fetch_rewards_challenge.receipt_processor.model.Item;
import com.fetch_rewards_challenge.receipt_processor.model.Receipt;
import com.fetch_rewards_challenge.receipt_processor.repository.ReceiptRepository;
import com.fetch_rewards_challenge.receipt_processor.service.impl.ReceiptServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReceiptServiceImplTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private ReceiptServiceImpl receiptService;

    private Receipt receiptWithKlarbrunn;
    private Receipt receiptWithGatorade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Klarbrunn receipt (from provided example)
        receiptWithKlarbrunn = new Receipt();
        receiptWithKlarbrunn.setRetailer("Target");
        receiptWithKlarbrunn.setPurchaseDate("2022-01-01");
        receiptWithKlarbrunn.setPurchaseTime("13:01");
        receiptWithKlarbrunn.setTotal("35.35");
        receiptWithKlarbrunn.setItems(Arrays.asList(
                new Item("Mountain Dew 12PK", "6.49"),
                new Item("Emils Cheese Pizza", "12.25"),
                new Item("Knorr Creamy Chicken", "1.26"),
                new Item("Doritos Nacho Cheese", "3.35"),
                new Item("   Klarbrunn 12-PK 12 FL OZ  ", "12.00")
        ));

        // Gatorade receipt (from provided example)
        receiptWithGatorade = new Receipt();
        receiptWithGatorade.setRetailer("M&M Corner Market");
        receiptWithGatorade.setPurchaseDate("2022-03-20");
        receiptWithGatorade.setPurchaseTime("14:33");
        receiptWithGatorade.setTotal("9.00");
        receiptWithGatorade.setItems(Arrays.asList(
                new Item("Gatorade", "2.25"),
                new Item("Gatorade", "2.25"),
                new Item("Gatorade", "2.25"),
                new Item("Gatorade", "2.25")
        ));
    }

    @Test
    public void testCalculatePoints_Klarbrunn() {
        BigDecimal points = receiptService.calculatePoints(receiptWithKlarbrunn);
        assertEquals(BigDecimal.valueOf(28), points);  // Expected value from the example breakdown
    }

    @Test
    public void testCalculatePoints_Gatorade() {
        BigDecimal points = receiptService.calculatePoints(receiptWithGatorade);
        assertEquals(BigDecimal.valueOf(109), points);  // Expected value from the example breakdown
    }

    @Test
    public void testProcessReceipt() {
        receiptService.processReceipt(receiptWithKlarbrunn);
        verify(receiptRepository, times(1)).setProcessingState(anyString(), eq(true));
    }

    @Test
    public void testIsProcessing_True() {
        String receiptId = "receipt-1";
        when(receiptRepository.isProcessing(receiptId)).thenReturn(true);

        boolean isProcessing = receiptService.isProcessing(receiptId);
        assertTrue(isProcessing);
        verify(receiptRepository, times(1)).isProcessing(receiptId);
    }

    @Test
    public void testIsProcessing_False() {
        String receiptId = "receipt-2";
        when(receiptRepository.isProcessing(receiptId)).thenReturn(false);

        boolean isProcessing = receiptService.isProcessing(receiptId);
        assertFalse(isProcessing);
        verify(receiptRepository, times(1)).isProcessing(receiptId);
    }

    @Test
    public void testProcessReceipt_2() throws InterruptedException {
        // Act: Process the receipt
        String actualReceiptId = receiptService.processReceipt(receiptWithKlarbrunn);

        // Assert that the returned receipt ID is not null
        assertNotNull(actualReceiptId);

        // Verify the interactions with the repository
        verify(receiptRepository).setProcessingState(eq(actualReceiptId), eq(true));
        verify(receiptRepository).saveReceipt(eq(actualReceiptId), any(Receipt.class), any(BigDecimal.class));
        verify(receiptRepository).setProcessingState(eq(actualReceiptId), eq(false));
    }
}
