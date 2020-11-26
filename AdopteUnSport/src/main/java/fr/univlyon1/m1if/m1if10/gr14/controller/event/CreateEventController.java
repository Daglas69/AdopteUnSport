package fr.univlyon1.m1if.m1if10.gr14.controller.event;

import java.io.IOException;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.univlyon1.m1if.m1if10.gr14.controller.util.ControllerHelper;
import fr.univlyon1.m1if.m1if10.gr14.dao.EventDao;
import fr.univlyon1.m1if.m1if10.gr14.dao.SportDao;
import fr.univlyon1.m1if.m1if10.gr14.model.Event;
import fr.univlyon1.m1if.m1if10.gr14.model.Sport;
import fr.univlyon1.m1if.m1if10.gr14.model.User;
import fr.univlyon1.m1if.m1if10.gr14.model.builder.EventBuilder;
import fr.univlyon1.m1if.m1if10.gr14.model.constraint.ConstraintValidator;
import fr.univlyon1.m1if.m1if10.gr14.route.Routes;
import fr.univlyon1.m1if.m1if10.gr14.util.StringValidator;
//import fr.univlyon1.m1if.m1if10.gr14.util.VerifyCaptcha;


/**
 * Controller of the application for the create event page.
 */
@WebServlet(name = "CreateEventController", urlPatterns = {
    Routes.EVENT_CREATE_URI
})
public class CreateEventController extends HttpServlet {
    private static final String VUE_PATH = "/WEB-INF/vue/event/createEvent.jsp";
    private static final long serialVersionUID = 1L;


    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {
        ServletContext sc = request.getServletContext();
        EntityManager em = (EntityManager) sc.getAttribute("EntityManager");
        SportDao sportDao = (SportDao) sc.getAttribute("sportDAO");
        EventDao eventDao = (EventDao) sc.getAttribute("eventDAO");

        //reCAPTCHA validation
        //Not available on the VM
        /*
        boolean valide = VerifyCaptcha.verify(request.getParameter("g-recaptcha-response"));
        if (!valide) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Le Captcha est incorrect.");
            return;
        }*/

        User owner = (User) request.getAttribute("jwtUser");
        String address = request.getParameter("address") + ", " + request.getParameter("city");
        String eventType = request.getParameter("eventType");
        String dateStr = request.getParameter("date");
        String timeStr = request.getParameter("time");
        Date date = StringValidator.parseDate(dateStr + " " + timeStr);
        String contact = request.getParameter("contact");
        String description = request.getParameter("description");

        //Sport type verification
        //Should not be null as the sport name is chosen from a select box
        //Can not use Bean validation for that
        Sport sport = sportDao.getSportByName(request.getParameter("sport"));
        if (sport == null) {
            forwardWithError(request, response,
                "Impossible de créer un évènement : le sport sélectionné est incorrect.");
            return;
        }

        //Max player number verification
        //Can not use Bean validation for that
        String maxPlayersStr = request.getParameter("maxPlayers");
        Integer maxPlayers = StringValidator.stringToInteger(maxPlayersStr);
        if (maxPlayers == null) {
            forwardWithError(request, response,
                "Impossible de créer un évènement : le nombre maximum de joueurs est incorrect.");
            return;
        }

        //Date verification
        //Can not use Bean validation for that
        if (!Event.isValidDate(date)) {
            forwardWithError(request, response,
                "Impossible de créer un évènement : la date indiquée n'est pas valide.");
            return;
        }

        //We create an event with the request parameters
        Event event = new EventBuilder()
            .withOwner(owner)
            .withSport(sport)
            .atAddress(address)
            .ofType(eventType)
            .withMaxPlayers(maxPlayers)
            .atDate(date)
            .withContact(contact)
            .withDescription(description)
            .build();

        //We check event attributes constraints validity
        String errors = ConstraintValidator.getInstance().validate(event);
        if (errors != null) {
            forwardWithError(request, response, "Impossible de créer un évènement : " + errors);
            return;
        }

        em.getTransaction().begin();
        boolean ok = eventDao.create(event);
        em.getTransaction().commit();
        if (!ok) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Impossible de créer un évènement, veuillez réessayer plus tard.");
            return;
        }

        //We add the owner as a player of the Event
        //True if the owner is playing
        boolean ownerPlaying = request.getParameter("isOwnerPlaying") != null ? true : false;
        if (ownerPlaying) {
            em.getTransaction().begin();
            eventDao.addUserToEvent(owner, event);
            em.getTransaction().commit();
        }


        ControllerHelper.addMessage(request, "L'évènement a bien été créé.");
        ControllerHelper.addSportList(request);
        request.getRequestDispatcher(VUE_PATH).forward(request, response);
    }


    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {
        ControllerHelper.addSportList(request);
        request.getRequestDispatcher(VUE_PATH).forward(request, response);
    }


    private void forwardWithError(
        final HttpServletRequest request, final HttpServletResponse response, final String msg)
        throws ServletException, IOException {
        ControllerHelper.addSportList(request);
        ControllerHelper.forwardWithError(request, response, VUE_PATH, msg);
    }
}
