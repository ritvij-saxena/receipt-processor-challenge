package com.fetch_rewards_challenge.receipt_processor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetch_rewards_challenge.receipt_processor.dto.ReceiptResponse;
import com.fetch_rewards_challenge.receipt_processor.model.Item;
import com.fetch_rewards_challenge.receipt_processor.model.Receipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Receipt receiptWithKlarbrunn;
    private Receipt receiptWithGatorade;

    @BeforeEach
    public void setUp() {
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
                new Item("Klarbrunn 12-PK 12 FL OZ", "12.00")
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
    public void testProcessReceipt_Klarbrunn() throws Exception {
        mockMvc.perform(post("/receipts/process")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(receiptWithKlarbrunn)))
                .andExpect(status().isCreated());  // Expect 201 Created
    }

    @Test
    public void testProcessReceipt_Gatorade() throws Exception {
        mockMvc.perform(post("/receipts/process")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(receiptWithGatorade)))
                .andExpect(status().isCreated());  // Expect 201 Created
    }

    @Test
    public void testGetPoints_Klarbrunn() throws Exception {
        MvcResult result = mockMvc.perform(post("/receipts/process")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(receiptWithKlarbrunn)))
                .andExpect(status().isCreated())
                .andReturn();

        String receiptId = objectMapper.readValue(result.getResponse().getContentAsString(), ReceiptResponse.class).getId();

        mockMvc.perform(get("/receipts/" + receiptId + "/points"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPoints_Gatorade() throws Exception {
        MvcResult result = mockMvc.perform(post("/receipts/process")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(receiptWithGatorade)))
                .andExpect(status().isCreated())
                .andReturn();
        String receiptId = objectMapper.readValue(result.getResponse().getContentAsString(), ReceiptResponse.class).getId();

        mockMvc.perform(get("/receipts/" + receiptId + "/points"))
                .andExpect(status().isOk());
    }


    @Test
    public void testProcessReceipt_InvalidInput_ShouldReturnBadRequest() throws Exception {
        Receipt invalidReceipt = new Receipt();
        invalidReceipt.setRetailer("");  // Invalid retailer name
        invalidReceipt.setPurchaseDate(null);  // Missing purchase date
        invalidReceipt.setItems(Arrays.asList());

        mockMvc.perform(post("/receipts/process")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidReceipt)))
                .andExpect(status().isBadRequest());  // Expect 400 Bad Request
    }
}
