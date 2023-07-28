package com.speedreadingapp.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    String access_token;
    String refresh_token;
}
