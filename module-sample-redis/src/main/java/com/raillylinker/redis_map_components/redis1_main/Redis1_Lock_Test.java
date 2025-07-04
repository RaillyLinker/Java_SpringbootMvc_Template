package com.raillylinker.redis_map_components.redis1_main;

import com.raillylinker.abstract_classes.BasicRedisLock;
import com.raillylinker.configurations.redis_configs.Redis1MainConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.jetbrains.annotations.*;

// [RedisMap 컴포넌트]
@Component
public class Redis1_Lock_Test extends BasicRedisLock {
    public Redis1_Lock_Test(
            // !!!RedisConfig 종류 변경!!!
            @Qualifier(Redis1MainConfig.REDIS_TEMPLATE_NAME)
            @NotNull RedisTemplate<String, String> redisTemplate
    ) {
        super(redisTemplate, MAP_NAME);
    }

    // <멤버 변수 공간>
    // !!!중복되지 않도록, 본 클래스명을 MAP_NAME 으로 설정하기!!!
    public static final @NotNull String MAP_NAME = "Redis1_Lock_Test";
}
