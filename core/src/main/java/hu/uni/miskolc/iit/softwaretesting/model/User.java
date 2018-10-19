package hu.uni.miskolc.iit.softwaretesting.model;

/**
 * This object represents user data.
 */
public class User {

    /**
     * @param username - The users name
     **/
    private String username;
    /**
     * @param password - The users password
     **/
    private Password password;
    /**
     * @param firstName - The users firstname.
     **/
    private String firstName;
    /**
     * @param lastName - The users lastname.
     **/
    private String lastName;
    /**
     * @param email - The users e-mail address.
     **/
    private String email;
    /**
     * @param mobileNumber - The users mobile phone number.
     **/
    private String mobileNumber;

    public User(String username, Password password, String firstName, String lastName, String email, String mobileNumber) {
        this.validateUsername(username);
        this.username = username;

        this.validatePassword(password);
        this.password = password;

        this.validateName(firstName);
        this.firstName = firstName;

        this.validateName(lastName);
        this.lastName = lastName;

        this.validateEmail(email);
        this.email = email;

        this.validateMobileNumber(mobileNumber);
        this.mobileNumber = mobileNumber;
    }

    private boolean isNullOrEmpty (String value) {
        return (value == null || value.isEmpty());
    }

    private void validateUsername (String username) {
        if (this.isNullOrEmpty(username)) {
            throw new IllegalArgumentException("Username is a required value.");
        }
    }

    private void validatePassword (Password password) {
        if (password == null) {
            throw new IllegalArgumentException("Password is a required value.");
        }
    }

    private void validateName (String name) {
        if (this.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Name is a required value.");
        }
    }

    private void validateEmail (String email) {
        if (this.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("Email is a required value.");
        }
        //source: https://emailregex.com/
        if (!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08" +
                "\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]" +
                "|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c" +
                "\\x0e-\\x7f])+)\\])")) {
            throw new IllegalArgumentException("Email address is not valid.");
        }
    }

    private void validateMobileNumber (String mobile) {
        if (this.isNullOrEmpty(mobile)) {
            throw new IllegalArgumentException("Mobile number is a required value.");
        }

        if (!mobile.matches("((?:\\+?3|0)6)(?:-|\\()?(\\d{1,2})(?:-|\\))?(\\d{3})-?(\\d{3,4})")) {
            throw new IllegalArgumentException("Mobile number is not valid.");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.validateUsername(username);
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.validatePassword(password);
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.validateName(firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.validateName(lastName);
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.validateEmail(email);
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.validateMobileNumber(mobileNumber);
        this.mobileNumber = mobileNumber;
    }

    @Override
    public java.lang.String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}