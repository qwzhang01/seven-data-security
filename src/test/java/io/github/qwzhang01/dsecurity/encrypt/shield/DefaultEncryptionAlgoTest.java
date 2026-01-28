package io.github.qwzhang01.dsecurity.encrypt.shield;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DefaultEncryptionAlgo
 */
@DisplayName("DefaultEncryptionAlgo Tests")
class DefaultEncryptionAlgoTest {

    private DefaultEncryptionAlgo algo;

    @BeforeEach
    void setUp() {
        algo = new DefaultEncryptionAlgo();
    }

    @Nested
    @DisplayName("Encryption Tests")
    class EncryptionTests {

        @Test
        @DisplayName("should encrypt plain text and add prefix")
        void shouldEncryptPlainText() {
            String plainText = "13800138000";
            String encrypted = algo.encrypt(plainText);

            assertNotNull(encrypted);
            assertTrue(encrypted.startsWith("_sensitive_start_"));
            assertNotEquals(plainText, encrypted);
        }

        @Test
        @DisplayName("should not double encrypt already encrypted text")
        void shouldNotDoubleEncrypt() {
            String plainText = "test@email.com";
            String firstEncrypt = algo.encrypt(plainText);
            String secondEncrypt = algo.encrypt(firstEncrypt);

            assertEquals(firstEncrypt, secondEncrypt);
        }

        @ParameterizedTest
        @DisplayName("should encrypt various plain text values")
        @ValueSource(strings = {
                "hello",
                "123456",
                "test@example.com",
                "中文测试",
                "Special!@#$%^&*()",
                "   spaces   ",
                "very-long-string-that-needs-encryption-12345678901234567890"
        })
        void shouldEncryptVariousValues(String plainText) {
            String encrypted = algo.encrypt(plainText);

            assertNotNull(encrypted);
            assertTrue(encrypted.startsWith("_sensitive_start_"));
        }
    }

    @Nested
    @DisplayName("Decryption Tests")
    class DecryptionTests {

        @Test
        @DisplayName("should decrypt encrypted text correctly")
        void shouldDecryptCorrectly() {
            String original = "13800138000";
            String encrypted = algo.encrypt(original);
            String decrypted = algo.decrypt(encrypted);

            assertEquals(original, decrypted);
        }

        @Test
        @DisplayName("should handle text without prefix")
        void shouldHandleTextWithoutPrefix() {
            // For text that's already in encrypted format (base64) without prefix
            String encrypted = algo.encrypt("testValue");
            String encryptedContent = encrypted.substring(17); // Remove prefix
            
            String decrypted = algo.decrypt(encryptedContent);
            assertEquals("testValue", decrypted);
        }

        @ParameterizedTest
        @DisplayName("should encrypt and decrypt round-trip correctly")
        @ValueSource(strings = {
                "simple",
                "With Spaces",
                "12345",
                "email@test.com",
                "中文内容",
                "Mixed混合Content内容123"
        })
        void shouldRoundTripCorrectly(String original) {
            String encrypted = algo.encrypt(original);
            String decrypted = algo.decrypt(encrypted);

            assertEquals(original, decrypted);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle empty string")
        void shouldHandleEmptyString() {
            String encrypted = algo.encrypt("");
            assertNotNull(encrypted);
            assertTrue(encrypted.startsWith("_sensitive_start_"));

            String decrypted = algo.decrypt(encrypted);
            assertEquals("", decrypted);
        }

        @Test
        @DisplayName("should handle unicode characters")
        void shouldHandleUnicode() {
            String original = "你好世界🌍emoji😀";
            String encrypted = algo.encrypt(original);
            String decrypted = algo.decrypt(encrypted);

            assertEquals(original, decrypted);
        }

        @Test
        @DisplayName("should handle special characters")
        void shouldHandleSpecialCharacters() {
            String original = "!@#$%^&*()_+-={}[]|\\:\";<>?,./~`";
            String encrypted = algo.encrypt(original);
            String decrypted = algo.decrypt(encrypted);

            assertEquals(original, decrypted);
        }

        @Test
        @DisplayName("should handle newlines and tabs")
        void shouldHandleWhitespace() {
            String original = "line1\nline2\ttab";
            String encrypted = algo.encrypt(original);
            String decrypted = algo.decrypt(encrypted);

            assertEquals(original, decrypted);
        }

        @Test
        @DisplayName("should handle very long string")
        void shouldHandleLongString() {
            String original = "A".repeat(10000);
            String encrypted = algo.encrypt(original);
            String decrypted = algo.decrypt(encrypted);

            assertEquals(original, decrypted);
        }
    }

    @Nested
    @DisplayName("Consistency Tests")
    class ConsistencyTests {

        @Test
        @DisplayName("should produce consistent encryption for same input")
        void shouldBeConsistent() {
            String plainText = "consistentTest";
            String encrypted1 = algo.encrypt(plainText);
            String encrypted2 = algo.encrypt(plainText);

            assertEquals(encrypted1, encrypted2);
        }

        @Test
        @DisplayName("should produce different encryption for different inputs")
        void shouldDifferForDifferentInputs() {
            String encrypted1 = algo.encrypt("value1");
            String encrypted2 = algo.encrypt("value2");

            assertNotEquals(encrypted1, encrypted2);
        }
    }
}
