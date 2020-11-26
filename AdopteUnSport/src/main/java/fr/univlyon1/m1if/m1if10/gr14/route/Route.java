package fr.univlyon1.m1if.m1if10.gr14.route;

/**
 * Defines a route of the application.
 */
public class Route {
    private final String uri;
    private final boolean needAuth; //True if the route can not be accessed without authentication

    /**
     * Constructor of the class, setting needAuth field to false.
     * The route does not need to be authenticated
     * @param uri URI of the route
     */
    public Route(final String uri) {
        this.uri = uri;
        this.needAuth = false;
    }

    /**
     * Constructor of the class.
     * @param uri URI of the route
     * @param needAuth True if the route is accessible only by authenticated user
     */
    public Route(final String uri, final boolean needAuth) {
        this.uri = uri;
        this.needAuth = needAuth;
    }

    /**
     * Getter for the URI field.
     * @return URI of the route
     */
    public String uri() {
        return this.uri;
    }

    /**
     * Getter for the needAuth field.
     * @return True if the user must be authenticated to access to the route
     */
    public boolean needAuth() {
        return this.needAuth;
    }
}
