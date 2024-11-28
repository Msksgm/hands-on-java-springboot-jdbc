package com.example.hands_on_java_springboot_jdbc.presentation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * エラーモデル
 * エラーレスポンスの詳細を List<String> 型で記述する
 */
@Getter
@EqualsAndHashCode
public class GenericErrorModelErrors {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "")
    @JsonProperty(value = "body", required = true)
    private List<String> body;

    public GenericErrorModelErrors(List<String> body) {
        this.body = body;
    }
}
