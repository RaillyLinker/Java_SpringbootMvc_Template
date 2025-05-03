package com.raillylinker.abstract_classes;

import com.google.gson.Gson;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.*;
import java.util.concurrent.TimeUnit;

// [RedisMap 의 Abstract 클래스]
// 본 추상 클래스를 상속받은 클래스를 key, value, expireTime 및 Redis 저장, 삭제, 조회 기능 메소드를 가진 클래스로 만들어줍니다.
// Redis Storage 를 Map 타입처럼 사용 가능하도록 래핑해주는 역할을 합니다.
public abstract class BasicRedisMap<ValueVo> {
    public BasicRedisMap(
            @NotNull RedisTemplate<String, String> redisTemplateObj,
            @NotNull String mapName,
            @NotNull Class<ValueVo> clazz
    ) {
        this.redisTemplateObj = redisTemplateObj;
        this.mapName = mapName;
        this.clazz = clazz;
    }

    private final @NotNull RedisTemplate<String, String> redisTemplateObj;
    private final @NotNull String mapName;
    private final @NotNull Class<ValueVo> clazz;
    private final @NotNull Gson gson = new Gson();

    // <공개 메소드 공간>
    // (RedisMap 에 Key-Value 저장)
    public void saveKeyValue(
            @NotNull String key,
            @NotNull ValueVo value,
            @Nullable Long expireTimeMs
    ) {
        // 입력 키 검증
        validateKey(key);

        // Redis Storage 에 실제로 저장 되는 키 (map 이름과 키를 합친 String)
        String innerKey = mapName + ":" + key; // 실제 저장되는 키 = 그룹명:키

        // Redis Storage 에 실제로 저장 되는 Value (Json String 형식)
        redisTemplateObj.opsForValue().set(innerKey, gson.toJson(value));

        if (expireTimeMs != null) {
            // Redis Key 에 대한 만료시간 설정
            redisTemplateObj.expire(innerKey, expireTimeMs, TimeUnit.MILLISECONDS);
        }
    }

    // (RedisMap 에 Key-Value 저장 - 정상 저장시 true, 동일 key 존재시 false 반환)
    public @NotNull Boolean saveKeyValueNoFix(
            @NotNull String key,
            ValueVo value,
            @Nullable Long expireTimeMs
    ) {
        // 입력 키 검증
        validateKey(key);

        // Redis Storage 에 실제로 저장 되는 키 (map 이름과 키를 합친 String)
        String innerKey = mapName + ":" + key; // 실제 저장되는 키 = 그룹명:키

        @Nullable Long scriptResult;
        if (expireTimeMs == null || expireTimeMs < 0) {
            // 만료시간 무한
            scriptResult = redisTemplateObj.execute(
                    RedisScript.of(
                            """
                                        if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then
                                            return 1
                                        else
                                            return 0
                                        end
                                    """,
                            Long.class
                    ),
                    Collections.singletonList(innerKey),
                    gson.toJson(value)
            );
        } else {
            // 만료시간 유한
            scriptResult = redisTemplateObj.execute(
                    RedisScript.of(
                            """
                                        if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then
                                            redis.call('pexpire', KEYS[1], ARGV[2])
                                            return 1
                                        else
                                            return 0
                                        end
                                    """,
                            Long.class
                    ),
                    Collections.singletonList(innerKey),
                    gson.toJson(value),
                    expireTimeMs.toString()
            );
        }

        return scriptResult != null && scriptResult == 1L;
    }

    // (RedisMap 의 모든 Key-Value 리스트 반환)
    public @NotNull List<RedisMapDataVo<ValueVo>> findAllKeyValues() {
        @NotNull List<RedisMapDataVo<ValueVo>> resultList = new ArrayList<>();

        @NotNull ScanOptions scanOptions = ScanOptions.scanOptions().match(mapName + ":*").build();
        @NotNull Cursor<String> cursor = redisTemplateObj.scan(scanOptions);

        try (cursor) {
            while (cursor.hasNext()) {
                // innerKey : Redis Storage 에 실제로 저장 되는 키 (map 이름과 키를 합친 String)
                @NotNull String innerKey = cursor.next();

                // 외부적으로 사용되는 Key (innerKey 에서 map 이름을 제거한 String)
                @NotNull String key = innerKey.substring((mapName + ":").length());

                // Redis Storage 에 실제로 저장 되는 Value (Json String 형식)
                @Nullable String innerValue = redisTemplateObj.opsForValue().get(innerKey);
                if (innerValue == null) continue;

                // 외부적으로 사용되는 Value (Json String 을 테이블 객체로 변환)
                ValueVo valueObject = gson.fromJson(innerValue, clazz);

                resultList.add(
                        new RedisMapDataVo<>(
                                key,
                                valueObject,
                                redisTemplateObj.getExpire(innerKey, TimeUnit.MILLISECONDS) // 남은 만료시간
                        )
                );
            }
        }

        return resultList;
    }

    // (RedisMap 의 key-Value 를 반환)
    public @Nullable RedisMapDataVo<ValueVo> findKeyValue(
            @NotNull String key
    ) {
        // 입력 키 검증
        validateKey(key);

        // Redis Storage 에 실제로 저장 되는 키 (map 이름과 키를 합친 String)
        @NotNull String innerKey = mapName + ":" + key;

        // Redis Storage 에 실제로 저장 되는 Value (Json String 형식)
        @Nullable String innerValue = redisTemplateObj.opsForValue().get(innerKey);

        if (innerValue == null) return null;

        // 외부적으로 사용되는 Value (Json String 을 테이블 객체로 변환)
        ValueVo valueObject =
                gson.fromJson(
                        innerValue, // 해석하려는 json 형식의 String
                        clazz // 파싱할 데이터 객체 타입
                );

        return new RedisMapDataVo<>(
                key,
                valueObject,
                redisTemplateObj.getExpire(innerKey, TimeUnit.MILLISECONDS)
        );
    }

    // (RedisMap 의 모든 Key-Value 리스트 삭제)
    public void deleteAllKeyValues() {
        @NotNull ScanOptions scanOptions = ScanOptions.scanOptions().match(mapName + ":*").build();
        @NotNull Cursor<String> cursor = redisTemplateObj.scan(scanOptions);

        @NotNull Set<String> keySet = new HashSet<>();

        try (cursor) {
            while (cursor.hasNext()) {
                keySet.add(cursor.next());
            }
        }

        // 삭제할 키가 있는 경우에만 삭제 요청
        if (!keySet.isEmpty()) {
            redisTemplateObj.delete(keySet);
        }
    }

    // (RedisMap 의 Key-Value 를 삭제)
    public void deleteKeyValue(
            @NotNull String key
    ) {
        // 입력 키 검증
        validateKey(key);

        // Redis Storage 에 실제로 저장 되는 키 (map 이름과 키를 합친 String)
        String innerKey = mapName + ":" + key;

        redisTemplateObj.delete(innerKey);
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>
    // (입력 키 검증 함수)
    private void validateKey(
            @NotNull String key
    ) {
        if (key.trim().isEmpty()) {
            throw new RuntimeException("key는 비어있을 수 없습니다.");
        }
        if (key.contains(":")) {
            throw new RuntimeException("key는 ':'를 포함할 수 없습니다.");
        }
    }


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
    // [RedisMap 의 출력값 데이터 클래스]
    @Data
    public static class RedisMapDataVo<ValueVo> {
        private final @NotNull String key;
        private final @NotNull ValueVo value;
        private final @Nullable Long expireTimeMs;
    }
}
