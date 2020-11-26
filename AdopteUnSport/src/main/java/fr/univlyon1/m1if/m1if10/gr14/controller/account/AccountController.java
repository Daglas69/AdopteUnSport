package fr.univlyon1.m1if.m1if10.gr14.controller.account;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.univlyon1.m1if.m1if10.gr14.controller.util.ControllerHelper;
import fr.univlyon1.m1if.m1if10.gr14.dao.EventDao;
import fr.univlyon1.m1if.m1if10.gr14.dao.UserDao;
import fr.univlyon1.m1if.m1if10.gr14.dto.DtoBuilder;
import fr.univlyon1.m1if.m1if10.gr14.dto.EventDto;
import fr.univlyon1.m1if.m1if10.gr14.model.Event;
import fr.univlyon1.m1if.m1if10.gr14.model.User;
import fr.univlyon1.m1if.m1if10.gr14.route.Routes;
import fr.univlyon1.m1if.m1if10.gr14.util.PasswordHelper;


/**
 * Controller of the application for the account pages.
 */
@WebServlet(name = "AccountController", urlPatterns = {
    Routes.ACCOUNT_RES + "/*"
})
public class AccountController extends HttpServlet {
    private static final String VUE_PATH = "/WEB-INF/vue/user/account.jsp";
    private static final long serialVersionUID = 1L;


    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {

        //Modify password action
        String param = request.getParameter("submitBtn");
        if (param != null && param.equals("changePassword")) {
            changePassword(request, response);
        }

        request.getRequestDispatcher(VUE_PATH).forward(request, response);
    }


    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {
        if (request.getParameter("page") != null) {

            switch (request.getParameter("page")) {

                case "Evenements":
                    processEvents(request, response);
                    break;

                default:
                    break;
            }
        }

        request.getRequestDispatcher(VUE_PATH).forward(request, response);
    }


    private void changePassword(
        final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        ServletContext sc = request.getServletContext();
        EntityManager em = (EntityManager) sc.getAttribute("EntityManager");
        UserDao userDao = (UserDao) sc.getAttribute("userDAO");
        User user = (User) request.getAttribute("jwtUser");

        String password = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        //Input asserts
        if (password == null || newPassword == null || confirmPassword == null)  {
            ControllerHelper.addErrorMessage(request, "Veuillez remplir tous les champs.");
        }
        else if (!PasswordHelper.checkPassword(password, user.getPassword())) {
            ControllerHelper.addErrorMessage(request, "Le mot de passe actuel est incorrect.");
        }
        else if (!newPassword.equals(confirmPassword)) {
            ControllerHelper.addErrorMessage(request, "Les mots de passe ne sont pas indentiques.");
        }
        else {
            em.getTransaction().begin();
            boolean ok = userDao.changePasswordOf(user, PasswordHelper.hashPassword(newPassword));
            em.getTransaction().commit();
            if (!ok) {
                ControllerHelper.addErrorMessage(request,
                    "Impossible de modifier votre mot de passe pour le moment, "
                    + "veuillez réessayer plus tard.");
            }
            else {
                ControllerHelper.addMessage(request, "Votre mot de passe a bien été modifié.");
            }
        }
    }


    private void processEvents(
        final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        ServletContext sc = request.getServletContext();
        EventDao eventDao = (EventDao) sc.getAttribute("eventDAO");
        User user = (User) request.getAttribute("jwtUser");

        List<Event> events = null;

        //It would be good to use State design pattern
        boolean showOldEvents = request.getParameter("old") != null ? true : false;
        String displayType = request.getParameter("afficher");
        String sortDate = request.getParameter("triDate");

        //Event display type
        if (displayType != null && displayType.equals("player")) {
            events = eventDao.getAllEventsByUser(user);
        }
        else if (displayType != null && displayType.equals("owner")) {
            events = eventDao.getAllOwnedEventsByUser(user);
        }
        else {
            events = eventDao.getAllEventsByUser(user);
            events.addAll(eventDao.getAllOwnedEventsByUser(user));
            //Remove duplicates
            events = events.stream().distinct().collect(Collectors.toList());
        }

        //Old events
        if (!showOldEvents) {
            events = events.stream().filter(e -> !e.isTerminated())
                .collect(Collectors.toList());
        }

        //Date sorting
        if (sortDate != null && sortDate.equals("croissant")) {
            events.sort((e1, e2) -> e1.getDate().compareTo(e2.getDate()));
        }
        else {
            events.sort((e1, e2) -> e2.getDate().compareTo(e1.getDate()));
        }


        request.setAttribute("myEvents", DtoBuilder.buildList(events, EventDto.class));
    }
}
