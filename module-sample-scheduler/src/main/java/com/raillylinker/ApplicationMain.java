package com.raillylinker;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling // 스케쥴러 사용 설정
@EnableAsync // 스케쥴러의 Async 사용 설정
@ComponentScan(
        // !!!Bean 스캔할 모듈들의 패키지 리스트(group) 추가하기!!!
        basePackages = {"com.raillylinker"}
)
@SpringBootApplication(
        // Using generated security password 워닝을 피하기 위해 SpringSecurity 비밀번호 자동 생성 비활성화
        exclude = {UserDetailsServiceAutoConfiguration.class}
)
public class ApplicationMain {
    @Bean
    public @NotNull CommandLineRunner init() {
        return args -> {
            // 서버 타임존 명시적 설정 (UTC, Asia/Seoul, ...)
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
            // System.out.println("Current TimeZone: " + TimeZone.getDefault().getID());
        };
    }

    public static void main(
            @NotNull String[] args
    ) {
        SpringApplication.run(ApplicationMain.class, args);
    }
}
