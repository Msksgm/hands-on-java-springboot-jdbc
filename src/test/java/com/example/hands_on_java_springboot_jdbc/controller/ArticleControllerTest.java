package com.example.hands_on_java_springboot_jdbc.controller;

import com.example.hands_on_java_springboot_jdbc.presentation.ArticleController;
import com.example.hands_on_java_springboot_jdbc.usecase.ShowArticleUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShowArticleUseCase showArticleUseCase;

    @Test
    void shouldReturnBadRequestWhenSlugIsInvalid() throws Exception {
        /*
            given:
        */
        String invalidSlug = "short-slug";
        Mockito.when(showArticleUseCase.execute(invalidSlug)).thenThrow(new ShowArticleUseCase.CreatedArticleValidationErrors(null, "バリデーションエラー 理由 xx"));

        /*
            when:
        */
        MockHttpServletResponse response = mockMvc.perform(get("/articles/{slug}", invalidSlug)).andReturn().getResponse();
        int actualStatus = response.getStatus();
        String actualResponseBody = response.getContentAsString();

        /*
            then:
        */
        int expectedStatus = HttpStatus.BAD_REQUEST.value();
        String expectedResponseBody =
                """
                 {
                "errors": {
                         "body": ["バリデーションエラー 理由 xx"]
                       }
                     }
                """;
        Assertions.assertThat(actualStatus).isEqualTo(expectedStatus);
        JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, true);
    }
}
