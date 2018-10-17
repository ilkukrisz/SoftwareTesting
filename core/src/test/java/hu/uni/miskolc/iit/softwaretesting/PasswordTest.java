package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.model.Password;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.Assert.fail;

public class PasswordTest {

    String plainPassword = "passwd123!#";

    @Test
    public void testConstructorWithLegalValues () {
        try {
            new Password(this.plainPassword, false);
            new Password(this.plainPassword, true);
            new Password("", true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testConstructorWithNullValue () {
        try {
            new Password(null, true);
            fail("Exception is expected for null value as password");
        } catch (IllegalArgumentException e) {
            //test succeeded
        }
    }

    @Test
    public void testPasswordCheckingWithHashedPassword () {
        try {
            Password hashedPassword = new Password(this.plainPassword, true);
            hashedPassword.checkPassword(this.plainPassword);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPassWordCheckingWithPlainPassword () {
        try {
            Password plainPassword = new Password(this.plainPassword, false);
            plainPassword.checkPassword(this.plainPassword);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPasswordCheckingWithIllegalValues () {
        try {
            Password password = new Password(this.plainPassword, true);
            password.checkPassword(null);
            fail("Exception is expected when the plain password param is null in checkPassword method.");
        } catch (IllegalArgumentException e) {
            //test succeeded
        }

        try {
            Password password = new Password(this.plainPassword, false);
            password.checkPassword(null);
            fail("Exception is expected when the plain password param is null in checkPassword method.");
        } catch (IllegalArgumentException e) {
            //test succeeded
        }
    }

    @Test
    public void testHashedPasswordGetter () {
        Password password1 = new Password(this.plainPassword, false);
        if (!password1.getHashedPassword().equals(this.plainPassword)) {
            fail("Password getter returns incorrect plain password.");
        }

        Password password2 = new Password(plainPassword, true);
        String hashed = password2.getHashedPassword();
        if (!BCrypt.checkpw(plainPassword, hashed)) {
            fail("Hashed password getter returns incorrect hash.");
        }
    }

    @Test
    public void testToString () {
        Password password1 = new Password(this.plainPassword, false);
        if (!password1.toString().equals(this.plainPassword)) {
            fail("toString method is expected to return plain password.");
        }

        Password password2 = new Password(this.plainPassword, true);
        String hashed = password2.getHashedPassword();
        if (!password2.toString().equals(hashed)) {
            fail("toString method is expected to return password hash.");
        }
    }
}