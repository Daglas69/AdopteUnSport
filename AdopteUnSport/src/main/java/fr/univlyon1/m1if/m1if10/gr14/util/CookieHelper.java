package fr.univlyon1.m1if.m1if10.gr14.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Utility class for cookie management.
 */
public final class CookieHelper {
    //Name of the cookie where the token will be stored
    private static final String TOKEN_NAME = "token";


    /**
     * Creates a new Cookie to store the token passed as argument.
     * @param token Value of the token
     * @return Cookie storing the token
     */
    public static Cookie createTokenCookie(final String token) {
        Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true); => issues on VM as the website is not using HTTPS
        cookie.setMaxAge(JwtHelper.TOKEN_AGE); //Same than token lifetime
        return cookie;
    }


    /**
     * Parses and returns the token cookie of the request if it exists.
     * @param req Request containing the cookies
     * @return Cookie or null
     */
    public static Cookie getTokenCookie(final HttpServletRequest req) {
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if (c.getName().equals(TOKEN_NAME)) {
                    return c;
                }
            }
        }
        return null;
    }


    /**
     * Parses and returns the JWT token stored in a cookie of the request if it exists.
     * @param req Request containing the cookies
     * @return JWT Token or null
     */
    public static String getToken(final HttpServletRequest req) {
        Cookie c = getTokenCookie(req);
        return ((c != null) ? c.getValue() : null);
    }


    /**
     * Invalidate the token cookie of an HTTP response.
     * Overrides the cookie with a null value.
     * @param res HTTP response containing cookies
     */
    public static void invalidateTokenCookie(final HttpServletResponse res) {
        res.addCookie(createTokenCookie(null));
    }



    //Final class
    private CookieHelper() {
    }
}
