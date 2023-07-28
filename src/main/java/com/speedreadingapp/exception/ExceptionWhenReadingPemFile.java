package com.speedreadingapp.exception;

import java.io.IOException;

public class ExceptionWhenReadingPemFile extends IOException {
    public ExceptionWhenReadingPemFile(String string) {
        super(string);
    }
}
