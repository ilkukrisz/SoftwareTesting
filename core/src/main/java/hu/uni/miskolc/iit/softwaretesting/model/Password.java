package hu.uni.miskolc.iit.softwaretesting.model;


import org.mindrot.jbcrypt.BCrypt;


public class Password {

    private static int saltStrength = 4;
    private String hashedPassword;

    public Password(String password, boolean toHash)
    {
        if (toHash)
        {
            String salt = BCrypt.gensalt(saltStrength);
            hashedPassword = BCrypt.hashpw(password, salt);
        }
        else
        {
            hashedPassword = password;
        }
    }

    public boolean checkPassword(String password_plaintext)
    {
        boolean password_verified = false;

        if (null == hashedPassword || !hashedPassword.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("The hashed password is invalid");

        password_verified = BCrypt.checkpw(password_plaintext, hashedPassword);

        return password_verified;
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
