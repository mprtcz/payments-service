package com.mprtcz.controller;


public interface ValidationController {
    boolean validate(
        String sourceAccountNumber, String destinationAccountNumber);
}
