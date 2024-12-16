package com.example.hands_on_java_springboot_jdbc.integration.helper;

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;

@Component
public class CustomizedMockMvc implements MockMvcBuilderCustomizer {
    @Override
    public void customize(ConfigurableMockMvcBuilder<?> builder) {
        if (builder != null) {
            builder.alwaysDo(result -> result.getResponse().setCharacterEncoding("UTF-8"));
        }
    }
}
