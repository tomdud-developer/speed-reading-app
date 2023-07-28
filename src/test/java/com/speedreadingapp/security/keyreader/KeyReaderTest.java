package com.speedreadingapp.security.keyreader;

import com.speedreadingapp.exception.ExceptionWhenReadingPemFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;

class KeyReaderTest {

    private static final String TEST_FILE_NAME_PRIVATE_KEY = "private_key.pem";
    private static final String TEST_FILE_NAME_PUBLIC_KEY = "public_key.pem";
    private static final String ALGORITHM = "RSA";

    static PemKeyRetriever pemKeyRetriever;
    static PemKeyRetriever pemKeyRetrieverFromStrings;

    @BeforeAll
    static void setUp() throws URISyntaxException, ExceptionWhenReadingPemFile, NoSuchAlgorithmException {
        URL urlToPrivateKey = KeyReaderTest.class.getClassLoader().getResource(TEST_FILE_NAME_PRIVATE_KEY);
        URL urlToPublicKey = KeyReaderTest.class.getClassLoader().getResource(TEST_FILE_NAME_PUBLIC_KEY);
        assert urlToPrivateKey != null;
        assert urlToPublicKey != null;

        pemKeyRetriever = new KeyReader(ALGORITHM, Path.of(urlToPrivateKey.toURI()) , Path.of(urlToPublicKey.toURI()));
        pemKeyRetrieverFromStrings = new KeyReader(ALGORITHM, getStringFromFile(urlToPrivateKey), getStringFromFile(urlToPublicKey));
    }

    private static String getStringFromFile(URL url) {
        try(
                InputStream inputStream = new FileInputStream(url.getFile());
                ) {
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getPemPrivateKey() throws InvalidKeySpecException {
        PrivateKey privateKey = pemKeyRetriever.getPemPrivateKey();
        Assertions.assertNotNull(privateKey);
    }

    @Test
    void getPemPublicKey() throws InvalidKeySpecException {
        PublicKey privateKey = pemKeyRetriever.getPemPublicKey();
        Assertions.assertNotNull(privateKey);
    }

    @Test
    void getPemPrivateKeyFromString() throws InvalidKeySpecException {
        PrivateKey privateKey = pemKeyRetrieverFromStrings.getPemPrivateKey();
        Assertions.assertNotNull(privateKey);
    }

    @Test
    void getPemPublicKeyFromString() throws InvalidKeySpecException {
        PublicKey privateKey = pemKeyRetrieverFromStrings.getPemPublicKey();
        Assertions.assertNotNull(privateKey);
    }


}