package com.graduation_project.digital_signature.utils;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class KeyUtils {

    private static final String KEYSTORE_PATH = "new_keystore.p12"; // Keystore Path (เก็บใน resources)
    private static final String KEYSTORE_PASSWORD = "123456"; // Keystore Password
    private static final String KEY_ALIAS = "myalias"; // Alias ของ Private Key
    private static final String KEY_PASSWORD = "123456"; // Password ของ Private Key

    public static PrivateKey getPrivateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("SHA-384");
        InputStream keystoreStream = KeyUtils.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH);
        keyStore.load(keystoreStream, KEYSTORE_PASSWORD.toCharArray());
        return (PrivateKey) keyStore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
    }

    public static Certificate getCertificate() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("SHA-384");
        InputStream keystoreStream = KeyUtils.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH);
        keyStore.load(keystoreStream, KEYSTORE_PASSWORD.toCharArray());
        return keyStore.getCertificate(KEY_ALIAS);
    }
}
