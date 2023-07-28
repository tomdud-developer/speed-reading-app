package com.speedreadingapp.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.speedreadingapp.configuration.JWTConfigurationProperties;
import com.speedreadingapp.exception.ExceptionWhenDefiningAlgorithm;
import com.speedreadingapp.security.keyreader.KeyReader;
import com.speedreadingapp.security.keyreader.PemKeyRetriever;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

@Service
@AllArgsConstructor
public class JWTAlgorithmProvider {

    private final JWTConfigurationProperties jwtConfigurationProperties;

    public Algorithm getAlgorithm() {
        try {
            PemKeyRetriever pemKeyRetriever = new KeyReader("RSA", jwtConfigurationProperties.getSecretKey(), jwtConfigurationProperties.getPublicKey());

            RSAPublicKey rsaPublicKey = (RSAPublicKey) pemKeyRetriever.getPemPublicKey();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) pemKeyRetriever.getPemPrivateKey();

            return  Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ExceptionWhenDefiningAlgorithm(e.getMessage());
        }
    }

}
