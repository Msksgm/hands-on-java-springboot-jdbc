package com.example.hands_on_java_springboot_jdbc.domain;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;
import net.jqwik.api.ForAll;
import net.jqwik.api.From;
import net.jqwik.api.Property;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlugTest {
    static class NewTest {
        @Property
        void 正常系_Slugを生成する(
            @ForAll @From(supplier = SlugValidRange.class) String validSlugValue
        ) {
            /*
                given:
             */
            try {
                /*
                    when:
                 */
                Slug slug = Slug.newSlug(validSlugValue);

                /*
                    then:
                 */
                Slug expectedSlug = Slug.newWithoutValidation(validSlugValue);
                assertEquals(expectedSlug, slug);
            } catch (Slug.CreationErrorException e) {
                fail("例外が発生しました。" + e.getMessage());
            }
        }

        @Test
        void 正常系_引数なしのnewで生成される文字列は32文字の英数字(){
            /*
                given:
             */

            /*
                when:
             */
            String actual = Slug.newSlug().getValue();

            /*
                then:
             */
            String expectedPattern = "^[a-z0-9]{32}$";
            assertThat(actual).matches(expectedPattern);
        }

        @Property
        void 異常系_文字列が有効でない場合バリデーションエラー(
            @ForAll @From(supplier = SlugInvalidRange.class) String invalidSlugValue
        ) {
            /*
                given:
             */

            try {
                /*
                    when:
                 */
                Slug.newSlug(invalidSlugValue);
                fail("例外が発生しませんでした");
            } catch (Slug.InvalidSlugFormatError actual) {
                /*
                    then:
                 */
                Slug.InvalidSlugFormatError expected = new Slug.InvalidSlugFormatError(invalidSlugValue);
                assertEquals(expected, actual);
            } catch (Slug.CreationErrorException e) {
                fail("想定外の例外が発生しました");
            }
        }

        @Test
        void 異常系_nullの場合バリデーションエラー() {
            /*
                given:
             */
            String invalidSlugValue = null;

            try {
                /*
                    when:
                 */
                Slug.newSlug(invalidSlugValue);
                fail("例外が発生しませんでした");
            } catch (Slug.EmptySlugError actual) {
                /*
                    then:
                 */
                Slug.EmptySlugError expected = new Slug.EmptySlugError();
                assertEquals(expected, actual);
            } catch (Slug.CreationErrorException e) {
                fail("想定外の例外が発生しました");
            }
        }
    }

    /**
     * Slug の有効な範囲の String プロパティ
     */
    final static class SlugValidRange implements ArbitrarySupplier<String> {
        @Override
        public Arbitrary<String> get() {
            return Arbitraries.strings()
                    .numeric()
                    .withCharRange('a', 'z')
                    .ofLength(32);
        }
    }

    /**
     * Slug の無効な範囲の String プロパティ
     */
    final static class SlugInvalidRange implements ArbitrarySupplier<String> {
        @Override
        public Arbitrary<String> get() {
            return Arbitraries.strings()
                    .filter(s -> !s.matches("^[a-z0-9]{32}$"));
        }
    }
}
