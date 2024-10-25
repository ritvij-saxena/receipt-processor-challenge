package com.fetch_rewards_challenge.receipt_processor.controller;

import com.fetch_rewards_challenge.receipt_processor.dto.PointsResponse;
import com.fetch_rewards_challenge.receipt_processor.dto.ReceiptResponse;
import com.fetch_rewards_challenge.receipt_processor.model.Receipt;
import com.fetch_rewards_challenge.receipt_processor.service.ReceiptService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/receipts")
@Validated
@Slf4j
public class ReceiptController {

    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    /**
     * Processes a receipt and returns the receipt ID.
     *
     * @param receipt The receipt to process.
     * @return A ResponseEntity containing the receipt ID.
     */

    @PostMapping("/process")
    public ResponseEntity<?> processReceipt(@RequestParam(name = "user_id") @NotBlank String userId,
                                            @RequestBody @Valid Receipt receipt) {
        String receiptId = receiptService.processReceipt(userId, receipt);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReceiptResponse(receiptId));
    }


    /**
     * Retrieves the points awarded for a given receipt ID.
     *
     * @param receiptId The ID of the receipt.
     * @return A ResponseEntity containing the points awarded.
     */
    @GetMapping("/{receiptId}/points")
    public ResponseEntity<?> getPoints(@PathVariable String receiptId) {
        log.info("In " + getClass().getName() + ":" + "getPoints");
        if (receiptService.isProcessing(receiptId)) {
            // Return a response indicating that processing is still in progress
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Processing");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }

        BigDecimal points = receiptService.getPoints(receiptId);
        if (points == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "receiptId: " + receiptId + "not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // ID not found
        }

        return ResponseEntity.ok(new PointsResponse(points));
    }

    // GET endpoint to retrieve receipt by receipt ID
    // out of scope of the assignment
    @GetMapping("/{receiptId}")
    public ResponseEntity<Map<String, Object>> getReceipt(@PathVariable String receiptId) {
        log.info("In " + getClass().getName() + ": getReceipt");

        // Check if the receipt is still processing
        if (receiptService.isProcessing(receiptId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Processing");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }

        // Retrieve the receipt by ID
        Receipt receipt = receiptService.getReceiptById(receiptId);
        if (receipt == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "receiptId: " + receiptId + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Get points awarded for the receipt
        BigDecimal points = receiptService.getPoints(receiptId);

        // Prepare the response
        Map<String, Object> response = new HashMap<>();
        response.put("receipt", receipt);
        response.put("points", points);
        return ResponseEntity.ok(response);
    }

}

