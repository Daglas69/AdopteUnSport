package fr.univlyon1.m1if.m1if10.gr14.util;

import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;


/**
 * Utility class for password management.
 * Contains methods to validate, hash and compare hashed passwords.
 */
public final class PasswordHelper {

    //BCrypt workload used when generating password hashes, between 10 and 31.
    private static final int WORKLOAD = 12;

    //Valid password regular expression
    private static final String PASSWORD_REGEXP =
           "^"                          // In the whole expression:
         + "(?=.*[0-9])"                // At least one digit
         + "(?=.*[a-z])(?=.*[A-Z])"     // At least lower and upper case
         + "(?=.*[@#$%^&+=])"           // At least one special char
         + "(?!=\\s+)"                  // No whitespace
         + ".{5,30}$";                  // Between 5 and 30 characters total


    /**
     * Returns OpenBSD-style crypt(3) formatted 60 chars hashed password from a plain text string.
     * This automatically handles secure 128-bit salt generation and storage within the hash.
     * It uses the jbcrypt library.
     * @param password Plaintext password
     * @return 60 chars hashed password in crypt(3) format.
    */
    public static String hashPassword(final String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(WORKLOAD));
    }


    /**
     * Verifies if the plaintext password is equals to the hashed password.
     * @param password Plaintext password
     * @param hashedPassword Hashed password
     * @return True if the passwords match
     */
    public static boolean checkPassword(final String password, final String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
        }
        return BCrypt.checkpw(password, hashedPassword);
    }


    /**
     * Verifies the strength of a plaintext password according to application policies.
     * The password must be between 5 and 30 chars, with at least one digit, one lower
     * and upper case alphabetic character, and one special character.
     * Checked with a regular expression.
     * @param password Plaintext password.
     * @return True if the password is strong enough.
     */
    public static boolean checkPasswordStrength(final String password) {
        if (password == null) {
            return false;
        }
        return Pattern.compile(PASSWORD_REGEXP)
            .matcher(password)
            .matches();
    }


    //Final class
    private PasswordHelper() {
    }
}
