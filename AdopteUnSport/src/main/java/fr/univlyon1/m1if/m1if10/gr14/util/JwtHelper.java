package fr.univlyon1.m1if.m1if10.gr14.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * utility class for Json Web Token token management.
 *
 * s/o to Lionel Medini, whom made a first version of this class.
 */
public final class JwtHelper {
    private static final String SECRET = "uefBFuj27hAjnkHdnui92dnzuz6dsld67Dh2dZZq46zdzfkfPDpkMDdh";
    private static final String ISSUER = "Adopte Un Sport";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    public static final int TOKEN_AGE = 3600; //Token lifetime in seconds
    private static final long LIFETIME = (long) TOKEN_AGE * 1000; //Token lifetime in miliseconds


    /**
     * verifies the token validity.
     * Needs the request to check if the request origin
     * is the same than the one contained in the token.
     * @param token Token to check
     * @param req http request to get the origin
     * @return true if the token is valid, else null
     */
    public static String verifyToken(
        final String token, @NotNull final HttpServletRequest req) {
        try {
            JWTVerifier authenticationVerifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .withAudience(getOrigin(req)) // Non-reusable verifier instance
                    .build();

            authenticationVerifier.verify(token);

            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("sub").asString();
        }
        catch (Exception e) {
            return null;
        }
    }


    /**
     * Creates a new token with a subject and request origin.
     * @param subject Subject of the token (could be user email)
     * @param req Http request to get the request origin
     * @return Signed token
     * @throws JWTCreationException si les paramètres ne permettent pas de créer un token
     */
    public static String generateToken(
        final String subject, final HttpServletRequest req) throws JWTCreationException {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(subject)
                .withAudience(getOrigin(req))
                .withExpiresAt(new Date(new Date().getTime() + LIFETIME))
                .sign(ALGORITHM);
    }


    /**
     * Returns the origin URL of the client, according
     * to proxy headers if they exist, or the request URL if not.
     * @param request Http request
     * @return String containing the origin of the Http request
     */
    private static String getOrigin(@NotNull final HttpServletRequest request) {
        String origin = String.valueOf(
            request.getRequestURL())
            .substring(0, request.getRequestURL()
            .lastIndexOf(request.getRequestURI()));

        if (request.getHeader("X-Forwarded-Host") != null
        && request.getHeader("X-Forwarded-Proto") != null
        && request.getHeader("X-Forwarded-Path") != null) {
            switch (request.getHeader("X-Forwarded-Proto")) {
                case "http":
                    origin = request.getHeader("X-Forwarded-Proto") + "://"
                    + (request.getHeader("X-Forwarded-Host").endsWith(":80")
                    ? request.getHeader("X-Forwarded-Host").replace(":80", "")
                    : request.getHeader("X-Forwarded-Host"));
                    break;
                case "https":
                    origin = request.getHeader("X-Forwarded-Proto") + "://"
                    + (request.getHeader("X-Forwarded-Host").endsWith(":443")
                    ? request.getHeader("X-Forwarded-Host").replace(":443", "")
                    : request.getHeader("X-Forwarded-Host"));
                    break;
                default:
                    break;
            }
            origin = origin + request.getHeader("X-Forwarded-Path");
        }
        return origin + request.getContextPath();
    }


    //Constructeur privé car la classe est finale.
    private JwtHelper() {
    }
}
