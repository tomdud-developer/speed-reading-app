package com.src.speedreadingapp.controlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="api/v1/user")
public class UserController {

    @GetMapping(value = "/test")
    @CrossOrigin
    public ResponseEntity<String> test() {
        try {

            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>("test", headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/test2")
    @CrossOrigin
    public String test2() {
        return "testing";
    }
}
