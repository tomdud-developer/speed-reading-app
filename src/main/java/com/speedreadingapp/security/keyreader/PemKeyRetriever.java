package com.speedreadingapp.security.keyreader;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public interface PemKeyRetriever {
    PrivateKey getPemPrivateKey() throws InvalidKeySpecException;
    PublicKey getPemPublicKey() throws InvalidKeySpecException;
}
