package fr.univlyon1.m1if.m1if10.gr14.route;

import javax.servlet.http.HttpServletRequest;


/**
 * Static class for route management.
 */
public final class Router {

    /**
     * Returns the URI of a request, without context path and request parameters.
     * @param request Request containing the URI
     * @return URI of the request
     */
    public static String getURI(final HttpServletRequest request) {
        String uriWithArgs = request.getRequestURI().substring(request.getContextPath().length());
        //if the URI contains parameters, we return only the URI
        if (uriWithArgs.contains("?")) {
            return uriWithArgs.split("?")[0];
        }
        return uriWithArgs;
    }


    /**
     * Returns true if the URI passed as argument is accessible only for authenticated users.
     * @param uri URI of the request
     * @return True if the user must be authenticated
     */
    public static boolean isAuthNeeded(final String uri) {
        for (Route r : Routes.getByAuth(true)) {
            if (r.uri().equals(uri)) {
                return true;
            }
        }
        return false;
    }



    //Final class
    private Router() {
    }
}
