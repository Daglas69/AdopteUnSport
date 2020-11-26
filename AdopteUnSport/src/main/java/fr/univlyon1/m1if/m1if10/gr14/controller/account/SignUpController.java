package fr.univlyon1.m1if.m1if10.gr14.controller.account;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.univlyon1.m1if.m1if10.gr14.controller.util.ControllerHelper;
import fr.univlyon1.m1if.m1if10.gr14.dao.UserDao;
import fr.univlyon1.m1if.m1if10.gr14.model.User;
import fr.univlyon1.m1if.m1if10.gr14.model.constraint.ConstraintValidator;
import fr.univlyon1.m1if.m1if10.gr14.route.Routes;
import fr.univlyon1.m1if.m1if10.gr14.util.CookieHelper;
import fr.univlyon1.m1if.m1if10.gr14.util.PasswordHelper;
import fr.univlyon1.m1if.m1if10.gr14.util.JwtHelper;
//import fr.univlyon1.m1if.m1if10.gr14.util.VerifyCaptcha;


/**
 * Controller of the application for the sign up page.
 */
@WebServlet(name = "SignUpController", urlPatterns = {
    Routes.SIGN_UP_URI
})
public class SignUpController extends HttpServlet {
    private static final String VUE_PATH = "/WEB-INF/vue/user/signup.jsp";
    private static final long serialVersionUID = 1L;


    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {
        ServletContext sc = request.getServletContext();
        EntityManager em = (EntityManager) sc.getAttribute("EntityManager");
        UserDao userDao = (UserDao) sc.getAttribute("userDAO");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");

        //reCAPTCHA validation
        //Not available on the VM
        /*
        boolean valide = VerifyCaptcha.verify(request.getParameter("g-recaptcha-response"));
        if (!valide) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Le Captcha est incorrect.");
            return;
        }*/

        //Email verification
        if (userDao.getUserByEmail(email) != null) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Un compte existe déjà avec cet email.");
            return;
        }

        //Username verification
        if (userDao.getUserByUsername(username) != null) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Un compte existe déjà avec déjà cet username.");
            return;
        }

        //Password verification
        //We can not check Password strength in the entity constraints validation
        //as it will be hashed before that the user object is created
        if (!PasswordHelper.checkPasswordStrength(password)) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Le mot de passe doit contenir entre 5 et 30 caractères, dont un chiffre, une "
                + "lettre minuscule et majuscule, et un caractère spécial : @ # $ % . ^ & + =");
            return;
        }
        if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Les mots de passe ne sont pas indentiques.");
            return;
        }

        User user = new User(email, username, PasswordHelper.hashPassword(password));

        //Check constraints validity
        String errors = ConstraintValidator.getInstance().validate(user);
        if (errors != null) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH, errors);
            return;
        }
        else {
            em.getTransaction().begin();
            userDao.create(user);
            em.getTransaction().commit();

            //We create a JWT and store it in a cookie
            String token = JwtHelper.generateToken(user.getEmail(), request);
            response.addCookie(CookieHelper.createTokenCookie(token));
            response.sendRedirect(request.getContextPath() + Routes.ACCOUNT_DETAILS_URI);
            return;
        }
    }


    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {
        request.getRequestDispatcher(VUE_PATH).forward(request, response);
    }
}
