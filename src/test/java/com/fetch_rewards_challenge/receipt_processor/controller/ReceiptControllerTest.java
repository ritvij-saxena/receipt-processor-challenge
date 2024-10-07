package com.fetch_rewards_challenge.receipt_processor.controller;

import com.fetch_rewards_challenge.receipt_processor.model.Receipt;
import com.fetch_rewards_challenge.receipt_processor.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class ReceiptControllerTest {

    private ReceiptService receiptService;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        receiptService = Mockito.mock(ReceiptService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ReceiptController(receiptService)).build();
    }

    @Test
    public void processReceipt_ShouldReturnId() throws Exception {
        Receipt receipt = new Receipt();
        // Set fields like retailer, purchaseDate, etc., if needed for the receipt object.
        String id = "7fb1377b-b223-49d9-a31a-5a02701dd310";

        when(receiptService.processReceipt(any(Receipt.class)))
                .thenReturn(CompletableFuture.completedFuture(id));

        mockMvc.perform(post("/receipts/process")
                .contentType("application/json")
                .content("{\"retailer\": \"Target\", \"purchaseDate\": \"2022-01-01\", \"purchaseTime\": \"13:01\", \"items\": [], \"total\": \"35.35\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(id));
    }

    @Test
    public void getPoints_ShouldReturnPoints() throws Exception {
        String id = "7fb1377b-b223-49d9-a31a-5a02701dd310";
        int points = 32;

        when(receiptService.getPoints(id)).thenReturn(BigDecimal.valueOf(points));

        mockMvc.perform(get("/receipts/{id}/points", id))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"points\": 32}")); // Return a JSON object with "points"
    }


    @Test
    public void processReceipt_ShouldReturnBadRequest_ForEmptyReceipt() throws Exception {
        mockMvc.perform(post("/receipts/process")
                .contentType("application/json")
                .content("{}")) // Empty JSON
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid receipt submitted"));
    }


    @Test
    public void processReceipt_ShouldReturnBadRequest_ForInvalidJson() throws Exception {
        mockMvc.perform(post("/receipts/process")
                .contentType("application/json")
                .content("{invalidJson}")) // Invalid JSON
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid receipt submitted"));
    }

}
