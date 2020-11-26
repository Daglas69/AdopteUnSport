package fr.univlyon1.m1if.m1if10.gr14.route;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Defines all the routes of the application.
 * Every Route object is associated to a String URI.
 */
public final class Routes {

    //INIT
    public static final String INIT_URI = "/init";


    /* ----------------------------------------------------------------- */
    /* ---------------------------- HOME ------------------------------- */
    /* ----------------------------------------------------------------- */

    public static final String HOME_URI = "/accueil";
    public static final Route HOME = new Route(
        HOME_URI
    );



    /* ----------------------------------------------------------------- */
    /* --------------------------- ACCOUNT ----------------------------- */
    /* ----------------------------------------------------------------- */

    public static final String ACCOUNT_RES = "/compte";

    public static final String DECO_URI = "/deconnexion";
    public static final Route DECO = new Route(
        DECO_URI,
        true
    );
    public static final String SIGN_UP_URI = "/inscription";
    public static final Route SIGN_UP = new Route(
        SIGN_UP_URI
    );
    public static final String SIGN_IN_URI = "/connexion";
    public static final Route SIGN_IN = new Route(
        SIGN_IN_URI
    );
    public static final String ACCOUNT_DETAILS_URI = ACCOUNT_RES + "/details";
    public static final Route ACCOUNT_DETAILS = new Route(
        ACCOUNT_DETAILS_URI,
        true
    );



    /* ----------------------------------------------------------------- */
    /* ---------------------------- EVENT ------------------------------ */
    /* ----------------------------------------------------------------- */

    public static final String EVENT_RES = "/evenement";

    public static final String EVENT_CREATE_URI = EVENT_RES + "/creation";
    public static final Route EVENT_CREATE = new Route(
        EVENT_CREATE_URI,
        true
    );
    public static final String EVENT_MODIF_URI = EVENT_RES + "/modification";
    public static final Route EVENT_MODIF = new Route(
        EVENT_MODIF_URI,
        true
    );

    public static final String EVENT_SUPPR_URI = EVENT_RES + "/suppression";
    public static final Route EVENT_SUPPR = new Route(
        EVENT_SUPPR_URI,
        true
    );

    public static final String EVENT_JOIN_URI = EVENT_RES + "/rejoindre";
    public static final Route EVENT_JOIN = new Route(
        EVENT_JOIN_URI,
        true
    );

    public static final String EVENT_QUIT_URI = EVENT_RES + "/quitter";
    public static final Route EVENT_QUIT = new Route(
        EVENT_QUIT_URI,
        true
    );

    public static final String EVENT_DETAILS_URI = EVENT_RES + "/details";
    public static final Route EVENT_DETAILS = new Route(
        EVENT_DETAILS_URI
    );



    /* ----------------------------------------------------------------- */
    /* ---------------------------- OTHER ------------------------------ */
    /* ----------------------------------------------------------------- */

    public static final String ABOUT_URI = "/about";
    public static final Route ABOUT = new Route(
        ABOUT_URI
    );





    /**
     * Returns all the routes of the application.
     * Uses the Java reflection to get the static route fields of the class.
     * @return List of routes
     */
    public static List<Route> getAll() {
        try {
            List<Route> routes = new ArrayList<>();
            Field[] fields = Routes.class.getDeclaredFields();
            for (Field f : fields) {
                if (Modifier.isStatic(f.getModifiers())) {
                    if (f.getType().equals(Route.class)) {
                        routes.add((Route) f.get(null));
                    }
                }
            }
            return routes;
        }
        catch (Exception e) {
            //Should not happen
            //We return an empty list
            Logger.getLogger(Routes.class.getName()).log(
                Level.SEVERE, "Getting all routes : ", e
            );
            return new ArrayList<>();
        }
    }


    /**
     * Returns the routes by authentication, according to the boolean argument.
     * Set it to true to get the routes accessible only for auhtenticated users.
     * Uses the Java reflection to get the static route fields of the class.
     * @param auth True to get routes needing authentication
     * @return List of routes
     */
    public static List<Route> getByAuth(final boolean auth) {
        try {
            List<Route> routes = new ArrayList<>();
            Field[] fields = Routes.class.getDeclaredFields();
            for (Field f : fields) {
                if (Modifier.isStatic(f.getModifiers())) {
                    if (f.getType().equals(Route.class)) {
                        Route r = (Route) f.get(null);
                        if (r.needAuth() == auth) {
                            routes.add(r);
                        }
                    }
                }
            }
            return routes;
        }
        catch (Exception e) {
            //Should not happen
            //We return an empty list
            Logger.getLogger(Routes.class.getName()).log(
                Level.SEVERE, "Getting routes by auth :", e
            );
            return new ArrayList<>(); //No null
        }
    }



    //Final class
    private Routes() {
    }
}
