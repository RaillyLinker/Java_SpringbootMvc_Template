package com.raillylinker.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raillylinker.services.KafkaTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "/kafka/test APIs", description = "Kafka 테스트 API 컨트롤러")
@Controller
@RequestMapping("/kafka/test")
@Validated
public class KafkaTestController {
    public KafkaTestController(
            @NotNull KafkaTestService service
    ) {
        this.service = service;
    }

    // <멤버 변수 공간>
    private final @NotNull KafkaTestService service;


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
            summary = "Kafka 토픽 메세지 발행 테스트",
            description = "Kafka 토픽 메세지를 발행합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @PostMapping(
            path = {"/kafka-produce-test"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void sendKafkaTopicMessageTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @RequestBody
            @NotNull SendKafkaTopicMessageTestInputVo inputVo
    ) {
        service.sendKafkaTopicMessageTest(
                httpServletResponse,
                inputVo
        );
    }

    @Data
    public static class SendKafkaTopicMessageTestInputVo {
        @Schema(description = "메세지", requiredMode = Schema.RequiredMode.REQUIRED, example = "testMessage")
        @JsonProperty("message")
        private final @NotNull String message;
    }
}
