package fr.univlyon1.m1if.m1if10.gr14.controller.util;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.univlyon1.m1if.m1if10.gr14.dao.SportDao;
import fr.univlyon1.m1if.m1if10.gr14.dto.DtoBuilder;
import fr.univlyon1.m1if.m1if10.gr14.dto.SportDto;
import fr.univlyon1.m1if.m1if10.gr14.util.CookieHelper;


/**
 * utility class containing static methods used in controllers.
 */
public final class ControllerHelper {
    //constants to add a type of message in request attributes
    public static final int SUCCESS_MSG = 1;
    public static final int ERROR_MSG = 0;


    /**
     * Sends an extern redirection (in the same domain)
     * to the route with the URI passed as argument.
     * Adds token cookie to the response.
     * @param request Current request
     * @param response Current response
     * @param uri URI of the redirection
     * @throws ServletException
     * @throws IOException
     */
    public static void sendRedirect(
        final HttpServletRequest request, final HttpServletResponse response,
        final String uri) throws ServletException, IOException {
            //We add cookie in response for localhost redirects
            //Does not affect VM
            response.addCookie(CookieHelper.getTokenCookie(request));
            response.sendRedirect(request.getContextPath() + uri);
    }


    /**
     * Stores a success message in a request attribute.
     * @param request Request storing the attribute
     * @param msg Message to store
     * @throws ServletException
     * @throws IOException
     */
    public static void addMessage(final HttpServletRequest request, final String msg)
        throws ServletException, IOException {
        addMessage(request, msg, SUCCESS_MSG);
    }


    /**
     * Stores an error message in a request attribute.
     * @param request Request storing the attribute
     * @param msg Message to store
     * @throws ServletException
     * @throws IOException
     */
    public static void addErrorMessage(final HttpServletRequest request, final String msg)
        throws ServletException, IOException {
        addMessage(request, msg, ERROR_MSG);
    }


    /**
     * Stores a message in a request attribute, according to the code passed as argument.
     * @param request Request storing the attribute
     * @param msg Message to store
     * @param code Code indicating the type of message
     * @throws ServletException
     * @throws IOException
     */
    public static void addMessage(
        final HttpServletRequest request, final String msg, final int code)
        throws ServletException, IOException {
        switch (code) {
            case ERROR_MSG :
                request.setAttribute("errorMsg", msg);
                break;
            case SUCCESS_MSG :
                request.setAttribute("successMsg", msg);
                break;
            default:
                break;
        }
    }


    /**
     * Add an error message in a request attribute, then forward it to the path passed as argument.
     * @param request Current request
     * @param response Current response
     * @param path Path of the dispatcher
     * @param msg Message to add
     * @throws ServletException
     * @throws IOException
     */
    public static void forwardWithError(
        final HttpServletRequest request, final HttpServletResponse response,
        final String path, final String msg) throws ServletException, IOException {
        addErrorMessage(request, msg);
        request.getRequestDispatcher(path).forward(request, response);
    }


    /**
     * Adds the list of sports in a request attribute.
     * @param request Request storing the attribute
     * @throws ServletException
     * @throws IOException
     */
    public static void addSportList(final HttpServletRequest request)
        throws ServletException, IOException {
        ServletContext sc = request.getServletContext();
        SportDao sportDao = (SportDao) sc.getAttribute("sportDAO");

        request.setAttribute("sports",
            DtoBuilder.buildList(sportDao.getAllSports(), SportDto.class));
    }



    //Final class
    private ControllerHelper() { }
}
