package com.speedreadingapp.security.keyreader;

import com.speedreadingapp.exception.ExceptionWhenReadingPemFile;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
public class KeyReader implements PemKeyRetriever{

    private final KeyFactory keyFactory;
    private final byte[] privateKeyBytes;
    private final byte[] publicKeyBytes;

    public KeyReader(String algorithm, Path pathToPrivateKey, Path pathToPublicKey) throws NoSuchAlgorithmException, ExceptionWhenReadingPemFile {
        keyFactory = KeyFactory.getInstance(algorithm);
        privateKeyBytes = getBytesFromFile(pathToPrivateKey);
        publicKeyBytes = getBytesFromFile(pathToPublicKey);
    }

    public KeyReader(String algorithm, String privateKey, String publicKey) throws NoSuchAlgorithmException {
        keyFactory = KeyFactory.getInstance(algorithm);
        privateKeyBytes = privateKey.getBytes();
        publicKeyBytes = publicKey.getBytes();
    }

    @Override
    public PrivateKey getPemPrivateKey() throws InvalidKeySpecException {

        String temp = new String(privateKeyBytes);
        String privateKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replace("\n", "");

        byte [] decoded = Base64.getDecoder().decode(privateKeyPEM);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

        return keyFactory.generatePrivate(spec);
    }

    @Override
    public PublicKey getPemPublicKey() throws InvalidKeySpecException {

        String temp = new String(publicKeyBytes);
        String publicKeyPEM = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("\n", "");

        byte [] decoded = Base64.getDecoder().decode(publicKeyPEM);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

        return keyFactory.generatePublic(spec);
    }

    private byte[] getBytesFromFile(Path path) throws ExceptionWhenReadingPemFile {
        File file = path.toFile();

        try(
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        ) {
            byte[] bytes  = new byte[(int) file.length()];
            dataInputStream.readFully(bytes);
            return bytes;
        } catch (IOException exception) {
            log.error("Error while reading pem file");
            throw new ExceptionWhenReadingPemFile("Error while reading pem file");
        }
    }


}
