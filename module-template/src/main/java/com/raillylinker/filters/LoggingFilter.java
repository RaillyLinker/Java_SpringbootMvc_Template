package com.raillylinker.filters;

import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.jetbrains.annotations.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

// [API 별 Request / Response 로깅 필터]
// API 호출시마다 Request 와 Response 를 로깅하도록 처리했습니다.
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter extends OncePerRequestFilter {
    // <멤버 변수 공간>
    private final @NotNull Logger classLogger = LoggerFactory.getLogger(LoggingFilter.class);

    // 로깅 body 에 표시할 데이터 타입
    // 여기에 포함된 데이터 타입만 로그에 표시됩니다.
    private final @NotNull List<MediaType> visibleTypeList = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml")
    );


    // ---------------------------------------------------------------------------------------------
    // <상속 메소드 공간>
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        @NotNull LocalDateTime requestTime = LocalDateTime.now();

        // 요청자 Ip (ex : 127.0.0.1)
        @NotNull String clientAddressIp = request.getRemoteAddr();

        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        @NotNull ContentCachingRequestWrapper httpServletRequest =
                (request instanceof ContentCachingRequestWrapper) ?
                        (ContentCachingRequestWrapper) request
                        : new ContentCachingRequestWrapper(request);
        @NotNull ContentCachingResponseWrapper httpServletResponse =
                (response instanceof ContentCachingResponseWrapper) ?
                        (ContentCachingResponseWrapper) response
                        : new ContentCachingResponseWrapper(response);

        boolean isError = false;
        try {
            if ("text/event-stream".equals(httpServletRequest.getHeader("accept"))) {
                // httpServletResponse 를 넣어야 response Body 출력이 제대로 되지만 text/event-stream 연결에 에러가 발생함
                filterChain.doFilter(httpServletRequest, response);
            } else {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        } catch (Exception e) {
            if (!(e instanceof ServletException && e.getCause() instanceof AccessDeniedException)) {
                isError = true;
            }
            throw e;
        } finally {
            @NotNull String queryString = (httpServletRequest.getQueryString() != null) ? "?" + httpServletRequest.getQueryString() : "";
            @NotNull String endpoint = httpServletRequest.getMethod() + " " + httpServletRequest.getRequestURI() + queryString;

            @NotNull Map<String, String> requestHeaders = new HashMap<>();
            @NotNull Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                @NotNull String headerName = headerNames.nextElement();
                @NotNull String headerValue = httpServletRequest.getHeader(headerName);
                requestHeaders.put(headerName, headerValue);
            }

            byte[] requestContentByteArray = httpServletRequest.getContentAsByteArray();
            @NotNull String requestBody =
                    (requestContentByteArray.length > 0) ?
                            getContentByte(requestContentByteArray, httpServletRequest.getContentType())
                            : "";

            int responseStatus = httpServletResponse.getStatus();
            @NotNull String responseStatusPhrase = "";
            try {
                responseStatusPhrase = HttpStatus.valueOf(responseStatus).getReasonPhrase();
            } catch (Exception ignored) {
            }

            @NotNull Map<String, String> responseHeaders = new HashMap<>();
            @NotNull Collection<String> responseHeaderNames = httpServletResponse.getHeaderNames();
            for (String headerName : responseHeaderNames) {
                @Nullable String headerValue = httpServletResponse.getHeader(headerName);
                responseHeaders.put(headerName, headerValue);
            }

            byte[] responseContentByteArray = httpServletResponse.getContentAsByteArray();
            @NotNull String responseBody = (responseContentByteArray.length > 0) ? (
                    httpServletResponse.getContentType().startsWith("text/html") ?
                            "HTML Content" :
                            (httpServletRequest.getRequestURI().startsWith("/v3/api-docs") ||
                                    httpServletRequest.getRequestURI().equals("/swagger-ui/swagger-initializer.js")) ?
                                    "Skip" :
                                    getContentByte(responseContentByteArray, httpServletResponse.getContentType())) : "";

            // 로깅 처리
            @NotNull String logMessage = String.format("""
                                >>ApiFilterLog>>
                                {
                                    "request_info" : {
                                        "request_time" : "%s",
                                        "end_point" : "%s",
                                        "client_ip" : "%s",
                                        "request_headers" : "%s",
                                        "request_body" : "%s"
                                    },
                                    "response_info" :{
                                        "response_status" : "%d %s",
                                        "processing_duration_ms" : "%d",
                                        "response_headers" : "%s",
                                        "response_body" : "%s"
                                    }
                                }
                            """, requestTime, endpoint, clientAddressIp, requestHeaders, requestBody, responseStatus, responseStatusPhrase,
                    Duration.between(requestTime, LocalDateTime.now()).toMillis(), responseHeaders, responseBody);

            if (isError) {
                classLogger.error(logMessage);
            } else {
                classLogger.info(logMessage);
            }

            // response 복사
            if (httpServletRequest.isAsyncStarted()) { // DeferredResult 처리
                httpServletRequest.getAsyncContext().addListener(new AsyncListener() {
                    @Override
                    public void onComplete(@Nullable AsyncEvent event) throws IOException {
                        httpServletResponse.copyBodyToResponse();
                    }

                    @Override
                    public void onTimeout(@Nullable AsyncEvent event) {
                    }

                    @Override
                    public void onError(@Nullable AsyncEvent event) {
                    }

                    @Override
                    public void onStartAsync(@Nullable AsyncEvent event) {
                    }
                });
            } else {
                httpServletResponse.copyBodyToResponse();
            }
        }
    }


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>
    private @NotNull String getContentByte(
            byte[] content,
            @NotNull String contentType
    ) {
        @NotNull MediaType mediaType = MediaType.valueOf(contentType);
        boolean visible = visibleTypeList.stream().anyMatch(visibleType -> visibleType.includes(mediaType));

        if (visible) {
            @NotNull String contentStr = "";

            @NotNull String contentStrTemp;
            try {
                contentStrTemp = new String(content, StandardCharsets.UTF_8);
            } catch (Exception e) {
                contentStrTemp = content.length + " bytes content";
            }
            contentStr += contentStrTemp;

            return contentStr;
        } else {
            return content.length + " bytes content";
        }
    }


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
}
