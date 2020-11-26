package fr.univlyon1.m1if.m1if10.gr14.controller.account;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.univlyon1.m1if.m1if10.gr14.controller.util.ControllerHelper;
import fr.univlyon1.m1if.m1if10.gr14.dao.UserDao;
import fr.univlyon1.m1if.m1if10.gr14.model.User;
import fr.univlyon1.m1if.m1if10.gr14.route.Routes;
import fr.univlyon1.m1if.m1if10.gr14.util.CookieHelper;
import fr.univlyon1.m1if.m1if10.gr14.util.PasswordHelper;
import fr.univlyon1.m1if.m1if10.gr14.util.JwtHelper;


/**
 * Controller of the application for the sign in page.
 */
@WebServlet(name = "SignInController", urlPatterns = {
    Routes.SIGN_IN_URI
})
public class SignInController extends HttpServlet {
    private static final String VUE_PATH = "/WEB-INF/vue/user/signin.jsp";
    private static final long serialVersionUID = 1L;


    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null) {
            ControllerHelper.forwardWithError(
                request, response, VUE_PATH, "Veuillez remplir tous les champs.");
        }
        else {
            ServletContext sc = request.getServletContext();
            UserDao userDao = (UserDao) sc.getAttribute("userDAO");
            User user = userDao.getUserByEmail(email);
            if (user == null) {
                ControllerHelper.forwardWithError(
                    request, response, VUE_PATH, "Aucun compte ne correspond à cet email.");
            }
            else {
                if (!PasswordHelper.checkPassword(password, user.getPassword())) {
                    ControllerHelper.forwardWithError(
                        request, response, VUE_PATH, "Votre mot de passe est incorrect.");
                }
                else {
                    //We create a JWT and store it in a cookie
                    String token = JwtHelper.generateToken(email, request);
                    response.addCookie(CookieHelper.createTokenCookie(token));
                    response.sendRedirect(request.getContextPath() + Routes.ACCOUNT_DETAILS_URI);
                }
            }
        }
    }


    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {
        request.getRequestDispatcher(VUE_PATH).forward(request, response);
    }
}
