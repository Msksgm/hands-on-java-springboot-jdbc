package com.example.hands_on_java_springboot_jdbc.integration;

import com.example.hands_on_java_springboot_jdbc.integration.helper.DbConnection;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

// NOTE: IntegrationTest クラスをヘルパーに作成して、mockMvc の DI、DbConnection.resetSequence は共通かしても良さそう
public class ArticleTest {
    @Nested
    @SpringBootTest
    @AutoConfigureMockMvc
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DBRider
    class GetArticle {
        @Autowired
        private MockMvc mockMvc;

        @BeforeEach
        void reset() {
            DbConnection.resetSequence();
        }

        @Test
        @DataSet(value = "datasets/yml/given/articles.yml")
        void 正常系_slugに該当する作成済記事を取得する() throws Exception {
            /*
             * given:
             */
            final String slug = "slug0000000000000000000000000001";
            // NOTE: 日付や外部 API のモックが必要なときには、ここに MockitoBean でモックを作成する。その際には必ず呼び出し回数のチェックを行うこと

            /*
             * when:
             */
            MockHttpServletResponse response = mockMvc.perform(get("/articles/{slug}", slug)).andReturn().getResponse();
            int actualStatus = response.getStatus();
            String actualResponseBody = response.getContentAsString();

            /*
             * then:
             * - ステータスコードが一致する
             * - レスポンスボディが一致する
             */
            int expectedStatus = HttpStatus.OK.value();
            String expectedResponseBody = """
                {
                  "article": {
                    "slug": "slug0000000000000000000000000001",
                    "title": "dummy-title-01",
                    "body": "dummy-body-01"
                  }
                }
            """;
            System.out.println(actualResponseBody);
            Assertions.assertThat(actualStatus).isEqualTo(expectedStatus);
            JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, true);
        }
    }
}
