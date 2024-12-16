package com.example.hands_on_java_springboot_jdbc.presentation;

import com.example.hands_on_java_springboot_jdbc.domain.CreatedArticle;
import com.example.hands_on_java_springboot_jdbc.presentation.model.Article;
import com.example.hands_on_java_springboot_jdbc.presentation.model.GenericErrorModel;
import com.example.hands_on_java_springboot_jdbc.presentation.model.GenericErrorModelErrors;
import com.example.hands_on_java_springboot_jdbc.presentation.model.SingleArticleResponse;
import com.example.hands_on_java_springboot_jdbc.usecase.ShowArticleUseCase;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class ArticleController {
    private final ShowArticleUseCase showArticleUseCase;

    public ArticleController(ShowArticleUseCase showArticleUseCase) {
        this.showArticleUseCase = showArticleUseCase;
    }

    @GetMapping(value = "/articles/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleArticleResponse> getArticle(
            @Parameter(description = "記事の slug", required = true)
            @Valid
            @PathVariable("slug")
            @Length(min = 32, max = 32)
            String slug
    ) throws ShowArticleUseCase.ShowArticleUseCaseException {
        final CreatedArticle createdArticle = this.showArticleUseCase.execute(slug);
        return ResponseEntity.ok(new SingleArticleResponse(
            new Article(
                createdArticle.getSlug().getValue(),
                createdArticle.getTitle().getValue(),
                createdArticle.getBody().getValue()
            )
        ));
    }

    // @ExceptionHandler でユースケースの予期する例外をキャッチして、異常系用のレスポンスに詰め替える。Spring Boot の異常系レスポンスの作り方の 1 つ。もう 1 つは @GlobalExceptionHandler を使う方法
    @ExceptionHandler(exception = ShowArticleUseCase.ShowArticleUseCaseException.class)
    public ResponseEntity<GenericErrorModel> handleShowArticleUseCaseException(ShowArticleUseCase.ShowArticleUseCaseException e) {
        // ここでユースケースの例外の種類によってレスポンスを変える。switch 式と sealed abstract class の組み合わせによって、例外の種類が増えてもコンパイルエラーになる
        return switch (e) {
            case ShowArticleUseCase.CreatedArticleValidationErrors ignored
                    -> new ResponseEntity<>(new GenericErrorModel(new GenericErrorModelErrors(List.of("バリデーションエラー 理由 xx"))), HttpStatus.BAD_REQUEST);
            case ShowArticleUseCase.NotFoundArticleBySlug ignored -> new ResponseEntity<>(new GenericErrorModel(new GenericErrorModelErrors(List.of("Slug に該当する記事がない slug 名 xxx"))), HttpStatus.BAD_REQUEST);
        };
    }
}
