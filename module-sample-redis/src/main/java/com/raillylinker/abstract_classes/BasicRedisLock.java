package com.raillylinker.abstract_classes;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.jetbrains.annotations.*;

import java.util.Collections;
import java.util.UUID;
import java.util.function.Supplier;

// [RedisLock 의 Abstract 클래스]
public abstract class BasicRedisLock {
    public BasicRedisLock(
            @NotNull RedisTemplate<String, String> redisTemplateObj,
            @NotNull String mapName
    ) {
        this.redisTemplateObj = redisTemplateObj;
        this.mapName = mapName;
    }

    private final @NotNull RedisTemplate<String, String> redisTemplateObj;
    private final @NotNull String mapName;

    // <공개 메소드 공간>
    // (락 획득 메소드 - Lua 스크립트 적용)
    public @Nullable String tryLock(
            // Lock 키(실제 Redis 에 저장되는 키는 "$mapName:${key}" 이러한 형태입니다.)
            @NotNull String key,
            // Lock 만료 시간(이 시간이 지나면 자동으로 락 정보가 사라집니다.)
            @NotNull Long expireTimeMs
    ) {
        @NotNull String uuid = UUID.randomUUID().toString();

        // Redis Storage 에 실제로 저장 되는 키 (map 이름과 키를 합친 String)
        @NotNull String innerKey = mapName + ":" + key; // 실제 저장되는 키 = 그룹명:키

        @Nullable Long scriptResult;
        if (expireTimeMs < 0) {
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
                    uuid
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
                    uuid,
                    String.valueOf(expireTimeMs)
            );
        }

        return (scriptResult != null && scriptResult == 1L) ? uuid : null;
    }


    // ----
    // (락 해제 메소드 - Lua 스크립트 적용)
    public void unlock(
            // tryLock 에 사용한 Lock 키
            @NotNull String key,
            // tryLock 에서 발행한 uuid
            @NotNull String uuid
    ) {
        // Redis Storage 에 실제로 저장 되는 키 (map 이름과 키를 합친 String)
        @NotNull String innerKey = mapName + ":" + key; // 실제 저장되는 키 = 그룹명:키

        redisTemplateObj.execute(
                RedisScript.of(
                        """
                                    if redis.call('get', KEYS[1]) == ARGV[1] then
                                        return redis.call('del', KEYS[1])
                                    else
                                        return 0
                                    end
                                """,
                        Long.class
                ),
                Collections.singletonList(innerKey),
                uuid
        );
    }


    // ----
    // (락 강제 삭제)
    // 만료시간과 uuid 에 상관없이 무조건적으로 락을 삭제합니다.
    public void deleteLock(
            @NotNull String key
    ) {
        @NotNull String innerKey = mapName + ":" + key; // 실제 저장되는 키 = 그룹명:키

        redisTemplateObj.delete(innerKey);
    }


    // ----
    // (반복 락 획득 시도)
    // 락을 획득 할 때까지 반복적으로 tryLock 을 하고, 락 획득시 콜백을 실행합니다.
    public <OutputType> OutputType tryLockRepeat(
            // lock 키
            @NotNull String key,
            // lock 만료시간
            // 음수일 경우 무한 대기
            // whenLockPass 의 작업 수행 시간보다 커야하며, 작다면 작업을 수행하는 도중 락이 풀릴 것입니다.
            @NotNull Long expireTimeMs,
            // lock 을 얻으면 수행할 작업
            @NotNull Supplier<OutputType> whenLockPass,
            // lock 불통과시 기본 대기시간 (ex : 50L)
            @NotNull Long baseWaitTime,
            // lock 불통과 때마다 기본 대기시간이 증가하는 비율 (ex : 0.1)
            @NotNull Double incrementalFactor,
            // 대기 시간 증가 최대값 (ex : 100L)
            @NotNull Long maxWaitTime
    ) throws InterruptedException {
        // 공유 락 해제 키
        @Nullable String unLockKey = null;
        // 현재 재시도 횟수
        @NotNull Long attempt = 0L;

        while (unLockKey == null) {
            // 공유 락을 얻을 때 까지 반복
            unLockKey = this.tryLock(key, expireTimeMs);
            if (unLockKey == null) {
                // 공유 락을 못 얻었다면 대기 시간 증가(불발 횟수에 따라 대기 시간 증가 처리)
                long calcWaitTime = baseWaitTime + (long) (baseWaitTime * attempt * incrementalFactor);

                if (attempt < Long.MAX_VALUE) {
                    // 재시도 횟수 오버플로우 방지
                    attempt++;
                }

                long waitTime = Math.max(baseWaitTime, Math.min(calcWaitTime, maxWaitTime));

                Thread.sleep(waitTime);
            }
        }

        OutputType output;

        try {
            output = whenLockPass.get();
        } finally {
            this.unlock(key, unLockKey);
        }

        return output;
    }
}
