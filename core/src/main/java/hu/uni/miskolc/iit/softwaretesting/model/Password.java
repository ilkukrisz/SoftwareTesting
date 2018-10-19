package hu.uni.miskolc.iit.softwaretesting.model;

import org.mindrot.jbcrypt.BCrypt;

public class Password {

    private static int saltStrength = 4;
    private String hashedPassword;

    public Password(String password)
    {
        if (password == null) {
            throw new IllegalArgumentException("Password should not be null value");
        }

        if (password.equals("")) {
            throw new IllegalArgumentException("Password should not be empty");
        }

        String salt = BCrypt.gensalt(saltStrength);
        hashedPassword = BCrypt.hashpw(password, salt);
    }

    public boolean checkPassword(String passwordPlaintext)
    {
        if (passwordPlaintext == null) {
            throw new IllegalArgumentException("Password should not be null value");
        }

        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            throw new IllegalArgumentException("The hashed password is invalid");
        }

        boolean passwordVerified = false;
        passwordVerified = BCrypt.checkpw(passwordPlaintext, hashedPassword);

        return passwordVerified;
    }

    public String getHashedPassword()
    {
        return hashedPassword;
    }

    public String toString()
    {
        return getHashedPassword();
    }
}
