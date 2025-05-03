package com.raillylinker.util_components;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import org.jetbrains.annotations.*;

// [암호화, 복호화 관련 유틸]
@Component
public class CryptoUtil {
    public CryptoUtil(
            @NotNull CustomUtil customUtil
    ) {
        this.customUtil = customUtil;
    }

    private final @NotNull CustomUtil customUtil;


    // ----
    // [암호화 / 복호화]
    // (AES256 암호화)
    public @NotNull String encryptAES256(
            @NotNull String text, // 암호화하려는 평문
            @NotNull String alg, // 암호화 알고리즘 (ex : "AES/CBC/PKCS5Padding")
            @NotNull String initializationVector, // 초기화 벡터 16byte = 16char
            @NotNull String encryptionKey // 암호화 키 32byte = 32char
    ) throws Exception {
        if (encryptionKey.length() != 32 || initializationVector.length() != 16) {
            throw new RuntimeException("encryptionKey length must be 32 and initializationVector length must be 16");
        }

        @NotNull Cipher cipher = Cipher.getInstance(alg);
        @NotNull SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        @NotNull IvParameterSpec ivParamSpec = new IvParameterSpec(initializationVector.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
        @NotNull byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // (AES256 복호화)
    public @NotNull String decryptAES256(
            @NotNull String cipherText, // 복호화하려는 암호문
            @NotNull String alg, // 암호화 알고리즘 (ex : "AES/CBC/PKCS5Padding")
            @NotNull String initializationVector, // 초기화 벡터 16byte = 16char
            @NotNull String encryptionKey // 암호화 키 32byte = 32char
    ) throws Exception {
        if (encryptionKey.length() != 32 || initializationVector.length() != 16) {
            throw new RuntimeException("encryptionKey length must be 32 and initializationVector length must be 16");
        }

        @NotNull Cipher cipher = Cipher.getInstance(alg);
        @NotNull SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        @NotNull IvParameterSpec ivParamSpec = new IvParameterSpec(initializationVector.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
        @NotNull byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        @NotNull byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // [인코딩 / 디코딩]
    // (Base64 인코딩)
    public @NotNull String base64Encode(
            @NotNull String str
    ) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    // (Base64 디코딩)
    public @NotNull String base64Decode(
            @NotNull String str
    ) {
        return new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // [해싱]
    // (SHA256 해싱)
    public @NotNull String hashSHA256(
            @NotNull String str
    ) throws Exception {
        @NotNull MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        return customUtil.bytesToHex(messageDigest.digest());
    }

    // (HmacSHA256)
    public @NotNull String hmacSha256(
            @NotNull String data,
            @NotNull String secret
    ) throws Exception {
        @NotNull Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        sha256Hmac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}
