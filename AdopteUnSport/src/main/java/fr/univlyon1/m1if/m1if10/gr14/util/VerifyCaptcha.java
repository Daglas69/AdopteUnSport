package fr.univlyon1.m1if.m1if10.gr14.util;

import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

/**
 * Utility class to verify google captcha.
 * WARNING : This is disable as it is not working on the VM
 */
public final class VerifyCaptcha {
    public static final String SITE_VERIFY_URL =
        "https://www.google.com/recaptcha/api/siteverify";


    /**
     * verifies the captcha with the string captcha response passed as argument.
     * @param gRecaptchaResponse Response of the captcha
     * @return True if the captcha is valid
     */
    public static boolean verify(final String gRecaptchaResponse) {
        if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0) {
            return false;
        }

        try {
            URL verifyUrl = new URL(SITE_VERIFY_URL);

            //New connection
            HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();

            //Headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            //Parameters
            String postParams = "secret=6Le2WOoZAAAAAPzpMqKs3W0RY7ci3Y6FiCZSk_KV"
                    + "&response=" + gRecaptchaResponse;

            //Send Request
            conn.setDoOutput(true);

            //Write data in the connection output stream <=> send data
            OutputStream outStream = conn.getOutputStream();
            outStream.write(postParams.getBytes());

            outStream.flush();
            outStream.close();

            //True is response code is ok
            return conn.getResponseCode() == 200;
        }
        catch (Exception e) {
            Logger.getLogger(VerifyCaptcha.class.getName()).log(
                Level.WARNING, "Verifying captcha: ", e
            );
            return false;
        }
    }


    //Final class
    private VerifyCaptcha() {
    }
}
