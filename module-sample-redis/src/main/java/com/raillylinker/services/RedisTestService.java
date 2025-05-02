package com.raillylinker.services;

import com.raillylinker.abstract_classes.BasicRedisMap;
import com.raillylinker.controllers.RedisTestController;
import com.raillylinker.redis_map_components.redis1_main.Redis1_Lock_Test;
import com.raillylinker.redis_map_components.redis1_main.Redis1_Map_Test;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
    Redis 는 주로 캐싱에 사용됩니다.
    이러한 특징에 기반하여, 본 프로젝트에서는 Redis 를 쉽고 편하게 사용하기 위하여 Key-Map 형식으로 래핑하여 사용하고 있으며,
    인 메모리 데이터 구조 저장소인 Redis 의 성능을 해치지 않기 위하여, Database 와는 달리 트랜젝션 처리를 따로 하지 않습니다.
    (Redis 는 애초에 고성능, 단순성을 위해 설계되었고, 롤백 기능을 지원하지 않으므로 일반적으로는 어플리케이션 레벨에서 처리합니다.)
    고로 Redis 에 값을 입력/삭제/수정하는 로직은, API 의 별도 다른 알고리즘이 모두 실행된 이후, "코드의 끝자락에서 한꺼번에 변경"하도록 처리하세요.
    그럼에도 트랜젝션 기능이 필요하다고 한다면,
    비동기 실행을 고려하여 Semaphore 등으로 락을 건 후, 기존 데이터를 백업한 후, 에러가 일어나면 복원하는 방식을 사용하면 됩니다.
 */
@Service
public class RedisTestService {
    public RedisTestService(
            // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
            @Value("${spring.profiles.active:default}")
            @NotNull String activeProfile,
            @NotNull Redis1_Map_Test redis1Test,
            @NotNull Redis1_Lock_Test redis1LockTest
    ) {
        this.activeProfile = activeProfile;
        this.redis1Test = redis1Test;
        this.redis1LockTest = redis1LockTest;
    }

    // <멤버 변수 공간>
    private final @NotNull String activeProfile;

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(RootService.class);

    private final @NotNull Redis1_Map_Test redis1Test;
    private final @NotNull Redis1_Lock_Test redis1LockTest;


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (Redis Key-Value 입력 테스트)
    public void insertRedisKeyValueTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull RedisTestController.InsertRedisKeyValueTestInputVo inputVo
    ) {
        @NotNull ArrayList<Redis1_Map_Test.ValueVo.InnerVo> innerVos = new ArrayList<>();
        innerVos.add(
                new Redis1_Map_Test.ValueVo.InnerVo("testObject1", false)
        );
        innerVos.add(
                new Redis1_Map_Test.ValueVo.InnerVo("testObject2", true)
        );

        redis1Test.saveKeyValue(
                inputVo.getKey(),
                new Redis1_Map_Test.ValueVo(
                        inputVo.getContent(),
                        new Redis1_Map_Test.ValueVo.InnerVo("testObject", true),
                        innerVos
                ),
                inputVo.getExpirationMs()
        );

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (Redis Key-Value 조회 테스트)
    public @Nullable RedisTestController.SelectRedisValueSampleOutputVo selectRedisValueSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String key
    ) {
        // 전체 조회 테스트
        @Nullable BasicRedisMap.RedisMapDataVo<Redis1_Map_Test.ValueVo> keyValue = redis1Test.findKeyValue(key);

        if (keyValue == null) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new RedisTestController.SelectRedisValueSampleOutputVo(
                Redis1_Map_Test.MAP_NAME,
                keyValue.getKey(),
                keyValue.getValue().getContent(),
                keyValue.getExpireTimeMs() == null ? 0L : keyValue.getExpireTimeMs()
        );
    }


    // ----
    // (Redis Key-Value 모두 조회 테스트)
    public @Nullable RedisTestController.SelectAllRedisKeyValueSampleOutputVo selectAllRedisKeyValueSample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        // 전체 조회 테스트
        @NotNull List<BasicRedisMap.RedisMapDataVo<Redis1_Map_Test.ValueVo>> keyValueList = redis1Test.findAllKeyValues();

        @NotNull ArrayList<RedisTestController.SelectAllRedisKeyValueSampleOutputVo.KeyValueVo> testEntityListVoList =
                new ArrayList<>();
        for (@NotNull BasicRedisMap.RedisMapDataVo<Redis1_Map_Test.ValueVo> keyValue : keyValueList) {
            testEntityListVoList.add(
                    new RedisTestController.SelectAllRedisKeyValueSampleOutputVo.KeyValueVo(
                            keyValue.getKey(),
                            keyValue.getValue().getContent(),
                            keyValue.getExpireTimeMs() == null ? 0L : keyValue.getExpireTimeMs()
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new RedisTestController.SelectAllRedisKeyValueSampleOutputVo(
                Redis1_Map_Test.MAP_NAME,
                testEntityListVoList
        );
    }


    // ----
    // (Redis Key-Value 삭제 테스트)
    public void deleteRedisKeySample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String key
    ) {
        // 전체 조회 테스트
        @Nullable BasicRedisMap.RedisMapDataVo<Redis1_Map_Test.ValueVo> keyValue = redis1Test.findKeyValue(key);

        if (keyValue == null) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return;
        }

        redis1Test.deleteKeyValue(key);

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (Redis Key-Value 모두 삭제 테스트)
    public void deleteAllRedisKeySample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        redis1Test.deleteAllKeyValues();

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (Redis Lock 테스트)
    public @Nullable RedisTestController.TryRedisLockSampleOutputVo tryRedisLockSample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @Nullable String lockKey = redis1LockTest.tryLock("test", 100000L);
        if (lockKey == null) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        } else {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return new RedisTestController.TryRedisLockSampleOutputVo(lockKey);
        }
    }


    // ----
    // (Redis unLock 테스트)
    public void unLockRedisLockSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String lockKey
    ) {
        redis1LockTest.unlock("test", lockKey);
        httpServletResponse.setStatus(HttpStatus.OK.value());
    }
}
