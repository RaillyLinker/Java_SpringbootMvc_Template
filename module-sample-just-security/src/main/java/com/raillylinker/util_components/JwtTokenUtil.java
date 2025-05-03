package com.raillylinker.util_components;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.stereotype.Component;
import org.jetbrains.annotations.*;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

// [JWT 토큰 유틸]
@Component
public class JwtTokenUtil {
    public JwtTokenUtil(
            @NotNull CryptoUtil cryptoUtil
    ) {
        this.cryptoUtil = cryptoUtil;
    }

    private final @NotNull CryptoUtil cryptoUtil;

    // <공개 메소드 공간>
    // (액세스 토큰 발행)
    // memberRoleList : 멤버 권한 리스트 (ex : ["ROLE_ADMIN", "ROLE_DEVELOPER"])
    public @NotNull String generateAccessToken(
            @NotNull Long memberUid,
            @NotNull Long accessTokenExpirationTimeSec,
            @NotNull String jwtClaimsAes256InitializationVector,
            @NotNull String jwtClaimsAes256EncryptionKey,
            @NotNull String issuer,
            @NotNull String jwtSecretKeyString,
            @NotNull List<String> roleList
    ) throws Exception {
        return doGenerateToken(
                memberUid,
                "access",
                accessTokenExpirationTimeSec,
                jwtClaimsAes256InitializationVector,
                jwtClaimsAes256EncryptionKey,
                issuer,
                jwtSecretKeyString,
                roleList
        );
    }

    // (리프레시 토큰 발행)
    public @NotNull String generateRefreshToken(
            @NotNull Long memberUid,
            @NotNull Long refreshTokenExpirationTimeSec,
            @NotNull String jwtClaimsAes256InitializationVector,
            @NotNull String jwtClaimsAes256EncryptionKey,
            @NotNull String issuer,
            @NotNull String jwtSecretKeyString
    ) throws Exception {
        return doGenerateToken(
                memberUid,
                "refresh",
                refreshTokenExpirationTimeSec,
                jwtClaimsAes256InitializationVector,
                jwtClaimsAes256EncryptionKey,
                issuer,
                jwtSecretKeyString,
                null
        );
    }

    // (JWT Secret 확인)
    // : 토큰 유효성 검증. 유효시 true, 위변조시 false
    public @NotNull Boolean validateSignature(
            @NotNull String token,
            @NotNull String jwtSecretKeyString
    ) throws Exception {
        @NotNull String[] tokenSplit = token.split("\\.");
        @NotNull String header = tokenSplit[0];
        @NotNull String payload = tokenSplit[1];
        @NotNull String signature = tokenSplit[2];

        // base64 로 인코딩된 header 와 payload 를 . 로 묶은 후 이를 시크릿으로 HmacSha256 해싱을 적용하여 signature 를 생성
        @NotNull String newSig = cryptoUtil.hmacSha256(header + "." + payload, jwtSecretKeyString);

        // 위 방식으로 생성된 signature 가 token 으로 전달된 signature 와 동일하다면 위/변조되지 않은 토큰으로 판단 가능
        // = 발행시 사용한 시크릿과 검증시 사용된 시크릿이 동일
        return signature.equals(newSig);
    }

    // (JWT 정보 반환)
    // Member Uid
    public @NotNull Long getMemberUid(
            @NotNull String token,
            @NotNull String jwtClaimsAes256InitializationVector,
            @NotNull String jwtClaimsAes256EncryptionKey
    ) throws Exception {
        return Long.parseLong(
                cryptoUtil.decryptAES256(
                        parseJwtForPayload(token).get("mu").toString(),
                        "AES/CBC/PKCS5Padding",
                        jwtClaimsAes256InitializationVector,
                        jwtClaimsAes256EncryptionKey
                )
        );
    }

    // (Token 용도 (access or refresh) 반환)
    public @NotNull String getTokenUsage(
            @NotNull String token,
            @NotNull String jwtClaimsAes256InitializationVector,
            @NotNull String jwtClaimsAes256EncryptionKey
    ) throws Exception {
        return cryptoUtil.decryptAES256(
                parseJwtForPayload(token).get("tu").toString(),
                "AES/CBC/PKCS5Padding",
                jwtClaimsAes256InitializationVector,
                jwtClaimsAes256EncryptionKey
        );
    }

    // (멤버 권한 리스트 반환)
    public @NotNull List<String> getRoleList(
            @NotNull String token,
            @NotNull String jwtClaimsAes256InitializationVector,
            @NotNull String jwtClaimsAes256EncryptionKey
    ) throws Exception {
        @NotNull String rl = cryptoUtil.decryptAES256(
                parseJwtForPayload(token).get("rl").toString(),
                "AES/CBC/PKCS5Padding",
                jwtClaimsAes256InitializationVector,
                jwtClaimsAes256EncryptionKey
        );
        return new Gson().fromJson(rl, new TypeToken<List<String>>() {
        }.getType());
    }

    // (발행자 반환)
    public @NotNull String getIssuer(
            @NotNull String token
    ) {
        return parseJwtForPayload(token).get("iss").toString();
    }

    // (토큰 남은 유효 시간(초) 반환 (만료된 토큰이라면 0))
    public @NotNull Long getRemainSeconds(
            @NotNull String token
    ) {
        @NotNull Long exp = ((Number) parseJwtForPayload(token).get("exp")).longValue();
        @NotNull Long currentEpochSeconds = Instant.now().getEpochSecond();

        return currentEpochSeconds < exp ? exp - currentEpochSeconds : 0;
    }

    // (토큰 만료 일시 반환)
    public @NotNull LocalDateTime getExpirationDateTime(
            @NotNull String token
    ) {
        long exp = ((Number) parseJwtForPayload(token).get("exp")).longValue();
        @NotNull Instant expirationInstant = Instant.ofEpochSecond(exp);

        return LocalDateTime.ofInstant(expirationInstant, ZoneId.systemDefault());
    }

    // (토큰 타입)
    public @NotNull String getTokenType(
            @NotNull String token
    ) {
        return parseJwtForHeader(token).get("typ").toString();
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>
    // (JWT 토큰 생성)
    private @NotNull String doGenerateToken(
            @NotNull Long memberUid,
            @NotNull String tokenUsage,
            @NotNull Long expireTimeSec,
            @NotNull String jwtClaimsAes256InitializationVector,
            @NotNull String jwtClaimsAes256EncryptionKey,
            @NotNull String issuer,
            @NotNull String jwtSecretKeyString,
            @Nullable List<String> roleList
    ) throws Exception {
        @NotNull JwtBuilder jwtBuilder = Jwts.builder();

        @NotNull Map<String, Object> headersMap = new HashMap<>();

        headersMap.put("typ", "JWT");

        jwtBuilder.header().empty().add(headersMap);

        @NotNull Map<String, Object> claimsMap = new HashMap<>();

        // member uid
        claimsMap.put(
                "mu",
                cryptoUtil.encryptAES256(
                        String.valueOf(memberUid),
                        "AES/CBC/PKCS5Padding",
                        jwtClaimsAes256InitializationVector,
                        jwtClaimsAes256EncryptionKey

                )
        );

        // 멤버 권한 리스트
        if (roleList != null) {
            claimsMap.put(
                    "rl",
                    cryptoUtil.encryptAES256(
                            new Gson().toJson(roleList),
                            "AES/CBC/PKCS5Padding",
                            jwtClaimsAes256InitializationVector,
                            jwtClaimsAes256EncryptionKey
                    )
            );
        }

        // token usage
        claimsMap.put(
                "tu",
                cryptoUtil.encryptAES256(
                        tokenUsage,
                        "AES/CBC/PKCS5Padding",
                        jwtClaimsAes256InitializationVector,
                        jwtClaimsAes256EncryptionKey
                )
        );

        // 발행자
        claimsMap.put("iss", issuer);

        claimsMap.put("iat", Instant.now().getEpochSecond());
        claimsMap.put("exp", Instant.now().getEpochSecond() + expireTimeSec);
        claimsMap.put("cd", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss.SSSSSS")));

        jwtBuilder.claims().empty().add(claimsMap);

        jwtBuilder
                .signWith(
                        Keys.hmacShaKeyFor(jwtSecretKeyString.getBytes(StandardCharsets.UTF_8)),
                        Jwts.SIG.HS256
                );

        return jwtBuilder.compact();
    }

    // (base64 로 인코딩된 Header, Payload 를 base64 로 디코딩)
    private @NotNull Map<String, Object> parseJwtForHeader(
            @NotNull String jwt
    ) {
        @NotNull String header = cryptoUtil.base64Decode(jwt.split("\\.")[0]);
        return new BasicJsonParser().parseMap(header);
    }

    private @NotNull Map<String, Object> parseJwtForPayload(
            @NotNull String jwt
    ) {
        @NotNull String payload = cryptoUtil.base64Decode(jwt.split("\\.")[1]);
        return new BasicJsonParser().parseMap(payload);
    }
}
