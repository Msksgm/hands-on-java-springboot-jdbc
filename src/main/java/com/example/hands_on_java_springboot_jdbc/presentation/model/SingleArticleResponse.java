package com.example.hands_on_java_springboot_jdbc.presentation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;

@Getter
public class SingleArticleResponse {
    @Valid
    @Schema(description = "記事の詳細", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "article", required = true)
    private Article article;

    public SingleArticleResponse(Article article) {
        this.article = article;
    }
}
