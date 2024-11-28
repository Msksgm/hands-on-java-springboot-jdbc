package com.example.hands_on_java_springboot_jdbc.presentation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * エラーモデル
 * エラーの内容レスポンスモデル
 */
@Getter
@EqualsAndHashCode
public final class GenericErrorModel {

    @Valid
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "")
    @JsonProperty(value = "errors", required = true)
    private final GenericErrorModelErrors errors;

    public GenericErrorModel(GenericErrorModelErrors errors) {
        this.errors = errors;
    }
}
