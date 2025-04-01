package com.unfi.codechallenges.cars.exception;

@SuppressWarnings("serial")
public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String message) {
        super(message);
    }
}

