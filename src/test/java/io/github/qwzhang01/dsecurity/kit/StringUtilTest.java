package io.github.qwzhang01.dsecurity.kit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StringUtil
 */
@DisplayName("StringUtil Tests")
class StringUtilTest {

    @Nested
    @DisplayName("camelToUnderscore Tests")
    class CamelToUnderscoreTests {

        @ParameterizedTest
        @DisplayName("should convert camelCase to snake_case")
        @CsvSource({
                "userName, user_name",
                "phoneNumber, phone_number",
                "userId, user_id",
                "createTime, create_time",
                "myBatisPlus, my_batis_plus"
        })
        void shouldConvertCamelToSnake(String input, String expected) {
            assertEquals(expected, StringUtil.camelToUnderscore(input));
        }

        @Test
        @DisplayName("should handle single word")
        void shouldHandleSingleWord() {
            assertEquals("name", StringUtil.camelToUnderscore("name"));
        }

        @Test
        @DisplayName("should handle uppercase string")
        void shouldHandleUppercase() {
            assertEquals("id", StringUtil.camelToUnderscore("ID"));
        }

        @ParameterizedTest
        @DisplayName("should handle null and empty")
        @NullAndEmptySource
        void shouldHandleNullAndEmpty(String input) {
            assertEquals(input, StringUtil.camelToUnderscore(input));
        }
    }

    @Nested
    @DisplayName("underscoreToCamel Tests")
    class UnderscoreToCamelTests {

        @ParameterizedTest
        @DisplayName("should convert snake_case to camelCase")
        @CsvSource({
                "user_name, userName",
                "phone_number, phoneNumber",
                "user_id, userId",
                "create_time, createTime",
                "my_batis_plus, myBatisPlus"
        })
        void shouldConvertSnakeToCamel(String input, String expected) {
            assertEquals(expected, StringUtil.underscoreToCamel(input));
        }

        @Test
        @DisplayName("should handle leading underscore")
        void shouldHandleLeadingUnderscore() {
            assertEquals("Id", StringUtil.underscoreToCamel("_id"));
        }

        @Test
        @DisplayName("should handle single word")
        void shouldHandleSingleWord() {
            assertEquals("name", StringUtil.underscoreToCamel("name"));
        }

        @ParameterizedTest
        @DisplayName("should handle null and empty")
        @NullAndEmptySource
        void shouldHandleNullAndEmpty(String input) {
            assertEquals(input, StringUtil.underscoreToCamel(input));
        }
    }

    @Nested
    @DisplayName("cleanParameterName Tests")
    class CleanParameterNameTests {

        @Test
        @DisplayName("should remove param. prefix")
        void shouldRemoveParamPrefix() {
            assertEquals("userId", StringUtil.cleanParameterName("param.userId"));
        }

        @Test
        @DisplayName("should remove arg. prefix")
        void shouldRemoveArgPrefix() {
            assertEquals("phoneNo", StringUtil.cleanParameterName("arg.phoneNo"));
        }

        @Test
        @DisplayName("should keep name without prefix")
        void shouldKeepNameWithoutPrefix() {
            assertEquals("fieldName", StringUtil.cleanParameterName("fieldName"));
        }

        @Test
        @DisplayName("should handle null")
        void shouldHandleNull() {
            assertNull(StringUtil.cleanParameterName(null));
        }
    }

    @Nested
    @DisplayName("extractFieldName Tests")
    class ExtractFieldNameTests {

        @Test
        @DisplayName("should extract field from nested path")
        void shouldExtractFromNestedPath() {
            assertEquals("city", StringUtil.extractFieldName("user.address.city"));
        }

        @Test
        @DisplayName("should extract field from simple nested path")
        void shouldExtractFromSimpleNestedPath() {
            assertEquals("phoneNo", StringUtil.extractFieldName("userParam.phoneNo"));
        }

        @Test
        @DisplayName("should return original for no dot")
        void shouldReturnOriginalForNoDot() {
            assertEquals("fieldName", StringUtil.extractFieldName("fieldName"));
        }

        @Test
        @DisplayName("should handle trailing dot")
        void shouldHandleTrailingDot() {
            assertEquals("field.", StringUtil.extractFieldName("field."));
        }

        @ParameterizedTest
        @DisplayName("should handle null and empty")
        @NullAndEmptySource
        void shouldHandleNullAndEmpty(String input) {
            assertEquals(input, StringUtil.extractFieldName(input));
        }
    }

    @Nested
    @DisplayName("isEmpty Tests")
    class IsEmptyTests {

        @Test
        @DisplayName("should return true for null")
        void shouldReturnTrueForNull() {
            assertTrue(StringUtil.isEmpty(null));
        }

        @Test
        @DisplayName("should return true for empty string")
        void shouldReturnTrueForEmpty() {
            assertTrue(StringUtil.isEmpty(""));
        }

        @ParameterizedTest
        @DisplayName("should return true for whitespace only")
        @ValueSource(strings = {" ", "  ", "\t", "\n", "  \t\n  "})
        void shouldReturnTrueForWhitespace(String input) {
            assertTrue(StringUtil.isEmpty(input));
        }

        @ParameterizedTest
        @DisplayName("should return false for non-empty strings")
        @ValueSource(strings = {"hello", " hi ", "test", "a"})
        void shouldReturnFalseForNonEmpty(String input) {
            assertFalse(StringUtil.isEmpty(input));
        }
    }

    @Nested
    @DisplayName("clearSqlTip Tests")
    class ClearSqlTipTests {

        @Test
        @DisplayName("should remove backticks")
        void shouldRemoveBackticks() {
            assertEquals("user_name", StringUtil.clearSqlTip("`user_name`"));
        }

        @Test
        @DisplayName("should trim whitespace")
        void shouldTrimWhitespace() {
            assertEquals("table_name", StringUtil.clearSqlTip("  table_name  "));
        }

        @Test
        @DisplayName("should handle null")
        void shouldHandleNull() {
            assertNull(StringUtil.clearSqlTip(null));
        }

        @Test
        @DisplayName("should handle empty string")
        void shouldHandleEmpty() {
            assertEquals("", StringUtil.clearSqlTip(""));
        }
    }

    @Nested
    @DisplayName("Round-trip Conversion Tests")
    class RoundTripTests {

        @ParameterizedTest
        @DisplayName("camelCase -> snake_case -> camelCase should be consistent")
        @ValueSource(strings = {"userName", "phoneNumber", "createTime", "userId"})
        void shouldRoundTripCorrectly(String original) {
            String snakeCase = StringUtil.camelToUnderscore(original);
            String backToCamel = StringUtil.underscoreToCamel(snakeCase);
            assertEquals(original, backToCamel);
        }
    }
}
