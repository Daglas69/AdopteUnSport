package fr.univlyon1.m1if.m1if10.gr14.controller.event;

import fr.univlyon1.m1if.m1if10.gr14.controller.util.ControllerHelper;
import fr.univlyon1.m1if.m1if10.gr14.dao.EventDao;
import fr.univlyon1.m1if.m1if10.gr14.dao.SportDao;
import fr.univlyon1.m1if.m1if10.gr14.dao.UserDao;
import fr.univlyon1.m1if.m1if10.gr14.dto.DtoBuilder;
import fr.univlyon1.m1if.m1if10.gr14.dto.EventDto;
import fr.univlyon1.m1if.m1if10.gr14.dto.SportDto;
import fr.univlyon1.m1if.m1if10.gr14.model.Event;
import fr.univlyon1.m1if.m1if10.gr14.model.Sport;
import fr.univlyon1.m1if.m1if10.gr14.model.User;
import fr.univlyon1.m1if.m1if10.gr14.model.builder.EventBuilder;
import fr.univlyon1.m1if.m1if10.gr14.model.constraint.ConstraintValidator;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.univlyon1.m1if.m1if10.gr14.route.Routes;
import fr.univlyon1.m1if.m1if10.gr14.util.StringValidator;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;


/**
 * Controller of the application for the modif event page.
 */
@WebServlet(name = "ModifEventController", urlPatterns = {
    Routes.EVENT_MODIF_URI
})
public class ModifEventController extends HttpServlet {
    private static final String VUE_PATH = "/WEB-INF/vue/event/modifEvent.jsp";
    private static final long serialVersionUID = 1L;


    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {

        ServletContext sc = request.getServletContext();

        EntityManager em = (EntityManager) sc.getAttribute("EntityManager");
        UserDao userDao = (UserDao) sc.getAttribute("userDAO");
        EventDao eventDao = (EventDao) sc.getAttribute("eventDAO");
        SportDao sportDao = (SportDao) sc.getAttribute("sportDAO");
        Integer id = StringValidator.stringToInteger(request.getParameter("idEvent"));

        //Event ID verification
        if (id == null) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Impossible de retrouver l'évènement, veuillez réessayer plus tard.");
            return;
        }

        //////////////////////////////////////////////////////////////////////////////
        //Part for the update manager if the user chooses to modify the selected event
        //////////////////////////////////////////////////////////////////////////////

        User user = userDao.getUserByEmail((String) request.getAttribute("jwtUserEmail"));
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String dateStr = request.getParameter("date");
        String timeStr = request.getParameter("time");
        Date date = StringValidator.parseDate(dateStr + " " + timeStr);
        String contact = request.getParameter("contact");
        Sport sport = sportDao.getSportByName(request.getParameter("sport"));
        String eventType = request.getParameter("eventType");
        String maxPlayers = request.getParameter("maxPlayers");
        String description = request.getParameter("description");

        Event eventBuild = new EventBuilder()
            .withOwner(user)
            .withSport(sport)
            .atAddress(address + ", " + city)
            .ofType(eventType)
            .withMaxPlayers(Integer.parseInt(maxPlayers))
            .atDate(date)
            .withContact(contact)
            .withDescription(description)
            .build();

        //Check constraints validity
        String errors = ConstraintValidator.getInstance().validate(eventBuild);
        if (errors != null) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Impossible de modifier l'évènement : " + errors);
            return;
        }

        //Date verification
        //Can not use Bean validation for that
        if (!Event.isValidDate(date)) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Impossible de modifier l'évènement avec la date indiquée.");
            return;
        }

        Event event = eventDao.getEventById(id);
        em.getTransaction().begin();
        boolean ok = eventDao.updateEvents(event, eventBuild);
        em.getTransaction().commit();
        if (!ok) {
            ControllerHelper.addErrorMessage(request,
                "Impossible de modifier l'évènement pour le moment, veuillez réessayer plus tard.");
        }
        else {
            ControllerHelper.addMessage(request, "L'évènement a bien été modifié.");

            //We update the list of players at the end to be sure that the event has been modified
            //We add or remove the owner from the list according to his choice, with a verification
            //on his current state : playing or not
            //True if the owner is playing
            boolean ownerPlaying = request.getParameter("isOwnerPlaying") != null ? true : false;
            boolean currentlyPlaying = event.getPlayers().contains(user);
            em.getTransaction().begin();
            if (ownerPlaying && !currentlyPlaying) {
                eventDao.addUserToEvent(user, event);
            }
            else if (!ownerPlaying && currentlyPlaying) {
                eventDao.removeUserFromEvent(user, event);
            }
            em.getTransaction().commit();
        }

        //To keep displaying the modif page
        doGet(request, response);
    }


    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {

        ServletContext sc = request.getServletContext();

        //////////////////////////////////////////////////////////////////////////////////
        //Part of filling the fields with known attributes of the events and verifications
        //////////////////////////////////////////////////////////////////////////////////

        UserDao userDao = (UserDao) sc.getAttribute("userDAO");
        EventDao eventDao = (EventDao) sc.getAttribute("eventDAO");
        SportDao sportDao = (SportDao) sc.getAttribute("sportDAO");

        //Event ID verification
        Integer id = StringValidator.stringToInteger(request.getParameter("idEvent"));
        if (id == null) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Impossible de retrouver l'évènement, veuillez réessayer plus tard.");
            return;
        }

        //User verification
        //Should not be null as the user must be authenticated to make requests to this servlet
        User user = userDao.getUserByEmail((String) request.getAttribute("jwtUserEmail"));
        if (user == null) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Impossible de modifier l'évènement : vous n'êtes pas connecté.");
            return;
        }

        //Event verification
        Event event = eventDao.getEventById(id);
        if (event == null) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Impossible de retrouver l'évènement, veuillez réessayer plus tard.");
            return;
        }

        //Owner verification
        if (!event.getOwner().equals(user)) {
            ControllerHelper.forwardWithError(request, response, VUE_PATH,
                "Impossible de modifier l'évènement : vous n'êtes pas organisateur.");
            return;
        }

        //Display is different if user is playing
        if (event.getPlayers().contains(user)) {
            request.setAttribute("userIsPlaying", true);
        }

        EventDto eventdto = DtoBuilder.build(event, EventDto.class);
        request.setAttribute("eventdto", eventdto);
        request.setAttribute("sports",
        DtoBuilder.buildList(sportDao.getAllSports(), SportDto.class));

        request.getRequestDispatcher(VUE_PATH).forward(request, response);
    }
}
