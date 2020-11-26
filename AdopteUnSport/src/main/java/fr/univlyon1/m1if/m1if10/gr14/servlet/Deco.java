package fr.univlyon1.m1if.m1if10.gr14.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.univlyon1.m1if.m1if10.gr14.route.Routes;
import fr.univlyon1.m1if.m1if10.gr14.util.CookieHelper;


/**
 * Servlet to disconnect an user from the application.
 */
@WebServlet(name = "Deco", urlPatterns = Routes.DECO_URI)
public class Deco extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {
        //Remove the cookie (override it)
        CookieHelper.invalidateTokenCookie(response);
        response.sendRedirect(request.getContextPath() + Routes.HOME_URI);
    }
}
