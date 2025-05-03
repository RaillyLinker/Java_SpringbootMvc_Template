package com.raillylinker.configurations;

import com.raillylinker.redis_map_components.redis1_main.Redis1_Map_TotalAuthForceExpireAuthorizationSet;
import com.raillylinker.util_components.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.jetbrains.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// [서비스 보안 시큐리티 설정]
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (비밀번호 인코딩, 매칭시 사용할 객체)
    // 해싱에 사용된 솔트 값은 생성된 암호문에 포함되므로 따로 설정할 필요가 없습니다.
    @Bean
    public @NotNull PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public @NotNull CorsConfigurationSource corsConfigurationSource() {
        @NotNull CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setMaxAge(3600L);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    protected @NotNull SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // !!!경로별 적용할 Security 필터 체인 Bean 작성하기!!!

    // [기본적으로 모든 요청 Open]
    @Bean
    @Order()
    public @NotNull SecurityFilterChain securityFilterChainMainSc(
            @NotNull HttpSecurity http
    ) throws Exception {
        // cors 적용(서로 다른 origin 의 웹화면에서 리퀘스트 금지)
        http.cors(
                cors -> {
                }
        );

        // (사이즈간 위조 요청(Cross site Request forgery) 방지 설정)
        // csrf 설정시 POST, PUT, DELETE 요청으로부터 보호하며 csrf 토큰이 포함되어야 요청을 받아들이게 됨
        // Rest API 에선 Token 이 요청의 위조 방지 역할을 하기에 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        http.httpBasic(AbstractHttpConfigurer::disable);

        // Token 인증을 위한 세션 비활성화
        http.sessionManagement(
                sessionManagementCustomizer -> sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 스프링 시큐리티 기본 로그인 화면 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);

        // 스프링 시큐리티 기본 로그아웃 비활성화
        http.logout(AbstractHttpConfigurer::disable);

        // (API 요청 제한)
        // 기본적으로 모두 Open
        http.authorizeHttpRequests(
                authorizeHttpRequestsCustomizer -> {
                    // 모든 요청 허용
                    authorizeHttpRequestsCustomizer.anyRequest().permitAll();
                }
        );

        return http.build();
    }


    // ----
    // [JWT 토큰 인증 체계 적용 필터 체인]
    @Bean
    @Order(1)
    public @NotNull SecurityFilterChain securityFilterChainToken(
            @NotNull HttpSecurity http,
            @NotNull AuthTokenFilterTotalAuth authTokenFilterTotalAuth
    ) throws Exception {
        @NotNull HttpSecurity securityMatcher =
                http.securityMatcher(authTokenFilterTotalAuth.securityUrlList.toArray(new String[0]));

        securityMatcher.headers(
                headersCustomizer -> {
                    // iframe 허용 설정
                    // 기본은 허용하지 않음, sameOrigin 은 같은 origin 일 때에만 허용하고, disable 은 모두 허용
                    headersCustomizer.frameOptions(
                            HeadersConfigurer.FrameOptionsConfig::sameOrigin
                    );
                }
        );

        // cors 적용(서로 다른 origin 의 웹화면에서 리퀘스트 금지)
        securityMatcher.cors(
                cors -> {
                }
        );

        // (사이즈간 위조 요청(Cross site Request forgery) 방지 설정)
        // csrf 설정시 POST, PUT, DELETE 요청으로부터 보호하며 csrf 토큰이 포함되어야 요청을 받아들이게 됨
        // Rest API 에선 Token 이 요청의 위조 방지 역할을 하기에 비활성화
        securityMatcher.csrf(AbstractHttpConfigurer::disable);

        securityMatcher.httpBasic(AbstractHttpConfigurer::disable);

        // Token 인증을 위한 세션 비활성화
        securityMatcher.sessionManagement(
                sessionManagementCustomizer -> sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 스프링 시큐리티 기본 로그인 화면 비활성화
        securityMatcher.formLogin(AbstractHttpConfigurer::disable);

        // 스프링 시큐리티 기본 로그아웃 비활성화
        securityMatcher.logout(AbstractHttpConfigurer::disable);

        // (Token 인증 검증 필터 연결)
        // API 요청마다 헤더로 들어오는 인증 토큰 유효성을 검증
        securityMatcher.addFilterBefore(
                authTokenFilterTotalAuth,
                UsernamePasswordAuthenticationFilter.class
        );

        // 예외처리
        securityMatcher.exceptionHandling(
                exceptionHandlingCustomizer -> {
                    // 비인증(Security Context 에 멤버 정보가 없음) 처리
                    exceptionHandlingCustomizer.authenticationEntryPoint(
                            (request, response, authException) -> {
                                // Http Status 401
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: UnAuthorized");
                            }
                    );
                    // 비인가(멤버 권한이 충족되지 않음) 처리
                    exceptionHandlingCustomizer.accessDeniedHandler(
                            (request, response, accessDeniedException) -> {
                                // Http Status 403
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error: Forbidden");
                            }
                    );
                }
        );

        // (API 요청 제한)
        // 기본적으로 모두 Open
        securityMatcher.authorizeHttpRequests(
                authorizeHttpRequestsCustomizer -> {
                    authorizeHttpRequestsCustomizer.anyRequest().permitAll();
                    /*
                        본 서버 접근 보안은 블랙 리스트 방식을 사용합니다.
                        일반적으로 모든 요청을 허용하며, 인증/인가가 필요한 부분에는,
                        @PreAuthorize("isAuthenticated() and (hasRole('ROLE_DEVELOPER') or hasRole('ROLE_ADMIN'))")
                        위와 같은 어노테이션을 접근 통제하고자 하는 API 위에 달아주면 인증 필터가 동작하게 됩니다.
                     */
                }
        );

        return securityMatcher.build();
    }

    // 인증 토큰 검증 필터 - API 요청마다 검증 실행
    @Component
    public static class AuthTokenFilterTotalAuth extends OncePerRequestFilter {
        public AuthTokenFilterTotalAuth(
                @NotNull Redis1_Map_TotalAuthForceExpireAuthorizationSet expireTokenRedis,
                @NotNull JwtTokenUtil jwtTokenUtil
        ) {
            this.expireTokenRedis = expireTokenRedis;
            this.jwtTokenUtil = jwtTokenUtil;
        }

        // <멤버 변수 공간>
        private final @NotNull Redis1_Map_TotalAuthForceExpireAuthorizationSet expireTokenRedis;
        private final @NotNull JwtTokenUtil jwtTokenUtil;

        // !!!아래 인증 관련 설정 정보 변수들의 값을 수정하기!!!
        // 계정 설정 - JWT 비밀키
        public final @NotNull String authJwtSecretKeyString = "123456789abcdefghijklmnopqrstuvw";

        // 계정 설정 - JWT AccessToken 유효기간(초)
        public final @NotNull Long authJwtAccessTokenExpirationTimeSec = 60L * 30L; // 30분

        // 계정 설정 - JWT RefreshToken 유효기간(초)
        public final @NotNull Long authJwtRefreshTokenExpirationTimeSec = 60L * 60L * 24L * 7L; // 7일

        // 계정 설정 - JWT 본문 암호화 AES256 IV 16자
        public final @NotNull String authJwtClaimsAes256InitializationVector = "odkejduc726dj48d";

        // 계정 설정 - JWT 본문 암호화 AES256 암호키 32자
        public final @NotNull String authJwtClaimsAes256EncryptionKey = "8fu3jd0ciiu3384hfucy36dye9sjv7b3";

        // 계정 설정 - JWT 발행자
        public final @NotNull String authJwtIssuer = "com.raillylinker.my-service";

        // 본 시큐리티 필터가 관리할 주소 체계
        public final @NotNull List<String> securityUrlList =
                List.of(
                        "/security/**"
                ); // 위 모든 경로에 적용

        // ---------------------------------------------------------------------------------------------
        // <공개 메소드 공간>
        @Override
        protected void doFilterInternal(
                @NotNull HttpServletRequest request,
                @NotNull HttpServletResponse response,
                @NotNull FilterChain filterChain
        ) throws ServletException, IOException {
            // 패턴에 매치되는지 확인
            boolean patternMatch = false;

            for (@NotNull String filterPattern : securityUrlList) {
                if (new AntPathRequestMatcher(filterPattern).matches(request)) {
                    patternMatch = true;
                    break;
                }
            }

            if (!patternMatch) {
                // 이 필터를 실행해야 할 패턴이 아님.

                // 다음 필터 실행
                filterChain.doFilter(request, response);
                return;
            }

            // 인증 결과 == 유저 권한 리스트
            @Nullable ArrayList<GrantedAuthority> authResult;
            try {
                authResult = checkRequestAuthorization(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // 인증 결과가 null 입니다.
            if (authResult == null) {
                // 다음 필터 실행
                filterChain.doFilter(request, response);
                return;
            }

            // (검증된 멤버 정보와 권한 정보를 Security Context 에 입력)
            // authentication 정보가 context 에 존재하는지 여부로 로그인 여부를 확인
            @NotNull UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            null,
                            null,
                            authResult
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        }

        // (request 의 인증/인가 정보 처리)
        // HttpServletRequest 에서 인증/인가 정보를 추출해 검증합니다.
        // 정상 인증시 권한 리스트가 반환되며, 인증 실패시 null 이 반환됩니다.
        public @Nullable ArrayList<GrantedAuthority> checkRequestAuthorization(
                @NotNull HttpServletRequest request
        ) throws Exception {
            // (리퀘스트에서 가져온 AccessToken 검증)
            // 헤더의 Authorization 의 값 가져오기
            // 정상적인 토큰값은 "Bearer {Token String}" 형식으로 온다고 가정.
            @Nullable String authorization = request.getHeader("Authorization"); // ex : "Bearer aqwer1234"
            if (authorization == null) return null; // Authorization 에 토큰을 넣지 않은 경우 = 인증 / 인가를 받을 의도가 없음
            return checkRequestAuthorization(authorization);
        }

        public @Nullable ArrayList<GrantedAuthority> checkRequestAuthorization(
                @NotNull String authorization
        ) throws Exception {
            // 타입과 토큰을 분리
            @NotNull String[] authorizationSplit = authorization.split(" ");
            if (authorizationSplit.length < 2) return null;

            // 타입으로 추정되는 문장이 존재할 때
            // 타입 분리
            @NotNull String tokenType = authorizationSplit[0].trim(); // 첫번째 단어는 토큰 타입
            @NotNull String accessToken = authorizationSplit[1].trim(); // 앞의 타입을 자르고 남은 토큰

            // 강제 토큰 만료 검증
            boolean forceExpired;
            try {
                forceExpired = expireTokenRedis.findKeyValue(tokenType + "_" + accessToken) != null;
            } catch (Exception e) {
                e.printStackTrace();
                forceExpired = false;
            }

            if (forceExpired || accessToken.isEmpty()) {
                return null;
            }

            if ("bearer".equalsIgnoreCase(tokenType)) { // Bearer JWT 토큰 검증
                // 토큰 문자열 해석 가능여부 확인
                @Nullable String accessTokenType;
                try {
                    accessTokenType = jwtTokenUtil.getTokenType(accessToken);
                } catch (Exception e) {
                    accessTokenType = null;
                }
                if (!"jwt".equalsIgnoreCase(accessTokenType) || // 토큰 타입이 JWT 가 아님
                        !"access".equalsIgnoreCase(
                                jwtTokenUtil.getTokenUsage(
                                        accessToken,
                                        authJwtClaimsAes256InitializationVector,
                                        authJwtClaimsAes256EncryptionKey
                                )
                        ) || // 토큰 용도가 다름
                        // 남은 시간이 최대 만료시간을 초과 (서버 기준이 변경되었을 때, 남은 시간이 더 많은 토큰을 견제하기 위한 처리)
                        jwtTokenUtil.getRemainSeconds(accessToken) > authJwtAccessTokenExpirationTimeSec ||
                        !authJwtIssuer.equals(jwtTokenUtil.getIssuer(accessToken)) || // 발행인 불일치
                        !jwtTokenUtil.validateSignature(
                                accessToken,
                                authJwtSecretKeyString
                        ) // 시크릿 검증이 무효 = 위변조 된 토큰
                ) {
                    return null;
                }

                // 토큰 만료 검증
                @NotNull Long jwtRemainSeconds = jwtTokenUtil.getRemainSeconds(accessToken);
                if (jwtRemainSeconds <= 0L) {
                    return null;
                }

                // 회원 권한
                @NotNull ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                for (@NotNull String memberRole : jwtTokenUtil.getRoleList(
                        accessToken,
                        authJwtClaimsAes256InitializationVector,
                        authJwtClaimsAes256EncryptionKey
                )) {
                    authorities.add(
                            new SimpleGrantedAuthority(memberRole)
                    );
                }
                return authorities;
            }

            return null;
        }
    }
}
