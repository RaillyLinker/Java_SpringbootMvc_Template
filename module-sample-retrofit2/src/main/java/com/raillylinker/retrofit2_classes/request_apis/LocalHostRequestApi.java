package com.raillylinker.retrofit2_classes.request_apis;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.*;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

// (한 주소에 대한 API 요청명세)
// 사용법은 아래 기본 사용 샘플을 참고하여 추상함수를 작성하여 사용
public interface LocalHostRequestApi {
    // [기본 요청 테스트 API]
    // 이 API 를 요청하면 현재 실행중인 프로필 이름을 반환합니다.
    // (api-result-code)
    @GET("/api-test")
    @NotNull
    Call<String> getMyServiceTkSampleRequestTest();


    // ----
    // [요청 Redirect 테스트 API]
    // 이 API 를 요청하면 /api-test 로 Redirect 됩니다.
    // (api-result-code)
    @GET("/api-test/redirect-to-blank")
    @NotNull
    Call<String> getMyServiceTkSampleRequestTestRedirectToBlank();


    // ----
    // [요청 Forward 테스트 API]
    // 이 API 를 요청하면 /api-test 로 Forward 됩니다.
    // (api-result-code)
    @GET("/api-test/forward-to-blank")
    @NotNull
    Call<String> getMyServiceTkSampleRequestTestForwardToBlank();


    // ----
    // [Get 요청(Query Parameter) 테스트 API]
    // Query Parameter 를 받는 Get 메소드 요청 테스트
    // (api-result-code)
    @GET("/api-test/get-request")
    @NotNull
    Call<GetMyServiceTkSampleRequestTestGetRequestOutputVO> getMyServiceTkSampleRequestTestGetRequest(
            @Query("queryParamString")
            @NotNull String queryParamString,
            @Query("queryParamStringNullable")
            @Nullable String queryParamStringNullable,
            @Query("queryParamInt")
            @NotNull Integer queryParamInt,
            @Query("queryParamIntNullable")
            @Nullable Integer queryParamIntNullable,
            @Query("queryParamDouble")
            @NotNull Double queryParamDouble,
            @Query("queryParamDoubleNullable")
            @Nullable Double queryParamDoubleNullable,
            @Query("queryParamBoolean")
            @NotNull Boolean queryParamBoolean,
            @Query("queryParamBooleanNullable")
            @Nullable Boolean queryParamBooleanNullable,
            @Query("queryParamStringList")
            @NotNull List<String> queryParamStringList,
            @Query("queryParamStringListNullable")
            @Nullable List<String> queryParamStringListNullable
    );

    record GetMyServiceTkSampleRequestTestGetRequestOutputVO(
            @SerializedName("queryParamString")
            @Expose
            @NotNull String queryParamString,
            @SerializedName("queryParamStringNullable")
            @Expose
            @Nullable String queryParamStringNullable,
            @SerializedName("queryParamInt")
            @Expose
            @NotNull Integer queryParamInt,
            @SerializedName("queryParamIntNullable")
            @Expose
            @Nullable Integer queryParamIntNullable,
            @SerializedName("queryParamDouble")
            @Expose
            @NotNull Double queryParamDouble,
            @SerializedName("queryParamDoubleNullable")
            @Expose
            @Nullable Double queryParamDoubleNullable,
            @SerializedName("queryParamBoolean")
            @Expose
            @NotNull Boolean queryParamBoolean,
            @SerializedName("queryParamBooleanNullable")
            @Expose
            @Nullable Boolean queryParamBooleanNullable,
            @SerializedName("queryParamStringList")
            @Expose
            @NotNull List<String> queryParamStringList,
            @SerializedName("queryParamStringListNullable")
            @Expose
            @Nullable List<String> queryParamStringListNullable
    ) {
    }


    // ----
    // [Get 요청(Path Parameter) 테스트 API]
    // Path Parameter 를 받는 Get 메소드 요청 테스트
    // (api-result-code)
    @GET("/api-test/get-request/{pathParamInt}")
    @NotNull
    Call<GetMyServiceTkSampleRequestTestGetRequestPathParamIntOutputVO> getMyServiceTkSampleRequestTestGetRequestPathParamInt(
            @Path("pathParamInt")
            @NotNull Integer pathParamInt
    );

    record GetMyServiceTkSampleRequestTestGetRequestPathParamIntOutputVO(
            @SerializedName("pathParamInt")
            @Expose
            @NotNull Integer pathParamInt
    ) {
    }


    // ----
    // [Post 요청(Application-Json) 테스트 API]
    // application-json 형태의 Request Body 를 받는 Post 메소드 요청 테스트
    // (api-result-code)
    @POST("/api-test/post-request-application-json")
    @NotNull
    Call<PostMyServiceTkSampleRequestTestPostRequestApplicationJsonOutputVO> postMyServiceTkSampleRequestTestPostRequestApplicationJson(
            @Body
            @NotNull PostMyServiceTkSampleRequestTestPostRequestApplicationJsonInputVO inputVo
    );

    record PostMyServiceTkSampleRequestTestPostRequestApplicationJsonInputVO(
            @SerializedName("requestBodyString")
            @Expose
            @NotNull String requestBodyString,
            @SerializedName("requestBodyStringNullable")
            @Expose
            @Nullable String requestBodyStringNullable,
            @SerializedName("requestBodyInt")
            @Expose
            @NotNull Integer requestBodyInt,
            @SerializedName("requestBodyIntNullable")
            @Expose
            @Nullable Integer requestBodyIntNullable,
            @SerializedName("requestBodyDouble")
            @Expose
            @NotNull Double requestBodyDouble,
            @SerializedName("requestBodyDoubleNullable")
            @Expose
            @Nullable Double requestBodyDoubleNullable,
            @SerializedName("requestBodyBoolean")
            @Expose
            @NotNull Boolean requestBodyBoolean,
            @SerializedName("requestBodyBooleanNullable")
            @Expose
            @Nullable Boolean requestBodyBooleanNullable,
            @SerializedName("requestBodyStringList")
            @Expose
            @NotNull List<String> requestBodyStringList,
            @SerializedName("requestBodyStringListNullable")
            @Expose
            @Nullable List<String> requestBodyStringListNullable
    ) {
    }

    record PostMyServiceTkSampleRequestTestPostRequestApplicationJsonOutputVO(
            @SerializedName("requestBodyString")
            @Expose
            @NotNull String requestBodyString,
            @SerializedName("requestBodyStringNullable")
            @Expose
            @Nullable String requestBodyStringNullable,
            @SerializedName("requestBodyInt")
            @Expose
            @NotNull Integer requestBodyInt,
            @SerializedName("requestBodyIntNullable")
            @Expose
            @Nullable Integer requestBodyIntNullable,
            @SerializedName("requestBodyDouble")
            @Expose
            @NotNull Double requestBodyDouble,
            @SerializedName("requestBodyDoubleNullable")
            @Expose
            @Nullable Double requestBodyDoubleNullable,
            @SerializedName("requestBodyBoolean")
            @Expose
            @NotNull Boolean requestBodyBoolean,
            @SerializedName("requestBodyBooleanNullable")
            @Expose
            @Nullable Boolean requestBodyBooleanNullable,
            @SerializedName("requestBodyStringList")
            @Expose
            @NotNull List<String> requestBodyStringList,
            @SerializedName("requestBodyStringListNullable")
            @Expose
            @Nullable List<String> requestBodyStringListNullable
    ) {
    }


    // ----
    // [Post 요청(x-www-form-urlencoded) 테스트 API]
    // x-www-form-urlencoded 형태의 Request Body 를 받는 Post 메소드 요청 테스트
    // (api-result-code)
    @POST("/api-test/post-request-x-www-form-urlencoded")
    @FormUrlEncoded
    @NotNull
    Call<PostMyServiceTkSampleRequestTestPostRequestXWwwFormUrlencodedOutputVO> postMyServiceTkSampleRequestTestPostRequestXWwwFormUrlencoded(
            @Field("requestFormString")
            @NotNull String requestFormString,
            @Field("requestFormStringNullable")
            @Nullable String requestFormStringNullable,
            @Field("requestFormInt")
            @NotNull Integer requestFormInt,
            @Field("requestFormIntNullable")
            @Nullable Integer requestFormIntNullable,
            @Field("requestFormDouble")
            @NotNull Double requestFormDouble,
            @Field("requestFormDoubleNullable")
            @Nullable Double requestFormDoubleNullable,
            @Field("requestFormBoolean")
            @NotNull Boolean requestFormBoolean,
            @Field("requestFormBooleanNullable")
            @Nullable Boolean requestFormBooleanNullable,
            @Field("requestFormStringList")
            @NotNull List<String> requestFormStringList,
            @Field("requestFormStringListNullable")
            @Nullable List<String> requestFormStringListNullable
    );

    record PostMyServiceTkSampleRequestTestPostRequestXWwwFormUrlencodedOutputVO(
            @SerializedName("requestFormString")
            @Expose
            @NotNull String requestFormString,
            @SerializedName("requestFormStringNullable")
            @Expose
            @Nullable String requestFormStringNullable,
            @SerializedName("requestFormInt")
            @Expose
            @NotNull Integer requestFormInt,
            @SerializedName("requestFormIntNullable")
            @Expose
            @Nullable Integer requestFormIntNullable,
            @SerializedName("requestFormDouble")
            @Expose
            @NotNull Double requestFormDouble,
            @SerializedName("requestFormDoubleNullable")
            @Expose
            @Nullable Double requestFormDoubleNullable,
            @SerializedName("requestFormBoolean")
            @Expose
            @NotNull Boolean requestFormBoolean,
            @SerializedName("requestFormBooleanNullable")
            @Expose
            @Nullable Boolean requestFormBooleanNullable,
            @SerializedName("requestFormStringList")
            @Expose
            @NotNull List<String> requestFormStringList,
            @SerializedName("requestFormStringListNullable")
            @Expose
            @Nullable List<String> requestFormStringListNullable
    ) {
    }


    // ----
    // [Post 요청(multipart/form-data) 테스트 API]
    // multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트(Multipart File List)
    // MultipartFile 파라미터가 null 이 아니라면 저장
    // (api-result-code)
    @POST("/api-test/post-request-multipart-form-data")
    @Multipart
    @NotNull
    Call<PostMyServiceTkSampleRequestTestPostRequestMultipartFormDataOutputVO> postMyServiceTkSampleRequestTestPostRequestMultipartFormData(
            @Part
            @NotNull MultipartBody.Part requestFormString,
            @Part
            @Nullable MultipartBody.Part requestFormStringNullable,
            @Part
            @NotNull MultipartBody.Part requestFormInt,
            @Part
            @Nullable MultipartBody.Part requestFormIntNullable,
            @Part
            @NotNull MultipartBody.Part requestFormDouble,
            @Part
            @Nullable MultipartBody.Part requestFormDoubleNullable,
            @Part
            @NotNull MultipartBody.Part requestFormBoolean,
            @Part
            @Nullable MultipartBody.Part requestFormBooleanNullable,
            @Part
            @NotNull List<MultipartBody.Part> requestFormStringList,
            @Part
            @Nullable List<MultipartBody.Part> requestFormStringListNullable,
            @Part
            @NotNull MultipartBody.Part multipartFile,
            @Part
            @Nullable MultipartBody.Part multipartFileNullable
    );

    record PostMyServiceTkSampleRequestTestPostRequestMultipartFormDataOutputVO(
            @SerializedName("requestFormString")
            @Expose
            @NotNull String requestFormString,
            @SerializedName("requestFormStringNullable")
            @Expose
            @Nullable String requestFormStringNullable,
            @SerializedName("requestFormInt")
            @Expose
            @NotNull Integer requestFormInt,
            @SerializedName("requestFormIntNullable")
            @Expose
            @Nullable Integer requestFormIntNullable,
            @SerializedName("requestFormDouble")
            @Expose
            @NotNull Double requestFormDouble,
            @SerializedName("requestFormDoubleNullable")
            @Expose
            @Nullable Double requestFormDoubleNullable,
            @SerializedName("requestFormBoolean")
            @Expose
            @NotNull Boolean requestFormBoolean,
            @SerializedName("requestFormBooleanNullable")
            @Expose
            @Nullable Boolean requestFormBooleanNullable,
            @SerializedName("requestFormStringList")
            @Expose
            @NotNull List<String> requestFormStringList,
            @SerializedName("requestFormStringListNullable")
            @Expose
            @Nullable List<String> requestFormStringListNullable
    ) {
    }


    // ----
    // [Post 요청(multipart/form-data list) 테스트 API]
    // multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트(Multipart File List)
    // 파일 리스트가 null 이 아니라면 저장
    // (api-result-code)
    @POST("/api-test/post-request-multipart-form-data2")
    @Multipart
    @NotNull
    Call<PostMyServiceTkSampleRequestTestPostRequestMultipartFormData2VO> postMyServiceTkSampleRequestTestPostRequestMultipartFormData2(
            @Part
            @NotNull MultipartBody.Part requestFormString,
            @Part
            @Nullable MultipartBody.Part requestFormStringNullable,
            @Part
            @NotNull MultipartBody.Part requestFormInt,
            @Part
            @Nullable MultipartBody.Part requestFormIntNullable,
            @Part
            @NotNull MultipartBody.Part requestFormDouble,
            @Part
            @Nullable MultipartBody.Part requestFormDoubleNullable,
            @Part
            @NotNull MultipartBody.Part requestFormBoolean,
            @Part
            @Nullable MultipartBody.Part requestFormBooleanNullable,
            @Part
            @NotNull List<MultipartBody.Part> requestFormStringList,
            @Part
            @Nullable List<MultipartBody.Part> requestFormStringListNullable,
            @Part
            @NotNull List<MultipartBody.Part> multipartFileList,
            @Part
            @Nullable List<MultipartBody.Part> multipartFileNullableList
    );

    record PostMyServiceTkSampleRequestTestPostRequestMultipartFormData2VO(
            @SerializedName("requestFormString")
            @Expose
            @NotNull String requestFormString,
            @SerializedName("requestFormStringNullable")
            @Expose
            @Nullable String requestFormStringNullable,
            @SerializedName("requestFormInt")
            @Expose
            @NotNull Integer requestFormInt,
            @SerializedName("requestFormIntNullable")
            @Expose
            @Nullable Integer requestFormIntNullable,
            @SerializedName("requestFormDouble")
            @Expose
            @NotNull Double requestFormDouble,
            @SerializedName("requestFormDoubleNullable")
            @Expose
            @Nullable Double requestFormDoubleNullable,
            @SerializedName("requestFormBoolean")
            @Expose
            @NotNull Boolean requestFormBoolean,
            @SerializedName("requestFormBooleanNullable")
            @Expose
            @Nullable Boolean requestFormBooleanNullable,
            @SerializedName("requestFormStringList")
            @Expose
            @NotNull List<String> requestFormStringList,
            @SerializedName("requestFormStringListNullable")
            @Expose
            @Nullable List<String> requestFormStringListNullable
    ) {
    }


    // ----
    // [Post 요청(multipart/form-data list) 테스트 API]
    // multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트(Multipart File List)
    // 파일 리스트가 null 이 아니라면 저장
    // (api-result-code)
    @POST("/api-test/post-request-multipart-form-data-json")
    @Multipart
    @NotNull
    Call<PostMyServiceTkSampleRequestTestPostRequestMultipartFormDataJsonOutputVO> postMyServiceTkSampleRequestTestPostRequestMultipartFormDataJson(
            @Part
            @NotNull MultipartBody.Part jsonString,
            @Part
            @NotNull MultipartBody.Part multipartFile,
            @Part
            @Nullable MultipartBody.Part multipartFileNullable
    );

    record PostMyServiceTkSampleRequestTestPostRequestMultipartFormDataJsonJsonStringVo(
            @JsonProperty("requestFormString")
            @NotNull String requestFormString,
            @JsonProperty("requestFormStringNullable")
            @Nullable String requestFormStringNullable,
            @JsonProperty("requestFormInt")
            @NotNull Integer requestFormInt,
            @JsonProperty("requestFormIntNullable")
            @Nullable Integer requestFormIntNullable,
            @JsonProperty("requestFormDouble")
            @NotNull Double requestFormDouble,
            @JsonProperty("requestFormDoubleNullable")
            @Nullable Double requestFormDoubleNullable,
            @JsonProperty("requestFormBoolean")
            @NotNull Boolean requestFormBoolean,
            @JsonProperty("requestFormBooleanNullable")
            @Nullable Boolean requestFormBooleanNullable,
            @JsonProperty("requestFormStringList")
            @NotNull List<String> requestFormStringList,
            @JsonProperty("requestFormStringListNullable")
            @Nullable List<String> requestFormStringListNullable
    ) {
    }

    record PostMyServiceTkSampleRequestTestPostRequestMultipartFormDataJsonOutputVO(
            @SerializedName("requestFormString")
            @Expose
            @NotNull String requestFormString,
            @SerializedName("requestFormStringNullable")
            @Expose
            @Nullable String requestFormStringNullable,
            @SerializedName("requestFormInt")
            @Expose
            @NotNull Integer requestFormInt,
            @SerializedName("requestFormIntNullable")
            @Expose
            @Nullable Integer requestFormIntNullable,
            @SerializedName("requestFormDouble")
            @Expose
            @NotNull Double requestFormDouble,
            @SerializedName("requestFormDoubleNullable")
            @Expose
            @Nullable Double requestFormDoubleNullable,
            @SerializedName("requestFormBoolean")
            @Expose
            @NotNull Boolean requestFormBoolean,
            @SerializedName("requestFormBooleanNullable")
            @Expose
            @Nullable Boolean requestFormBooleanNullable,
            @SerializedName("requestFormStringList")
            @Expose
            @NotNull List<String> requestFormStringList,
            @SerializedName("requestFormStringListNullable")
            @Expose
            @Nullable List<String> requestFormStringListNullable
    ) {
    }


    // ----
    // [인위적 에러 발생 테스트 API]
    // 요청 받으면 인위적인 서버 에러를 발생시킵니다.(Http Response Status 500)
    // (api-result-code)
    @POST("/api-test/generate-error")
    @NotNull
    Call<Void> postMyServiceTkSampleRequestTestGenerateError();


    // ----
    // [결과 코드 발생 테스트 API]
    // Response Header 에 api-result-code 를 반환하는 테스트 API
    //(api-result-code)
    // 1 : errorType 을 A 로 보냈습니다.
    // 2 : errorType 을 B 로 보냈습니다.
    // 3 : errorType 을 C 로 보냈습니다.
    @POST("/api-test/api-result-code-test")
    @NotNull
    Call<Void> postMyServiceTkSampleRequestTestApiResultCodeTest(
            @Query("errorType")
            @NotNull PostMyServiceTkSampleRequestTestApiResultCodeTestErrorTypeEnum errorType
    );

    enum PostMyServiceTkSampleRequestTestApiResultCodeTestErrorTypeEnum {
        A,
        B,
        C
    }


    // ----
    // [인위적 타임아웃 에러 발생 테스트]
    // 타임아웃 에러를 발생시키기 위해 임의로 응답 시간을 지연시킵니다.
    // (api-result-code)
    @POST("/api-test/time-delay-test")
    @NotNull
    Call<Void> postMyServiceTkSampleRequestTestGenerateTimeOutError(
            @Query("delayTimeSec")
            @NotNull Long delayTimeSec
    );


    // ----
    // [text/string 반환 샘플]
    // text/string 형식의 Response Body 를 반환합니다.
    // (api-result-code)
    @GET("/api-test/return-text-string")
    @Headers("Content-Type: text/string")
    @NotNull
    Call<String> getMyServiceTkSampleRequestTestReturnTextString();


    // ----
    // [text/html 반환 샘플]
    // text/html 형식의 Response Body 를 반환합니다.
    // (api-result-code)
    @GET("/api-test/return-text-html")
    @Headers("Content-Type: text/html")
    @NotNull
    Call<String> getMyServiceTkSampleRequestTestReturnTextHtml();


    // ----
    // [비동기 처리 결과 반환 샘플]
    // API 호출시 함수 내에서 별도 스레드로 작업을 수행하고,
    // 비동기 작업 완료 후 그 처리 결과가 반환됨
    // (api-result-code)
    @GET("/api-test/async-result")
    @NotNull
    Call<GetMyServiceTkSampleRequestTestAsyncResultOutputVO> getMyServiceTkSampleRequestTestAsyncResult();

    record GetMyServiceTkSampleRequestTestAsyncResultOutputVO(
            @SerializedName("resultMessage")
            @Expose
            @NotNull String resultMessage
    ) {
    }
}