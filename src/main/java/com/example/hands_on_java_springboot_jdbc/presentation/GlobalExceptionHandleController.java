package com.example.hands_on_java_springboot_jdbc.presentation;

import com.example.hands_on_java_springboot_jdbc.presentation.model.GenericErrorModel;
import com.example.hands_on_java_springboot_jdbc.presentation.model.GenericErrorModelErrors;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * グローバルに例外ハンドリングし、エラーレスポンスを実現するコントローラ
 *
 * 予期しない例外が発生した際に以下を実施する。
 * - 秘密情報をレスポンスに含めないためのエラーハンドリング
 * - 原因調査のためのログ出力
 */
@RestControllerAdvice
public class GlobalExceptionHandleController {

    /**
     * 存在しないエンドポイントにリクエストされた時のエラーレスポンスを作成するメソッド
     *
     * @param e NoResourceFoundException
     * @return 404 エラーのレスポンス
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<GenericErrorModel> noResourceFoundExceptionHandler(NoResourceFoundException e) {
        GenericErrorModel error = new GenericErrorModel(
                new GenericErrorModelErrors(List.of("該当するエンドポイントがありませんでした"))
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * 許可されていないメソッドでリクエストを送った時のエラーレスポンスを作成するメソッド
     *
     * @param e HttpRequestMethodNotSupportedException
     * @return 405 エラーのレスポンス
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<GenericErrorModel> noHttpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        GenericErrorModel error = new GenericErrorModel(
                new GenericErrorModelErrors(List.of("該当エンドポイントで" + e.getMethod() + "メソッドの処理は許可されていません"))
        );
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * エンドポイントが想定していない Content-Type でリクエストされた時にエラーレスポンスを作成するメソッド
     *
     * @param e HttpMediaTypeNotSupportedException
     * @return 415 エラーのレスポンス
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<GenericErrorModel> noHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        GenericErrorModel error = new GenericErrorModel(
                new GenericErrorModelErrors(List.of("該当エンドポイントで" + e.getContentType() + "のリクエストはサポートされていません"))
        );
        return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * API スキーマが想定していないリクエストだった場合に発生させるエラーレスポンスを作成するメソッド
     *
     * @param e HttpMessageNotReadableException
     * @return 400 エラーのレスポンス
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericErrorModel> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        GenericErrorModel error = new GenericErrorModel(
                new GenericErrorModelErrors(List.of("エンドポイントが想定していない形式または型のリクエストが送られました"))
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // MethodValidationException が削除されていたのでコメントアウト。代替手段を検討
    /**
     * リクエストのバリデーションエラーが発生した場合に発生させるエラーレスポンスを作成するメソッド
     *
     * @param e MethodArgumentNotValidException
     * @return 400 エラーのレスポンス
     */
    @ExceptionHandler(MethodValidationException.class)
    public ResponseEntity<GenericErrorModel> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<String> messages = e.getBindingResult().getAllErrors().stream()
                .map(error -> error instanceof FieldError
                        ? ((FieldError) error).getField() + "は" + error.getDefaultMessage()
                        : error.getDefaultMessage())
                .collect(Collectors.toList());
        GenericErrorModel error = new GenericErrorModel(
                new GenericErrorModelErrors(messages)
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * パスパラメータまたはクエリパラメータのバリデーションエラーが発生した場合に発生させるエラーレスポンスを作成するメソッド
     *
     * @param e ConstraintViolationException
     * @return 400 エラーのレスポンス
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericErrorModel> constraintViolationExceptionExceptionHandler(ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations().stream()
                .map(violation -> {
                    String chainCaseRequestParam = violation.getPropertyPath().toString()
                            .split("\\.")[1]
                            .replaceAll("([A-Z])", "-$1").toLowerCase(Locale.getDefault());
                    return chainCaseRequestParam + "は" + violation.getMessage();
                })
                .collect(Collectors.toList());
        GenericErrorModel error = new GenericErrorModel(
                new GenericErrorModelErrors(messages)
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
