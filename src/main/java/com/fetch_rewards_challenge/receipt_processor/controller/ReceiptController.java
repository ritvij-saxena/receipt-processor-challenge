package com.fetch_rewards_challenge.receipt_processor.controller;

import com.fetch_rewards_challenge.receipt_processor.dto.PointsResponse;
import com.fetch_rewards_challenge.receipt_processor.dto.ReceiptResponse;
import com.fetch_rewards_challenge.receipt_processor.model.Receipt;
import com.fetch_rewards_challenge.receipt_processor.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/receipts")
@Validated
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
    public ResponseEntity<ReceiptResponse> processReceipt(@Valid @RequestBody Receipt receipt) {
        String receiptId = receiptService.processReceipt(receipt);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReceiptResponse(receiptId));
    }

    /**
     * Retrieves the points awarded for a given receipt ID.
     *
     * @param id The ID of the receipt.
     * @return A ResponseEntity containing the points awarded.
     */
    @GetMapping("/{id}/points")
    public ResponseEntity<?> getPoints(@PathVariable String id) {
        if (receiptService.isProcessing(id)) {
            // Return a response indicating that processing is still in progress
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Processing");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }

        BigDecimal points = receiptService.getPoints(id);
        if (points == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "id: " + id + "not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // ID not found
        }

        return ResponseEntity.ok(new PointsResponse(points));
    }

    // GET endpoint to retrieve receipt by receipt ID
    // out of scope of the assignment
    @GetMapping("/{id}/receipt")
    public ResponseEntity<Map<String, Object>> getReceipt(@PathVariable String id) {
        if (receiptService.isProcessing(id)) {
            // Return a response indicating that processing is still in progress
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Processing");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }

        Receipt receipt = receiptService.getReceiptById(id);
        if (receipt == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "id: " + id + "not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // ID not found
        }

        Map<String, Object> response = new HashMap<>();
        response.put("receipt", receipt);
        return ResponseEntity.ok(response);
    }
}

