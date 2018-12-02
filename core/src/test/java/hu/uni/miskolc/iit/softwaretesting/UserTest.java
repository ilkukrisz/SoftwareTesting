package hu.uni.miskolc.iit.softwaretesting;

import hu.uni.miskolc.iit.softwaretesting.model.User;
import hu.uni.miskolc.iit.softwaretesting.model.Password;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UserTest {

    User user;
    String username = "1d10tUs3r";
    Password password = new Password("gr34tL0ngP4ssw0rdF0r4S1mpl3User!");
    String firstName = "Bela";
    String lastName = "Toth";
    String email = "bela.toth@example.com";
    String mobileNumber = "06-99-1234567";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp () {
        this.user = new User(this.username, this.password, this.firstName, this.lastName, this.email, this.mobileNumber);
    }

    @Test
    public void testConstructorWithLegalValues () {
        try {
            new User(this.username, this.password, this.firstName, this.lastName, this.email, this.mobileNumber);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testConstructorWithIllegalValues () {
        exceptionRule.expect(IllegalArgumentException.class);
        new User(null, this.password, this.firstName, this.lastName, this.email, this.mobileNumber);
        
        exceptionRule.expect(IllegalArgumentException.class);
        new User("", this.password, this.firstName, this.lastName, this.email, this.mobileNumber);

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, null, this.firstName, this.lastName, this.email, this.mobileNumber);

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, new Password(""), this.firstName, this.lastName, this.email, this.mobileNumber);

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, this.password, null, this.lastName, this.email, this.mobileNumber);

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, this.password, "", this.lastName, this.email, this.mobileNumber);

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, this.password, this.firstName, null, this.email, this.mobileNumber);

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, this.password, this.firstName, "", this.email, this.mobileNumber);

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, this.password, this.firstName, this.lastName, null, this.mobileNumber);

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, this.password, this.firstName, this.lastName, "", this.mobileNumber);

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, this.password, this.firstName, this.lastName, "example#example.org", this.mobileNumber);

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, this.password, this.firstName, this.lastName, this.email, null);

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, this.password, this.firstName, this.lastName, this.email, "");

        exceptionRule.expect(IllegalArgumentException.class);
        new User(this.username, this.password, this.firstName, this.lastName, this.email, "123");
    }

    @Test
    public void testGetUsername () {
        assertEquals(this.username, this.user.getUsername());
    }

    @Test
    public void testSetUsername () {
        String username = "gyuriahegyrol";
        this.user.setUsername(username);
        assertEquals(username, this.user.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUsernameWithNull () {
        this.user.setUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUsernameWithEmptyString () {
        this.user.setUsername("");
    }



    @Test
    public void testGetPassword () {
        assertEquals(this.user.getPassword(), this.password);
    }

    @Test
    public void testSetPassword () {
        Password pswd = new Password("alma");
        this.user.setPassword(pswd);
        assertEquals(pswd, this.user.getPassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordWithNullValue () {
        this.user.setPassword(null);
    }



    @Test
    public void testGetFirstName () {
        assertEquals(this.firstName, this.user.getFirstName());
    }

    @Test
    public void testSetFirstName () {
        String firstName = "Jozsef";
        this.user.setFirstName(firstName);
        assertEquals(firstName, this.user.getFirstName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameWithNull () {
        this.user.setFirstName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameWithEmptyString () {
        this.user.setFirstName("");
    }



    @Test
    public void testGetLastName () {
        assertEquals(this.lastName, this.user.getLastName());
    }

    @Test
    public void testSetLastName () {
        String lastName = "Attila";
        this.user.setLastName(lastName);
        assertEquals(lastName, this.user.getLastName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameWithNull () {
        this.user.setLastName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameWithEmptyString () {
        this.user.setLastName("");
    }



    @Test
    public void testGetEmail () {
        assertEquals(this.email, this.user.getEmail());
    }

    @Test
    public void testSetEmail () {
        String email = "valid.email@example.com";
        this.user.setEmail(email);
        assertEquals(email, this.user.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmailToNull () {
        this.user.setEmail(null);
    }

    @Test
    public void testSetEmailToInvalidFormat () {
        exceptionRule.expect(IllegalArgumentException.class);
        this.user.setEmail("");

        exceptionRule.expect(IllegalArgumentException.class);
        this.user.setEmail("abcvcvc@example");

        exceptionRule.expect(IllegalArgumentException.class);
        this.user.setEmail("sdasd*!@aaa.com");

        exceptionRule.expect(IllegalArgumentException.class);
        this.user.setEmail("aaaa");
    }



    @Test
    public void testGetMobileNumber () {
        assertEquals(this.mobileNumber, this.user.getMobileNumber());
    }

    @Test
    public void testSetMobileNumber () {
        String mobile = "+36-30-765-4321";
        this.user.setMobileNumber(mobile);
        assertEquals(mobile, this.user.getMobileNumber());

        mobile = "06-30-765-4321";
        this.user.setMobileNumber(mobile);
        assertEquals(mobile, this.user.getMobileNumber());

        mobile = "06(30)765-4321";
        this.user.setMobileNumber(mobile);
        assertEquals(mobile, this.user.getMobileNumber());

        mobile = "06307654321";
        this.user.setMobileNumber(mobile);
        assertEquals(mobile, this.user.getMobileNumber());
    }

    @Test
    public void testSetMobileNumberToIllegalValues () {
        exceptionRule.expect(IllegalArgumentException.class);
        this.user.setMobileNumber(null);

        exceptionRule.expect(IllegalArgumentException.class);
        this.user.setMobileNumber("");

        exceptionRule.expect(IllegalArgumentException.class);
        this.user.setMobileNumber("06");

        exceptionRule.expect(IllegalArgumentException.class);
        this.user.setMobileNumber("0630!0001112");
    }
}
