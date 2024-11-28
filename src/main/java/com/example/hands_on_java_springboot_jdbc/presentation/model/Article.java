package com.example.hands_on_java_springboot_jdbc.presentation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class Article {
    @Schema(description = "slug", example = "283e60096c26aa3a39cf04712cdd1ff7", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "slug", required = true)
    private String slug;

    @Schema(description = "記事のタイトル", example = "タイトル", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "title", required = true)
    private String title;

    @Schema(description = "記事の内容", example = "内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "body", required = true)
    private String body;

    public Article(String slug, String title, String body) {
        this.slug = slug;
        this.title = title;
        this.body = body;
    }
}
