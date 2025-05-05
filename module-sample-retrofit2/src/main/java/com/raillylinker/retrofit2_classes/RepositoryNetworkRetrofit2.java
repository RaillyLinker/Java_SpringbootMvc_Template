package com.raillylinker.retrofit2_classes;

import com.google.gson.Gson;
import com.raillylinker.retrofit2_classes.request_apis.LocalHostRequestApi;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import org.jetbrains.annotations.*;

import java.util.concurrent.TimeUnit;

public class RepositoryNetworkRetrofit2 {
    // <멤버 변수 공간>
    public static final @NotNull RepositoryNetworkRetrofit2 instance = new RepositoryNetworkRetrofit2();

    // (Network Request Api 객체들)
    // !!!요청을 보낼 서버 위치 경로별 RequestApi 객체를 생성하기!!!

    // (로컬 테스트용)
    public final @NotNull LocalHostRequestApi localHostRequestApi =
            getRetrofitClient(
                    "http://localhost:12006",
                    7000L,
                    7000L,
                    7000L,
                    false
            ).create(LocalHostRequestApi.class);


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>
    private @NotNull Retrofit getRetrofitClient(
            @NotNull String baseUrl,
            @NotNull Long connectTimeOutMilliSecond,
            @NotNull Long readTimeOutMilliSecond,
            @NotNull Long writeTimeOutMilliSecond,
            @NotNull Boolean retryOnConnectionFailure
    ) {
        // 클라이언트 설정 객체
        @NotNull OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        // URL interceptor
        okHttpClientBuilder.addInterceptor(
                chain -> {
                    @NotNull Request originalRequest = chain.request();
                    @NotNull HttpUrl addedUrl = originalRequest.url().newBuilder().build();
                    @NotNull Request finalRequest = originalRequest.newBuilder()
                            .url(addedUrl)
                            .method(originalRequest.method(), originalRequest.body())
                            .build();
                    return chain.proceed(finalRequest);
                }
        );

        // 연결 설정
        okHttpClientBuilder.connectTimeout(connectTimeOutMilliSecond, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(readTimeOutMilliSecond, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.writeTimeout(writeTimeOutMilliSecond, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.retryOnConnectionFailure(retryOnConnectionFailure);

        // 로깅 인터셉터 설정
        @NotNull Logger logger = LoggerFactory.getLogger(RepositoryNetworkRetrofit2.class);
        @NotNull HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> logger.debug("[Retrofit2 Log] : {}", message));
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(interceptor);

        // 위 설정에 따른 retrofit 객체 생성 및 반환
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(okHttpClientBuilder.build())
                .build();
    }
}
