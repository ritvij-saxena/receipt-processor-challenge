package com.fetch_rewards_challenge.receipt_processor.exception;

public class InvalidReceiptException extends RuntimeException {
    public InvalidReceiptException(String message) {
        super(message);
    }
}
