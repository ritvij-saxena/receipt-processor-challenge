package com.fetch_rewards_challenge.receipt_processor.controller;

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
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/receipts")
@Validated
public class ReceiptController {

    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    // POST endpoint to process the receipt
    @PostMapping("/process")
    public CompletableFuture<ResponseEntity<String>> processReceipt(@Valid @RequestBody Receipt receipt) {
        CompletableFuture<String> receiptIdFuture = receiptService.processReceipt(receipt);
        return receiptIdFuture.thenApply(receiptId ->
                ResponseEntity.status(HttpStatus.CREATED).body(receiptId))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid receipt submitted"));
    }

    // GET endpoint to retrieve points by receipt ID
    @GetMapping("/{id}/points")
    public ResponseEntity<Map<String, Object>> getPoints(@PathVariable String id) {
        if (receiptService.isProcessing(id)) {
            // Return a response indicating that processing is still in progress
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Processing");
            return ResponseEntity.status(202).body(response);
        }

        BigDecimal points = receiptService.getPoints(id);
        if (points == null) {
            return ResponseEntity.status(404).build(); // ID not found
        }

        Map<String, Object> response = new HashMap<>();
        response.put("points", points);
        return ResponseEntity.ok(response);
    }

    // GET endpoint to retrieve receipt by receipt ID
    // out of scope of the assignment
    @GetMapping("/{id}/receipt")
    public ResponseEntity<Map<String, Object>> getReceipt(@PathVariable String id) {
        if (receiptService.isProcessing(id)) {
            // Return a response indicating that processing is still in progress
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Processing");
            return ResponseEntity.status(202).body(response);
        }

        Receipt receipt = receiptService.getReceiptById(id);
        if (receipt == null) {
            return ResponseEntity.status(404).build(); // ID not found
        }

        Map<String, Object> response = new HashMap<>();
        response.put("receipt", receipt);
        return ResponseEntity.ok(response);
    }
}

