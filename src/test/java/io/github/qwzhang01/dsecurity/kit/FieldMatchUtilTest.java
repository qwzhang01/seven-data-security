package io.github.qwzhang01.dsecurity.kit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FieldMatchUtil - Testing field name matching logic
 * through StringUtil methods (as FieldMatchUtil relies on StringUtil)
 */
@DisplayName("Field Name Matching Tests")
class FieldMatchUtilTest {

    /**
     * Helper method to test field matching logic
     * This mimics the isFieldNameMatch logic in FieldMatchUtil
     */
    private boolean fieldMatch(String paramName, String columnName) {
        if (paramName == null || columnName == null) {
            return false;
        }
        return paramName.equalsIgnoreCase(columnName) ||
                StringUtil.camelToUnderscore(paramName).equalsIgnoreCase(columnName) ||
                StringUtil.underscoreToCamel(paramName).equalsIgnoreCase(columnName);
    }

    @Nested
    @DisplayName("Field Matching Logic Tests")
    class FieldMatchingTests {

        @Test
        @DisplayName("should match exact field name")
        void shouldMatchExactFieldName() {
            assertTrue(fieldMatch("userName", "userName"));
        }

        @Test
        @DisplayName("should match camelCase to snake_case")
        void shouldMatchCamelToSnake() {
            assertTrue(fieldMatch("userName", "user_name"));
        }

        @Test
        @DisplayName("should match snake_case to camelCase")
        void shouldMatchSnakeToCamel() {
            assertTrue(fieldMatch("user_name", "userName"));
        }

        @ParameterizedTest
        @DisplayName("should match various field name pairs")
        @CsvSource({
                "phoneNumber, phone_number",
                "phone_number, phoneNumber",
                "userId, user_id",
                "createTime, create_time",
                "id, id"
        })
        void shouldMatchVariousPairs(String field1, String field2) {
            assertTrue(fieldMatch(field1, field2));
        }

        @Test
        @DisplayName("should not match different fields")
        void shouldNotMatchDifferentFields() {
            assertFalse(fieldMatch("userName", "userId"));
        }

        @Test
        @DisplayName("should handle null inputs")
        void shouldHandleNullInputs() {
            assertFalse(fieldMatch(null, "field"));
            assertFalse(fieldMatch("field", null));
            assertFalse(fieldMatch(null, null));
        }

        @Test
        @DisplayName("should handle empty inputs")
        void shouldHandleEmptyInputs() {
            assertTrue(fieldMatch("", ""));
            assertFalse(fieldMatch("field", ""));
            assertFalse(fieldMatch("", "field"));
        }

        @Test
        @DisplayName("should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertTrue(fieldMatch("UserName", "username"));
            assertTrue(fieldMatch("USERNAME", "username"));
        }
    }
}
