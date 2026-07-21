package com.szymonpytel.employeedataservice.crypto;

import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SsnEncryptorTest {

    private final SsnEncryptor ssnEncryptor = new SsnEncryptor(generateTestKey());

    @Test
    void encryptThenDecryptReturnsOriginalValue() {
        String originalSsn = "123-45-6789";

        String encrypted = ssnEncryptor.encrypt(originalSsn);
        String decrypted = ssnEncryptor.decrypt(encrypted);

        assertEquals(originalSsn, decrypted);
    }

    @Test
    void encryptedValueDoesNotContainPlaintext() {
        String originalSsn = "123-45-6789";

        String encrypted = ssnEncryptor.encrypt(originalSsn);

        assertFalse(encrypted.contains(originalSsn));
    }

    @Test
    void encryptingSameValueTwiceProducesDifferentCiphertext() {
        String originalSsn = "123-45-6789";

        String firstEncryption = ssnEncryptor.encrypt(originalSsn);
        String secondEncryption = ssnEncryptor.encrypt(originalSsn);

        assertNotEquals(firstEncryption, secondEncryption);
    }

    private static String generateTestKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey key = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}