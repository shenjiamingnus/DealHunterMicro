package com.nus.dhproduct.exception;

public class PriceHistoryServiceException extends RuntimeException  {

    public PriceHistoryServiceException(String message) {
        super(message);
    }

    public PriceHistoryServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
