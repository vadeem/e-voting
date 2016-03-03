/******************************************************************************
 * e-voting system                                                            *
 * Copyright (C) 2016 DSX Technologies Limited.                               *
 * *
 * This program is free software; you can redistribute it and/or modify       *
 * it under the terms of the GNU General Public License as published by       *
 * the Free Software Foundation; either version 2 of the License, or          *
 * (at your option) any later version.                                        *
 * *
 * This program is distributed in the hope that it will be useful,            *
 * but WITHOUT ANY WARRANTY; without even the implied                         *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * *
 * You can find copy of the GNU General Public License in LICENSE.txt file    *
 * at the top-level directory of this distribution.                           *
 * *
 * Removal or modification of this copyright notice is prohibited.            *
 * *
 ******************************************************************************/

package uk.dsxt.voting.common.utils;

import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class CryptoHelper {
    private static final String ENCODING = "UTF-8";

    @Getter
    private final String algoritm;

    @Getter
    private final String signatureAlgoritm;

    public static final CryptoHelper DEFAULT_CRYPTO_HELPER = new CryptoHelper("RSA", "MD5WithRSA");


    public CryptoHelper(String algoritm, String signatureAlgoritm) {
        this.algoritm = algoritm;
        this.signatureAlgoritm = signatureAlgoritm;
    }

    public PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        byte[] clear = Base64.getDecoder().decode(key64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance(algoritm);
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return priv;
    }

    public PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
        byte[] data = Base64.getDecoder().decode(stored);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance(algoritm);
        return fact.generatePublic(spec);
    }

    public String createSignature(String originalText, PrivateKey privateKey)
            throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] data = originalText.getBytes(ENCODING);
        Signature sig = Signature.getInstance(signatureAlgoritm);
        sig.initSign(privateKey);
        sig.update(data);
        byte[] signatureBytes = sig.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public boolean verifySignature(String originalText, String signature, PublicKey publicKey)
            throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] data = originalText.getBytes(ENCODING);
        Signature sig = Signature.getInstance(signatureAlgoritm);
        sig.initVerify(publicKey);
        sig.update(data);

        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return sig.verify(signatureBytes);
    }

    public String encrypt(String text, Key key) throws GeneralSecurityException, UnsupportedEncodingException {
        //TODO
        return text;
    }

    public String decrypt(String text, Key key) throws GeneralSecurityException, UnsupportedEncodingException {
        //TODO
        return text;
    }
}
