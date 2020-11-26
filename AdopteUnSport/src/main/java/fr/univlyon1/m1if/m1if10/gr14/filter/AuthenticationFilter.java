package fr.univlyon1.m1if.m1if10.gr14.filter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.univlyon1.m1if.m1if10.gr14.dao.UserDao;
import fr.univlyon1.m1if.m1if10.gr14.route.Router;
import fr.univlyon1.m1if.m1if10.gr14.route.Routes;
import fr.univlyon1.m1if.m1if10.gr14.util.CookieHelper;
import fr.univlyon1.m1if.m1if10.gr14.util.JwtHelper;


/**
 * Filter to manage request authentication.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
    "/*"
})
public class AuthenticationFilter extends HttpFilter {
    private static final long serialVersionUID = 1L;
    private ServletContext context;


    /**
     * Initialize the filter.
     * @param config Filter configuration
     */
    public void init(final FilterConfig config) throws ServletException {
        this.context = config.getServletContext();
        this.context.log("AuthenticationFilter initialized");
    }


    @Override
    protected void doFilter(
        final HttpServletRequest request,
        final HttpServletResponse response, final FilterChain chain)
        throws java.io.IOException, ServletException {

        //For each request, we try to verify the JWT token validity stored in a cookie
        //If it does not exist, or it is invalid, it will return null
        String token = CookieHelper.getToken(request);
        String jwtUserEmail = JwtHelper.verifyToken(token, request);

        //If the token is valid, we add user info in request attributes
        if (jwtUserEmail != null) {
            UserDao userDao = (UserDao) request.getServletContext().getAttribute("userDAO");
            request.setAttribute("jwtUser", userDao.getUserByEmail(jwtUserEmail));
            request.setAttribute("jwtUserEmail", jwtUserEmail);
            chain.doFilter(request, response);
        }

        //If the token is invalid
        //For unauthenticated requests
        else {
            //We get the URI from the request to check if the
            //route is accessible for unauthenticated users
            String uri = Router.getURI(request);
            if (Router.isAuthNeeded(uri)) {
                this.context.log("Unauthenticated request on: " + uri);
                response.sendRedirect(request.getContextPath() + Routes.SIGN_IN_URI);
                return;
            }
            else {
                chain.doFilter(request, response);
                return;
            }
        }
    }
}
