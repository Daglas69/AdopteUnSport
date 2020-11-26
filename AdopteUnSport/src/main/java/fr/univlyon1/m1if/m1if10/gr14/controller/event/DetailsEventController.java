package fr.univlyon1.m1if.m1if10.gr14.controller.event;

import fr.univlyon1.m1if.m1if10.gr14.controller.util.ControllerHelper;
import fr.univlyon1.m1if.m1if10.gr14.dao.EventDao;
import fr.univlyon1.m1if.m1if10.gr14.dto.DtoBuilder;
import fr.univlyon1.m1if.m1if10.gr14.dto.EventDto;
import fr.univlyon1.m1if.m1if10.gr14.model.Event;
import fr.univlyon1.m1if.m1if10.gr14.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.univlyon1.m1if.m1if10.gr14.route.Routes;
import fr.univlyon1.m1if.m1if10.gr14.util.StringValidator;
import javax.servlet.ServletContext;


/**
 * Controller of the application for the detail event page.
 */
@WebServlet(name = "DetailsEventController", urlPatterns = {
Routes.EVENT_DETAILS_URI
})
public class DetailsEventController extends HttpServlet {
    private static final String VUE_PATH = "/WEB-INF/vue/event/detailsEvent.jsp";
    private static final long serialVersionUID = 1L;


    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException, IOException {
        ServletContext sc = request.getServletContext();
        EventDao eventDao = (EventDao) sc.getAttribute("eventDAO");
        Integer id = StringValidator.stringToInteger(request.getParameter("idEvent"));

        //Event ID verification
        if (id == null) {
            ControllerHelper.addErrorMessage(request,
                "Impossible de retrouver l'évènement, veuillez réessayer plus tard.");
        }
        else {
            //Event verification
            Event event = eventDao.getEventById(id);
            if (event == null) {
                ControllerHelper.addErrorMessage(request,
                    "Impossible de retrouver l'évènement, veuillez réessayer plus tard.");
            }
            else {
                //To display the correct buttons according to user role
                User user = (User) request.getAttribute("jwtUser");
                if (event.getOwner().equals(user)) {
                    request.setAttribute("currentUserOwner", true);
                }
                else if (event.getPlayers().contains(user)) {
                    request.setAttribute("currentUserPlayer", true);
                }

                EventDto eventdto = DtoBuilder.build(event, EventDto.class);
                request.setAttribute("eventdto", eventdto);
            }

        }

        request.getRequestDispatcher(VUE_PATH).forward(request, response);
    }
}
