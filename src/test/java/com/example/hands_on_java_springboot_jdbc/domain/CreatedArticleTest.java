package com.example.hands_on_java_springboot_jdbc.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/*
ParameterizedTest の書き方は検討
 */
class CreatedArticleTest {

    static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(
                        "Slug が一致する場合、他のプロパティが異なっていても、true を戻す",
                        CreatedArticle.newWithoutValidation(
                                Slug.newWithoutValidation("dummy-slug"),
                                Title.newWithoutValidation("dummy-title"),
                                Body.newWithoutValidation("dummy-body")
                        ),
                        CreatedArticle.newWithoutValidation(
                                Slug.newWithoutValidation("dummy-slug"), // slug が同じ
                                Title.newWithoutValidation("other-dummy-title"),
                                Body.newWithoutValidation("other-dummy-body")
                        ),
                        true
                ),
                Arguments.of(
                        "Slug が一致しない場合、他のプロパティが一致していても、false を戻す",
                        CreatedArticle.newWithoutValidation(
                                Slug.newWithoutValidation("dummy-slug"),
                                Title.newWithoutValidation("dummy-title"),
                                Body.newWithoutValidation("dummy-body")
                        ),
                        CreatedArticle.newWithoutValidation(
                                Slug.newWithoutValidation("other-dummy-slug"), // slug が異なる
                                Title.newWithoutValidation("dummy-title"),
                                Body.newWithoutValidation("dummy-body")
                        ),
                        false
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCases")
    void articleEqualTest(
            String title,
            CreatedArticle createdArticle,
            CreatedArticle otherCreatedArticle,
            boolean expected
    ) {
        // When
        boolean actual = createdArticle.equals(otherCreatedArticle);

        // Then
        assertThat(actual).isEqualTo(expected);
    }
}
