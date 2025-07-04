package com.raillylinker.redis_map_components.redis1_main;

import com.raillylinker.abstract_classes.BasicRedisMap;
import com.raillylinker.configurations.redis_configs.Redis1MainConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.jetbrains.annotations.*;

import java.util.List;

// [RedisMap 컴포넌트]
@Component
public class Redis1_Map_Test extends BasicRedisMap<Redis1_Map_Test.ValueVo> {
    public Redis1_Map_Test(
            // !!!RedisConfig 종류 변경!!!
            @Qualifier(Redis1MainConfig.REDIS_TEMPLATE_NAME)
            @NotNull RedisTemplate<String, String> redisTemplate
    ) {
        super(redisTemplate, MAP_NAME, ValueVo.class);
    }

    // <멤버 변수 공간>
    // !!!중복되지 않도록, 본 클래스명을 MAP_NAME 으로 설정하기!!!
    public static final String MAP_NAME = "Redis1_Map_Test";

    // !!!본 RedisMAP 의 Value 클래스 설정!!!
    @Data
    public static class ValueVo {
        // 기본 변수 타입 String 사용 예시
        private final @NotNull String content;
        // Object 변수 타입 사용 예시
        private final @NotNull InnerVo innerVo;
        // Object List 변수 타입 사용 예시
        private final @NotNull List<InnerVo> innerVoList;

        // 예시용 Object 데이터 클래스
        @Data
        public static class InnerVo {
            private final @NotNull String testString;
            private final @NotNull Boolean testBoolean;
        }
    }
}
