package com.example.hands_on_java_springboot_jdbc.domain;

import com.example.hands_on_java_springboot_jdbc.util.ValidationErrorException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class Title {
    private final String value;

    /**
     * コンストラクタ
     * private にして外部からのインスタンス生成を禁止
     * ファクトリメソッドを経由して、インスタンスを生成する
     *
     * @param value タイトル
     */
    private Title(String value) {
        this.value = value;
    }

    /**
     * バリデーションありでタイトルを生成するファクトリメソッド
     * ユースケースから呼び出し、不正な値の場合は例外をスローする
     *
     * @param title タイトル
     * @throws CreationErrorException タイトル生成時の例外
     */
    static public Title newTitle(String title) throws CreationErrorException  {
        final int maxTitleLength = 32;

        if (title == null || title.isEmpty()) {
            throw new EmptyTitleError();
        }

        if (title.length() > maxTitleLength) {
            throw new TooLongError(title);
        }

        return new Title(title);
    }

    /**
     * バリデーションなしでタイトルを生成するファクトリメソッド
     * DB からのデータ生成やテスト用に使用
     * ユースケースから呼び出さないために、ArchUnit で newWithoutValidation が呼ばれていないことを検証
     *
     * @param title タイトル
     */
    static public Title newWithoutValidation(String title) {
        return new Title(title);
    }

    /**
     * タイトル生成時のドメイン例外
     * sealed abstract class によって、生成時の例外を限定する
     */
    public static sealed abstract class CreationErrorException extends ValidationErrorException permits EmptyTitleError, TooLongError {

        public CreationErrorException(String message) {
            super(message);
        }
    }

    /**
     * タイトルは必須のドメインエラー
     */
    public static final class EmptyTitleError extends CreationErrorException {
        public EmptyTitleError() {
            super("タイトルは必須です");
        }
    }

    /**
     * タイトルの文字数のドメインエラー
     */
    @Getter
    public static final class TooLongError extends CreationErrorException {
        private final String title;

        public TooLongError(String title) {
            super(String.format("タイトルが長すぎます: %s", title));
            this.title = title;
        }
    }
}
