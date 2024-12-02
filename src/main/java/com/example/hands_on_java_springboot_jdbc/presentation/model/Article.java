package com.example.hands_on_java_springboot_jdbc.presentation.model;

import com.example.hands_on_java_springboot_jdbc.domain.Body;
import com.example.hands_on_java_springboot_jdbc.domain.Slug;
import com.example.hands_on_java_springboot_jdbc.domain.Title;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class Article {
    @Schema(description = "slug", example = "283e60096c26aa3a39cf04712cdd1ff7", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "slug", required = true)
    private String slug;

    @Schema(description = "記事のタイトル", example = "タイトル", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "title", required = true)
    private String title;

    @Schema(description = "記事の説明", example = "内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(value = "description", required = true)
    private String content;

    public Article(Slug slug, Title title, Body body) {
    }
}
