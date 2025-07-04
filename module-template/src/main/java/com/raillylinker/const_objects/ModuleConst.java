package com.raillylinker.const_objects;

import java.util.UUID;

import org.jetbrains.annotations.*;

// [모듈 전역 상수 모음]
// 아래 변수들은 절대 런타임에 변경되어서는 안됩니다.
// 왜냐면, 서버 복제와 같은 Scale out 기법을 사용시 메모리에 저장되는 상태변수가 존재하면 에러가 날 것이기 때문입니다.
// 꼭 메모리에 저장을 해야한다면 Redis, Kafka 등을 사용해 결합성을 낮추는 방향으로 설계하세요.
public final class ModuleConst {
    // (DatabaseConfig)
    // !!!모듈 패키지명 작성!!!
    public static final @NotNull String PACKAGE_NAME = "com.raillylinker";

    // 서버 고유값
    public static final @NotNull String SERVER_UUID = System.currentTimeMillis() + "/" + UUID.randomUUID();
}
