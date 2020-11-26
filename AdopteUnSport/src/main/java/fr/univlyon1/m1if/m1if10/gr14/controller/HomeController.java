package fr.univlyon1.m1if.m1if10.gr14.controller;

import fr.univlyon1.m1if.m1if10.gr14.controller.util.ControllerHelper;
import fr.univlyon1.m1if.m1if10.gr14.dao.EventDao;
import fr.univlyon1.m1if.m1if10.gr14.dto.DtoBuilder;
import fr.univlyon1.m1if.m1if10.gr14.dto.EventDto;
import fr.univlyon1.m1if.m1if10.gr14.model.Event;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.univlyon1.m1if.m1if10.gr14.route.Routes;
import fr.univlyon1.m1if.m1if10.gr14.util.StringValidator;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;


/**
 * Controller of the application for the home page.
 */
@WebServlet(name = "HomeController", urlPatterns = {
    "",
    Routes.HOME_URI
})
public class HomeController extends HttpServlet {
    private static final String VUE_PATH = "/WEB-INF/vue/home.jsp";
    private static final long serialVersionUID = 1L;


    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {
        processEvents(request, response);
        ControllerHelper.addSportList(request);
        request.getRequestDispatcher(VUE_PATH).forward(request, response);
    }


    private void processEvents(
        final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        ServletContext sc = request.getServletContext();
        EventDao eventDao = (EventDao) sc.getAttribute("eventDAO");

        String date = request.getParameter("date");
        String heure = request.getParameter("time");
        String address = request.getParameter("address");
        String sport = request.getParameter("sport");
        String typeEvent = request.getParameter("eventType");
        String owner = request.getParameter("owner");
        String nbMaxPlayers = request.getParameter("nbMax");
        List<Event> events = eventDao.getAvailableEvents();

        //Filter on sport
        if (sport != null && !sport.equals("")) {
            if (!sport.equals("Activité non renseignée")) {
                events = events.stream()
                    .filter(e -> e.getSport().getName().equals(sport))
                    .collect(Collectors.toList());
            }
        }

        //Filter on address
        if (address != null && !address.equals("")) {
            //For each event, we check if all the words from the address passed as filter
            //are present in the event address, splitted as a list of words
            events = events.stream()
                    .filter(e -> Arrays.asList(
                            e.getAddress().replace(",", "")
                            .toLowerCase().split(" ")
                        ).containsAll(Arrays.asList(address.toLowerCase().split(" "))))
                    .collect(Collectors.toList());
        }

        //Filter on date
        if (date != null && !date.equals("")) {
            String heureStr = (heure != null && !heure.equals("")) ? heure : "00:00";
            final Date finalDate = StringValidator.parseDate(date + " " + heureStr);
            if (finalDate != null) {
                events = events.stream()
                    .filter(e -> e.getDate().compareTo(finalDate) >= 0)
                    .collect(Collectors.toList());
            }
            else {
                events.sort((e1, e2) -> e1.getDate().compareTo(e2.getDate()));
            }
        }
        else {
            events.sort((e1, e2) -> e1.getDate().compareTo(e2.getDate()));
        }

        //Filter on event type
        if (typeEvent != null) {
            if (!typeEvent.equals("")) {
                if (!typeEvent.equals("all")) {
                    events = events.stream()
                        .filter(e -> e.getEventType().equals(typeEvent))
                        .collect(Collectors.toList());
                }
            }
        }

        //Filter on maximum number of players
        if (nbMaxPlayers != null
                && !nbMaxPlayers.equals("")) {
            events = events.stream()
                .filter(e -> e.getMaxPlayers()
                    <= Integer.parseInt(request.getParameter("nbMax")))
                .collect(Collectors.toList());
        }

        //Filter on owner username
        if (owner != null && !owner.equals("")) {
            events = events.stream()
                .filter(e -> e.getOwner()
                    .getUsername().toLowerCase()
                    .equals(owner.toLowerCase()))
                .collect(Collectors.toList());
        }


        request.setAttribute("existingEvents", DtoBuilder.buildList(events, EventDto.class));
    }
}
