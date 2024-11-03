package com.mprtcz.validator.controller;


public interface ValidationController {
    boolean validate(
        String sourceAccountNumber, String destinationAccountNumber);
}
