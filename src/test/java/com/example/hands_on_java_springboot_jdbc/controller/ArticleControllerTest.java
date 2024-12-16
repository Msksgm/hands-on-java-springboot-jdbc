package com.example.hands_on_java_springboot_jdbc.controller;

import com.example.hands_on_java_springboot_jdbc.presentation.ArticleController;
import com.example.hands_on_java_springboot_jdbc.usecase.ShowArticleUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// アノテーションによるバリデーションエラーを発生させるには、Spring Boot のテストフレームワークを利用必要がある。
// やり方はいろいろあるけど、Http リクエスト -> バリデーションエラー -> レスポンス の流れを再現するために、@WebMvcTest が使う
// @SpringBootTest でも代替可能だけど、@Repository アノテーションまで動くことになって、DB との接続が必要になりそなので、@WebMvcTest を選択
// TODO バリデーションエラーは PBT でテストしたいけど、実装の仕方がわからない。。。
@WebMvcTest(controllers = ArticleController.class)
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShowArticleUseCase showArticleUseCase;

    @Test
    void 異常系_単一記事取得API_Slugが32文字でない() throws Exception {
        /*
            given:
        */
        String invalidSlug = "invalid-slug";
        when(showArticleUseCase.execute(invalidSlug)).thenThrow(new RuntimeException("バリデーションエラーが発生しないはずの例外"));

        /*
            when:
        */
        MockHttpServletResponse response = mockMvc.perform(get("/articles/{slug}", invalidSlug)).andReturn().getResponse();
        int actualStatus = response.getStatus();
        String actualResponseBody = response.getContentAsString();

        /*
            then:
            - ステータスコードとレスポンスが正しいことを確認
            - Mock が呼び出されていないことを確認
        */
        int expectedStatus = HttpStatus.BAD_REQUEST.value();
        String expectedResponseBody =
                """
                {
                    "errors": {
                        "body": ["slugは32文字以上32文字以下にしてください"]
                    }
                 }
            """;
        Assertions.assertThat(actualStatus).isEqualTo(expectedStatus);
        JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, true);

        verify(showArticleUseCase, times(0)).execute(invalidSlug);
    }
}
