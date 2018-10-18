package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.model.Password;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PasswordTest {

    String plainPassword = "passwd123!#";

    @Test
    public void testConstructorWithLegalValue () {
        try {
            new Password(this.plainPassword);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullValue () {
        new Password(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyValue () {
        new Password("");
    }

    @Test
    public void testPasswordChecking () {
        try {
            Password password = new Password(this.plainPassword);
            assertTrue("Password checking fails.", password.checkPassword(this.plainPassword));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPasswordCheckingWithIllegalValues () {
        Password password = new Password(this.plainPassword);
        password.checkPassword(null);
    }

    @Test
    public void testHashedPasswordGetter () {
        Password password = new Password(this.plainPassword);
        String hashed = password.getHashedPassword();

        assertTrue("getHashedPassword returns incorrect password hash.", BCrypt.checkpw(plainPassword, hashed));
    }

    @Test
    public void testToString () {
        Password password = new Password(this.plainPassword);
        String hashed = password.getHashedPassword();

        assertEquals("toString method is expected to return password hash.", hashed, password.toString());
    }
}