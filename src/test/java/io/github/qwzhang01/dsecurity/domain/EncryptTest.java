package io.github.qwzhang01.dsecurity.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Encrypt domain class
 */
@DisplayName("Encrypt Domain Tests")
class EncryptTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("should create Encrypt with value")
        void shouldCreateWithValue() {
            Encrypt encrypt = new Encrypt("testValue");
            assertEquals("testValue", encrypt.getValue());
        }

        @Test
        @DisplayName("should create Encrypt with null value")
        void shouldCreateWithNullValue() {
            Encrypt encrypt = new Encrypt(null);
            assertNull(encrypt.getValue());
        }

        @Test
        @DisplayName("should create Encrypt with empty value")
        void shouldCreateWithEmptyValue() {
            Encrypt encrypt = new Encrypt("");
            assertEquals("", encrypt.getValue());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("should get and set value correctly")
        void shouldGetAndSetValue() {
            Encrypt encrypt = new Encrypt("initial");
            assertEquals("initial", encrypt.getValue());

            encrypt.setValue("updated");
            assertEquals("updated", encrypt.getValue());
        }

        @Test
        @DisplayName("should handle setting null value")
        void shouldHandleNullValue() {
            Encrypt encrypt = new Encrypt("initial");
            encrypt.setValue(null);
            assertNull(encrypt.getValue());
        }
    }

    @Nested
    @DisplayName("Equals Tests")
    class EqualsTests {

        @Test
        @DisplayName("should be equal for same value")
        void shouldBeEqualForSameValue() {
            Encrypt encrypt1 = new Encrypt("testValue");
            Encrypt encrypt2 = new Encrypt("testValue");

            assertEquals(encrypt1, encrypt2);
        }

        @Test
        @DisplayName("should not be equal for different values")
        void shouldNotBeEqualForDifferentValues() {
            Encrypt encrypt1 = new Encrypt("value1");
            Encrypt encrypt2 = new Encrypt("value2");

            assertNotEquals(encrypt1, encrypt2);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            Encrypt encrypt = new Encrypt("test");
            assertNotEquals(null, encrypt);
        }

        @Test
        @DisplayName("should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            Encrypt encrypt = new Encrypt("test");
            assertNotEquals("test", encrypt);
        }

        @Test
        @DisplayName("should be reflexive")
        void shouldBeReflexive() {
            Encrypt encrypt = new Encrypt("test");
            assertEquals(encrypt, encrypt);
        }

        @Test
        @DisplayName("should be symmetric")
        void shouldBeSymmetric() {
            Encrypt encrypt1 = new Encrypt("test");
            Encrypt encrypt2 = new Encrypt("test");

            assertEquals(encrypt1, encrypt2);
            assertEquals(encrypt2, encrypt1);
        }
    }

    @Nested
    @DisplayName("HashCode Tests")
    class HashCodeTests {

        @Test
        @DisplayName("should have same hashCode for equal objects")
        void shouldHaveSameHashCodeForEqualObjects() {
            Encrypt encrypt1 = new Encrypt("test");
            Encrypt encrypt2 = new Encrypt("test");

            assertEquals(encrypt1.hashCode(), encrypt2.hashCode());
        }

        @Test
        @DisplayName("should have different hashCode for different values")
        void shouldHaveDifferentHashCodeForDifferentValues() {
            Encrypt encrypt1 = new Encrypt("value1");
            Encrypt encrypt2 = new Encrypt("value2");

            assertNotEquals(encrypt1.hashCode(), encrypt2.hashCode());
        }

        @Test
        @DisplayName("should be consistent")
        void shouldBeConsistent() {
            Encrypt encrypt = new Encrypt("test");
            int hashCode1 = encrypt.hashCode();
            int hashCode2 = encrypt.hashCode();

            assertEquals(hashCode1, hashCode2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("should return value as string")
        void shouldReturnValueAsString() {
            Encrypt encrypt = new Encrypt("testValue");
            assertEquals("testValue", encrypt.toString());
        }

        @Test
        @DisplayName("should return null for null value")
        void shouldReturnNullForNullValue() {
            Encrypt encrypt = new Encrypt(null);
            assertNull(encrypt.toString());
        }

        @Test
        @DisplayName("should return empty string for empty value")
        void shouldReturnEmptyStringForEmptyValue() {
            Encrypt encrypt = new Encrypt("");
            assertEquals("", encrypt.toString());
        }
    }
}
