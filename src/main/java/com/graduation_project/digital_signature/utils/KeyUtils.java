package com.graduation_project.digital_signature.utils;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class KeyUtils {

    // ตั้งค่า keystore details
    private static final String KEYSTORE_PATH = "new_keystore.p12"; // ไฟล์ keystore อยู่ใน resources
    private static final String KEYSTORE_PASSWORD = "123456"; // รหัสผ่าน keystore
    private static final String KEY_ALIAS = "myalias"; // Alias ที่ตั้งไว้ตอนสร้าง keystore
    private static final String KEY_PASSWORD = "123456"; // รหัสผ่านของ Private Key (สมมติใช้รหัสเดียวกันกับ keystore)

    public static PrivateKey getPrivateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream keystoreStream = KeyUtils.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH)) {
            if (keystoreStream == null) {
                throw new Exception("Keystore file not found: " + KEYSTORE_PATH);
            }
            keyStore.load(keystoreStream, KEYSTORE_PASSWORD.toCharArray());
            return (PrivateKey) keyStore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
        }
    }

    public static Certificate[] getCertificateChain() throws Exception {
        KeyStore keyStore = loadKeyStore();
        Certificate[] chain = keyStore.getCertificateChain(KEY_ALIAS);
        if (chain == null || chain.length == 0) {
            throw new Exception("No certificate chain found for alias: " + KEY_ALIAS);
        }
        return chain;
    }

    private static KeyStore loadKeyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream keystoreStream = KeyUtils.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH)) {
            if (keystoreStream == null) {
                throw new Exception("Keystore file not found: " + KEYSTORE_PATH);
            }
            keyStore.load(keystoreStream, KEYSTORE_PASSWORD.toCharArray());
        }
        return keyStore;
    }
}