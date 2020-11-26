package fr.univlyon1.m1if.m1if10.gr14.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.univlyon1.m1if.m1if10.gr14.route.Routes;


/**
 * Controller of the application for the about page.
 */
@WebServlet(name = "AboutController", urlPatterns = {
    Routes.ABOUT_URI
})
public class AboutController extends HttpServlet {
    private static final String VUE_PATH = "/WEB-INF/vue/about.jsp";
    private static final long serialVersionUID = 1L;

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {
        request.getRequestDispatcher(VUE_PATH).forward(request, response);
    }
}
