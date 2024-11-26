package com.example.hands_on_java_springboot_jdbc.domain;

import jakarta.validation.constraints.NotBlank;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;
import net.jqwik.api.ForAll;
import net.jqwik.api.From;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.StringLength;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BodyTest {
    static class NewTest {
        @Property
        void 正常系_Bodyを生成(@ForAll @From(supplier = BodyValidRange.class) String validBodyString) {
            /*
              given:
             */

            try {
                /*
                  when:
                 */
                Body body = Body.newBody(validBodyString);

                /*
                  then:
                 */
                Body expectedBody = Body.newWithoutValidation(validBodyString);
                assertEquals(expectedBody, body);
            } catch (Body.CreationErrorException e) {
                fail("例外が発生しました");
            }
        }

        @Property
        void 異常系_タイトルが空文字の場合バリデーションエラー() {
            /*
              given:
             */
            String body = "";

            try {
                /*
                  when:
                 */
                Body.newBody(body);
                fail("例外が発生しませんでした");
            } catch (Body.EmptyBodyError actual) {
                /*
                  then:
                 */
                Body.EmptyBodyError expected = new Body.EmptyBodyError();
                assertEquals(expected, actual);
            } catch (Body.CreationErrorException e) {
                fail("想定外の例外が発生しました");
            }
        }

        @Property
        void 異常系_タイトルがnullの場合バリデーションエラー() {
            /*
              given:
             */
            String body = null;

            try {
                /*
                  when:
                 */
                Body.newBody(body);
                fail("例外が発生しませんでした");
            } catch (Body.EmptyBodyError actual) {
                /*
                  then:
                 */
                Body.EmptyBodyError expected = new Body.EmptyBodyError();
                assertEquals(expected, actual);
            } catch (Body.CreationErrorException e) {
                fail("想定外の例外が発生しました");
            }
        }

        @Property
        void 異常系_Bodyが長すぎる場合バリデーションエラー(
            @ForAll @NotBlank @StringLength(min = 1025) String tooLongString
        ) {
            /*
                given:
             */

            try {
                /*
                    when:
                 */
                Body.newBody(tooLongString);
                fail("例外が発生しませんでした");
            } catch (Body.TooLongBodyFormatError actual) {
                /*
                    then:
                 */
                Body.TooLongBodyFormatError expected = new Body.TooLongBodyFormatError(1024);
                assertEquals(expected, actual);
            } catch (Body.CreationErrorException e) {
                fail("想定外の例外が発生しました");
            }

        }
    }

    static class BodyValidRange implements ArbitrarySupplier<String> {
        @Override
        public Arbitrary<String> get() {
            return Arbitraries.strings().ofMinLength(1).ofMaxLength(1024).filter(s -> !s.isBlank());
        }
    }
}
