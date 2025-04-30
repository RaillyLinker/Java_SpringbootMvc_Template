package com.raillylinker.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.function.Consumer;

// [Swagger API 문서 설정]
@Configuration
public class SwaggerConfig {
    public SwaggerConfig(
            // (버전 정보)
            @Value("${custom-config.swagger.document-version}")
            String documentVersion,

            // (문서 제목)
            @Value("${custom-config.swagger.document-title}")
            String documentTitle,

            // (문서 설명)
            @Value("${custom-config.swagger.document-description}")
            String documentDescription
    ) {
        this.documentVersion = documentVersion;
        this.documentTitle = documentTitle;
        this.documentDescription = documentDescription;
    }

    // <멤버 변수 공간>
    private final String documentVersion;
    private final String documentTitle;
    private final String documentDescription;


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // 공개 메소드
    @Bean
    public OpenAPI openAPI() {
        Components component =
                new Components().addSecuritySchemes(
                        "JWT",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name(HttpHeaders.AUTHORIZATION)
                );

        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("JWT");

        Info documentInfo = new Info()
                .title(documentTitle)
                .version(documentVersion)
                .description(documentDescription);

        return new OpenAPI()
                .components(component)
                .addSecurityItem(securityRequirement)
                .info(documentInfo);
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        Consumer<Operation> pathItemConsumer =
                operation -> {
                    operation.getResponses()
                            .addApiResponse(
                                    "400",
                                    new ApiResponse()
                                            .description(
                                                    "클라이언트에서 전달한 Request 변수의 형식이 잘못되었습니다.<br>" +
                                                            "입력 데이터를 다시 한번 확인해주세요"
                                            )
                            )
                            .addApiResponse(
                                    "500",
                                    new ApiResponse()
                                            .description(
                                                    "서버에서 런타임 에러가 발생하였습니다.<br>" +
                                                            "서버 개발자에게 에러 상황, 에러 로그 등의 정보를 알려주세요."
                                            )
                            );
                };

        return openApi ->
                openApi.getPaths()
                        .values()
                        .forEach(
                                pathItem ->
                                        pathItem.readOperations()
                                                .forEach(pathItemConsumer)
                        );
    }
}
