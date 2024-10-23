package org.grupo11.Services;

import org.junit.Test;
import static org.junit.Assert.*;

import org.grupo11.Utils.PasswordValidator;

/**
 * As far as I understand, we should not be writing unit tests that directly
 * test private methods. What we must be testing is the public contract that the
 * class has with other objects.
 * 
 * here you have this article for further reading
 * https://www.artima.com/articles/testing-private-methods-with-junit-and-suiterunner
 */
public class PasswordValidatorTest {
    @Test
    public void ValidatePassword_shouldNotBeValid() {
        PasswordValidator.Result result = PasswordValidator.ValidatePassword("hola1234");
        assertFalse(result.valid);
    }

    @Test
    public void ValidatePassword_shouldBeValid() {
        PasswordValidator.Result result = PasswordValidator.ValidatePassword("Halas213.321778923.321");
        assertTrue(result.valid);
    }

    @Test
    public void HasMinimumLength_shouldReturnTrue() {
        assertTrue(PasswordValidator.HasMinimumLength("ValidPassword1!"));
    }

    @Test
    public void HasMinimumLength_shouldReturnFalse() {
        assertFalse(PasswordValidator.HasMinimumLength("Short1!"));
    }

    @Test
    public void HasUppercaseLetter_shouldReturnTrue() {
        assertTrue(PasswordValidator.HasUppercaseLetter("ValidPassword1!"));
    }

    @Test
    public void HasUppercaseLetter_shouldReturnFalse() {
        assertFalse(PasswordValidator.HasUppercaseLetter("validpassword1!"));
    }

    @Test
    public void HasNumber_shouldReturnTrue() {
        assertTrue(PasswordValidator.HasNumber("ValidPassword1!"));
    }

    @Test
    public void HasNumber_shouldReturnFalse() {
        assertFalse(PasswordValidator.HasNumber("ValidPassword!"));
    }

    @Test
    public void HasSpecialSymbol_shouldReturnTrue() {
        assertTrue(PasswordValidator.HasSpecialSymbol("ValidPassword1!"));
    }

    @Test
    public void HasSpecialSymbol_shouldReturnFalse() {
        assertFalse(PasswordValidator.HasSpecialSymbol("ValidPassword1"));
    }

    @Test
    public void IsKnownPassword_shouldReturnFalseForKnownPassword() {
        assertFalse(PasswordValidator.IsKnownPassword("hola"));
    }

    @Test
    public void IsKnownPassword_shouldReturnTrueForKnownPassword() {
        assertTrue(PasswordValidator.IsKnownPassword("passrodsklowewq..ewq?382910"));
    }
}
