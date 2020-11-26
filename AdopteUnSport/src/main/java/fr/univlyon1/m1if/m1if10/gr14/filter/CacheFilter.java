//package fr.univlyon1.m1if.m1if10.gr14.filter;

//import fr.univlyon1.m1if.m1if10.gr14.route.Routes;
//import java.io.IOException;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

/*
 * We did not find a way to reload the cached page when a user
 * connects or disconnects himself to the application.
 * So we decided to do not integrate the cacheFilter
 * for the moment.
*/


/*
 * Filter to manage cache.
 */
//@WebFilter(
//    filterName = "CacheFilter",
//    urlPatterns = {
//        Routes.ACCOUNT_DETAILS_URI,
//        Routes.HOME_URI,
//        Routes.EVENT_QUIT_URI,
//        Routes.EVENT_JOIN_URI,
//        Routes.EVENT_SUPPR_URI,
//        Routes.EVENT_MODIF_URI,
//        Routes.EVENT_CREATE_URI
//    }
//)
/*public class CacheFilter extends HttpFilter {
    private static final long serialVersionUID = 1L;

    //Stores the last server change on events
    private long lastChange = System.currentTimeMillis();


    @Override
    protected void doFilter(
        final HttpServletRequest request,
        final HttpServletResponse response, final FilterChain chain)
        throws IOException, ServletException {

        //We update last server change time for POST methods
        if (request.getMethod().equals("POST"))  {
            this.lastChange = System.currentTimeMillis();
        }

        else if (request.getMethod().equals("GET")) {

            //We check if the browser and server last change time are equals
            if (this.lastChange <= request.getDateHeader("If-Modified-Since")
                && request.getDateHeader("If-Modified-Since") != -1) {
                response.setStatus(304); //Nothing changed
                return;
            }
            //We update the browser last change time
            response.setDateHeader("Last-Modified", System.currentTimeMillis());
        }
        chain.doFilter(request, response);
    }
}
*/
