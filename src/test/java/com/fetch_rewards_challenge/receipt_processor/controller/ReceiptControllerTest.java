package com.fetch_rewards_challenge.receipt_processor.controller;

import com.fetch_rewards_challenge.receipt_processor.dto.PointsResponse;
import com.fetch_rewards_challenge.receipt_processor.dto.ReceiptResponse;
import com.fetch_rewards_challenge.receipt_processor.model.Item;
import com.fetch_rewards_challenge.receipt_processor.model.Receipt;
import com.fetch_rewards_challenge.receipt_processor.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class ReceiptControllerTest {

    @Mock
    private ReceiptService receiptService;

    @InjectMocks
    private ReceiptController receiptController;

    private Receipt receiptWithKlarbrunn;
    private Receipt receiptWithGatorade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Example 1: Klarbrunn receipt
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

        // Example 2: Gatorade receipt
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
    public void testProcessReceipt_Klarbrunn() {
        String receiptId = "receipt-1";
        when(receiptService.processReceipt(receiptWithKlarbrunn)).thenReturn(receiptId);

        ResponseEntity<?> response = receiptController.processReceipt(receiptWithKlarbrunn);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ReceiptResponse receiptResponse = (ReceiptResponse) response.getBody();
        assertNotNull(receiptResponse.getId());
        assertEquals(receiptId, receiptResponse.getId());
    }

    @Test
    public void testProcessReceipt_Gatorade() {
        String receiptId = "receipt-2";
        when(receiptService.processReceipt(receiptWithGatorade)).thenReturn(receiptId);

        ResponseEntity<?> response = receiptController.processReceipt(receiptWithGatorade);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ReceiptResponse receiptResponse = (ReceiptResponse) response.getBody();
        assertNotNull(receiptResponse.getId());
        assertEquals(receiptId, receiptResponse.getId());
    }

    @Test
    public void testGetPoints_Klarbrunn() {
        String receiptId = "receipt-1";
        BigDecimal expectedPoints = BigDecimal.valueOf(28);  // From the breakdown provided
        when(receiptService.getPoints(receiptId)).thenReturn(expectedPoints);

        ResponseEntity<?> response = receiptController.getPoints(receiptId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PointsResponse pointsResponse = (PointsResponse) response.getBody();
        assertNotNull(pointsResponse.getPoints());
        assertEquals(expectedPoints, pointsResponse.getPoints());
    }

    @Test
    public void testGetPoints_Gatorade() {
        String receiptId = "receipt-2";
        BigDecimal expectedPoints = BigDecimal.valueOf(109);  // From the breakdown provided
        when(receiptService.getPoints(receiptId)).thenReturn(expectedPoints);

        ResponseEntity<?> response = receiptController.getPoints(receiptId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PointsResponse pointsResponse = (PointsResponse) response.getBody();
        assertNotNull(pointsResponse.getPoints());
        assertEquals(expectedPoints, pointsResponse.getPoints());
    }
}
