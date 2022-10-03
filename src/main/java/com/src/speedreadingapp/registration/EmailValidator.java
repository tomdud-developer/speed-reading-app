package com.src.speedreadingapp.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;


@Service
public class EmailValidator implements Predicate<String> {

    public boolean test(String s) {
        return true;
    }
}
