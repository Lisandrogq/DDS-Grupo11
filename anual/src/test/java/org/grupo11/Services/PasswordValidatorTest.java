package org.grupo11.Services;

import org.junit.Test;
import static org.junit.Assert.*;

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

}
