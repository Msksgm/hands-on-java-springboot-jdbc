package com.example.hands_on_java_springboot_jdbc.domain;

import jakarta.validation.constraints.NotBlank;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;
import net.jqwik.api.ForAll;
import net.jqwik.api.From;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.StringLength;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TitleTest {
    static class NewTest {
        @Property
        void 正常系_Titleを生成(@ForAll @From(supplier = TitleValidRange.class) String validString) {
            /*
              given:
             */

            try {
                /*
                  when:
                 */
                Title title = Title.newTitle(validString);

                /*
                  then:
                */
                Title expectedTitle = Title.newWithoutValidation(validString);
                assertEquals(expectedTitle, title);
            } catch (Title.CreationErrorException e) {
                fail("例外が発生しました");
            }
        }

        @Property
        void 異常系_タイトルが空文字の場合バリデーションエラー() {
            /*
              given:
             */
            String title = "";

            try {
                /*
                  when:
                 */
                Title.newTitle(title);
                fail("例外が発生しませんでした");
            } catch (Title.EmptyTitleError actual) {
                /*
                  then:
                 */
                Title.EmptyTitleError expected = new Title.EmptyTitleError();
                assertEquals(expected, actual);
            } catch (Title.CreationErrorException e) {
                fail("想定外の例外が発生しました");
            }
        }

        @Property
        void 異常系_タイトルがnullの場合バリデーションエラー() {
            /*
              given:
             */
            String title = null;

            /*
              then:
            */
            try {
                /*
                  when:
                 */
                Title.newTitle(title);
                fail("例外が発生しませんでした");
            } catch (Title.EmptyTitleError actual) {
                /*
                  then:
                 */
                Title.EmptyTitleError expected = new Title.EmptyTitleError();
                assertEquals(expected, actual);
            } catch (Title.CreationErrorException e) {
                fail("想定外の例外が発生しました");
            }
        }

        @Property
        void 異常系_タイトルが長すぎる場合バリデーションエラー(
            @ForAll @NotBlank @StringLength(min = 33) String tooLongString
        ) {
            /*
              given:
             */

            try {
                /*
                  when:
                 */
                Title.newTitle(tooLongString);
                fail("例外が発生しませんでした");
            } catch (Title.TooLongError actual) {
                /*
                  then:
                 */
                Title.TooLongError expected = new Title.TooLongError(tooLongString);
                assertEquals(expected, actual);
            } catch (Title.CreationErrorException e) {
                fail("想定外の例外が発生しました");
            }
        }
    }

    static class TitleValidRange implements ArbitrarySupplier<String> {
        @Override
        public Arbitrary<String> get() {
            return Arbitraries.strings().ofMinLength(1).ofMaxLength(32).filter(s -> !s.isBlank());
        }
    }
}

