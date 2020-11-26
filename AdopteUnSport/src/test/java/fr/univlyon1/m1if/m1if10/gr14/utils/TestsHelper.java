package fr.univlyon1.m1if.m1if10.gr14.utils;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import fr.univlyon1.m1if.m1if10.gr14.model.Event;
import fr.univlyon1.m1if.m1if10.gr14.model.Sport;
import fr.univlyon1.m1if.m1if10.gr14.model.User;

/**
 * Helper class storing several utilitary functions that should only be used in tests.
 * ?? is it really useful for it to be static?
 * ?? => singleton keeping the EntityManager instance unique
 */
public final class TestsHelper {
    private static EntityManager emInstance = null;

    private TestsHelper() { }

    /**
     * Returns an EntityManager instance using the tests database.
     * @return a tests-specific EntityManager
     */
    public static EntityManager getEntityManager() {
        if (emInstance == null) {
            emInstance = Persistence.createEntityManagerFactory("AdopteUnSportTest")
                                    .createEntityManager();
        }
        return emInstance;
    }



    /* ### UTILS ### */

    /**
     * Tries to persist all given objects with the given EntityManager (handles transaction).
     * Assumes each is an Entity not already persisted. Does keep the order of insertion.
     * @param em the EntityManager to use to persist
     * @param entities list of the entities to persist at once
     * @param <T> MUST be an Entity-annotated type
     */
    public static <T> void persistList(final EntityManager em, final List<T> entities) {
        for (Object o : entities) {
            persist(em, o);
        }
    }

    /**
     * Tries to persist all given objects with the given EntityManager (handles transaction).
     * Assumes each is an Entity not already persisted. Does keep the order of insertion.
     * @param em the EntityManager to use to persist
     * @param entities all the entities to persist at once,
     *                 each of them MUST be of an Entity-annotated type
     */
    public static void persistAll(final EntityManager em, final Object... entities) {
        for (Object o : entities) {
            persist(em, o);
        }
    }

    /**
     * Tries to persist the given entity immediately (handles transaction).
     * Assumes the entity is is not already persisted and of an Entity-annotated type.
     * @param em the EntityManager to use to persist
     * @param entity the entity to persist; MUST be of an Entity-annotated type
     */
    public static void persist(final EntityManager em, final Object entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }

    /**
     * Clears <strong>every persisted data</strong> from the database.
     * @param em the EntityManager to use
     */
    public static void clearDatabase(final EntityManager em) {
        clearEvents(em);
        clearUsers(em);
        clearSports(em);
    }

    /**
     * Clears every event persisted in the database.
     * @param em the EntityManager to use
     */
    public static void clearEvents(final EntityManager em) {
        List<Event> events = em.createNamedQuery("Event.getAllEvents", Event.class)
                               .getResultList();
        em.getTransaction().begin();
        for (Event e : events) {
            em.remove(e);
        }
        em.getTransaction().commit();
    }

    /**
     * Clears every user persisted in the database.
     * @param em the EntityManager to use
     */
    public static void clearUsers(final EntityManager em) {
        List<User> users = em.createNamedQuery("User.getAllUsers", User.class)
                               .getResultList();
        em.getTransaction().begin();
        for (User u : users) {
            em.remove(u);
        }
        em.getTransaction().commit();
    }

    /**
     * Clears every sport persisted in the database.
     * @param em the EntityManager to use
     */
    public static void clearSports(final EntityManager em) {
        List<Sport> sports = em.createNamedQuery("Sport.getAllSports", Sport.class)
                               .getResultList();
        em.getTransaction().begin();
        for (Sport s : sports) {
            em.remove(s);
        }
        em.getTransaction().commit();
    }
}
