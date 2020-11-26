package fr.univlyon1.m1if.m1if10.gr14.servlet;

import fr.univlyon1.m1if.m1if10.gr14.dao.EventDao;
import fr.univlyon1.m1if.m1if10.gr14.dao.SportDao;
import fr.univlyon1.m1if.m1if10.gr14.dao.UserDao;
import fr.univlyon1.m1if.m1if10.gr14.route.Routes;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;


/**
 * Servlet called at startup to init the application with persistence and context.
 */
@WebServlet(name = "Init", urlPatterns = Routes.INIT_URI, loadOnStartup = 1)
public class Init extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private EntityManager em;


    @Override
    public void init(final ServletConfig sc) throws ServletException {
        super.init(sc);

        em = Persistence.createEntityManagerFactory("AdopteUnSport").createEntityManager();

        sc.getServletContext().setAttribute("EntityManager", em);
        sc.getServletContext().setAttribute("eventDAO", new EventDao(this.em));
        sc.getServletContext().setAttribute("sportDAO", new SportDao(this.em));
        sc.getServletContext().setAttribute("userDAO", new UserDao(this.em));
    }
}
