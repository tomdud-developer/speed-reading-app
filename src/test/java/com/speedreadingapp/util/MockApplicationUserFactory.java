package com.speedreadingapp.util;

import com.speedreadingapp.dto.RegisterRequestDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.service.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MockApplicationUserFactory {

    @Autowired
    public PasswordEncoder passwordEncoder;

    public ApplicationUser getMockUserWithHashedPassword() {
        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO
                .builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("password"))
                .firstname("testFirstname")
                .lastname("testLastname")
                .build();

        return ValueMapper.convertToEntity(registerRequestDTO);
    }

}
