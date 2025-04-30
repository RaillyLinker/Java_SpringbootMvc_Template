package com.raillylinker.services;

import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class RootService {
    public RootService(
            // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
            @Value("${spring.profiles.active:default}")
            @NotNull String activeProfile,

            // (스웨거 문서 공개 여부 설정)
            @Value("${springdoc.swagger-ui.enabled}")
            @NotNull Boolean swaggerEnabled
    ) {
        this.activeProfile = activeProfile;
        this.swaggerEnabled = swaggerEnabled;
    }

    // <멤버 변수 공간>
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    private final @NotNull String activeProfile;

    // (스웨거 문서 공개 여부 설정)
    private final @NotNull Boolean swaggerEnabled;

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(RootService.class);

    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (루트 홈페이지 반환 함수)
    public @Nullable ModelAndView getRootInfo(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull ModelAndView mv = new ModelAndView();
        mv.setViewName("home_page/home_page");

        mv.addObject(
                "viewModel",
                new GetHome(activeProfile, swaggerEnabled)
        );

        return mv;
    }

    // 데이터 클래스 GetHome을 Java POJO로 변환
    public static class GetHome {
        public GetHome(
                @NotNull String env,
                @NotNull Boolean showApiDocumentBtn
        ) {
            this.env = env;
            this.showApiDocumentBtn = showApiDocumentBtn;
        }

        public @NotNull String env;
        public @NotNull Boolean showApiDocumentBtn;
    }
}
