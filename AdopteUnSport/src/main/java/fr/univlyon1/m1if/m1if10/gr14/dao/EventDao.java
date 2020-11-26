package fr.univlyon1.m1if.m1if10.gr14.dao;

import fr.univlyon1.m1if.m1if10.gr14.model.Event;
import fr.univlyon1.m1if.m1if10.gr14.model.Sport;
import fr.univlyon1.m1if.m1if10.gr14.model.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;


/**
 * Class for queries related to Event.
 */
public class EventDao {
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_EVENT_FULL = 1;
    public static final int CODE_EVENT_CANCEL = 2;
    public static final int CODE_INCORRECT_PARAM = -1;
    public static final int CODE_USER_NOT_PARTICIPATING = 3;

    private final EntityManager em;


    /**
     * Constructor of EventDAO.
     * @param em EntityManager for queries
     */
    public EventDao(final EntityManager em) {
        this.em = em;
    }



    /**
     * Create an event and return the same event.
     * Return null if it already exists.
     * @param event The event you want to create
     * @return The created event or null
     */
    public boolean create(final Event event) {

        Event tmp = getEventById(event.getIdEvent());

        if (tmp != null) {
            return false;
        }

        else {
            em.persist(event);
            return true;
        }
    }


    /**
     * Get an event with a specific id.
     * @param idevent Id of the event
     * @return The corresponding event if exists, return null if not found
     */
    public Event getEventById(final int idevent) {
        return em.find(Event.class, idevent);
    }


    /**
     * Get all users from a given event.
     * @param event The event you're looking for
     * @return A list of users participating to the given event.
     */
    public List<User> getPlayersByEvent(final Event event) {
        Event tmp = em.find(Event.class, event.getIdEvent());

        if (tmp != null) {
            return tmp.getPlayers();
        }

        return null;
    }


    /**
     * Add a user to an event.
     * @param user The user you want to add
     * @param event The event you're looking for
     * @return Int code from public constant class
     */
    public int addUserToEvent(final User user, final Event event) {

        if (em.find(User.class, user.getEmail()) == null) {
            return EventDao.CODE_INCORRECT_PARAM;
        }
        if (em.find(Event.class, event.getIdEvent()) == null) {
            return EventDao.CODE_INCORRECT_PARAM;
        }
        if (event.getCancelled()) {
            return EventDao.CODE_EVENT_CANCEL;
        }

        if (event.getCurrentPlayers() < event.getMaxPlayers()) {

            event.setCurrentPlayers(event.getCurrentPlayers() + 1);
            event.getPlayers().add(user);

            return EventDao.CODE_SUCCESS;
        }
        else {
            return EventDao.CODE_EVENT_FULL;
        }
    }


    /**
     * Remove an user from an event.
     * @param user The user you want to remove
     * @param event The event you're looking for
     * @return Int code from public constant class
     */
    public int removeUserFromEvent(final User user, final Event event) {
        if (em.find(User.class, user.getEmail()) == null) {
            return EventDao.CODE_INCORRECT_PARAM;
        }
        if (em.find(Event.class, event.getIdEvent()) == null) {
            return EventDao.CODE_INCORRECT_PARAM;
        }
        for (User u : event.getPlayers()) {
            if (u.equals(user)) {
                event.setCurrentPlayers(event.getCurrentPlayers() - 1);
                event.getPlayers().remove(user);
                return EventDao.CODE_SUCCESS;
            }
        }
        return EventDao.CODE_USER_NOT_PARTICIPATING;
    }


    /**
     * Cancel an event.
     * @param event The event you want to cancel
     * @return True if it has been cancelled, false if it has not been found
     */
    public boolean cancelEvent(final Event event) {
        if (em.find(Event.class, event.getIdEvent()) == null) {
            return false;
        }
        else {
            event.cancel();
            return true;
        }
    }



    /* ****************************************************** */
    /* ***************** CURRENT EVENTS ********************* */
    /* ****************************************************** */

    /**
     * Get current events, even the full ones. (The event is not past)
     * @return List of all current events
     */
    public List<Event> getCurrentEvents() {
        return em.createNamedQuery("Event.getCurrentEvents", Event.class)
            .getResultList();
    }

    /**
     * Get current events, even full ones. (The event is not past)
     * @param limit The number of elements
     * @param page The page you want
     * @return List of all current events, with the desired size
     */
    public List<Event> getCurrentEvents(final int limit, final int page) {
        return em.createNamedQuery("Event.getCurrentEvents", Event.class)
            .setFirstResult((page - 1) * limit)
            .setMaxResults(limit)
            .getResultList();
    }



    /* ****************************************************** */
    /* **************** AVAILABLE EVENTS ******************** */
    /* ****************************************************** */

    /**
     * Get available events. (The event date is not past and the event is not full or cancelled)
     * @return List of all joinable events
     */
    public List<Event> getAvailableEvents() {
        return em.createNamedQuery("Event.getAvailableEvents", Event.class)
            .getResultList();
    }

    /**
     * Get available events. (The event date is not past and the event is not full or cancelled)
     * @param limit The number of elements
     * @param page The page you want
     * @return List of all joinable events, with the desired size
     */
    public List<Event> getAvailableEvents(final int limit, final int page) {
        return em.createNamedQuery("Event.getAvailableEvents", Event.class)
            .setFirstResult((page - 1) * limit)
            .setMaxResults(limit)
            .getResultList();
    }

    /**
     * Get available events with a specific owner.
     * (The event date is not past and the event is not full or cancelled)
     * @param owner The user that own the event
     * @return A list of all available events by the given owner
     */
    public List<Event> getAvailableEventsByOwner(final User owner) {
        return em.createNamedQuery("Event.getAvailableEventsByOwner", Event.class)
            .setParameter("ownerEmail", owner.getEmail())
            .getResultList();
    }

    /**
     * Get available events with a specific sport.
     * (The event date is not past and the event is not full or cancelled)
     * @param sport The specific sport you're looking for
     * @return A list of all available events by the given sport
     */
    public List<Event> getAvailableEventsBySport(final Sport sport) {
        return em.createNamedQuery("Event.getAvailableEventsBySport", Event.class)
            .setParameter("sportName", sport.getName())
            .getResultList();
    }



    /* ****************************************************** */
    /* ******************* ALL EVENTS *********************** */
    /* ****************************************************** */

    /**
     * Get events of a specific user. (old and current events)
     * @param user The user that participate to the event
     * @return A list of all events where the user participate
     */
    public List<Event> getAllEventsByUser(final User user) {
        return em.createNamedQuery("Event.getAllEventsByUser", Event.class)
            .setParameter("userEmail", user.getEmail())
            .getResultList();
    }

    /**
     * Get events of a specific owner. (old and current events)
     * @param owner The user that owns the event
     * @return A list of all events where the user is owner
     */
    public List<Event> getAllOwnedEventsByUser(final User owner) {
        return em.createNamedQuery("Event.getAllOwnedEventsByUser", Event.class)
            .setParameter("ownerEmail", owner.getEmail())
            .getResultList();
    }



    /* ****************************************************** */
    /* ******************* OLD EVENTS *********************** */
    /* ****************************************************** */

    /**
     * Get old events with a specific owner. (The event date is past)
     * @param owner The user that own the event
     * @return A list of all old events by the given owner
     */
    public List<Event> getOldOwnedEventsByUser(final User owner) {
        return em.createNamedQuery("Event.getOldOwnedEventsByUser", Event.class)
            .setParameter("ownerEmail", owner.getEmail())
            .getResultList();
    }

    /**
     * Get old events with a specific owner. (The event date is past)
     * @param owner The user that own the event
     * @param limit The number of elements
     * @param page The page you want
     * @return A list of all old events by the given owner, with the desired size
     */
    public List<Event> getOldOwnedEventsByUser(final User owner, final int limit, final int page) {
        return em.createNamedQuery("Event.getOldOwnedEventsByUser", Event.class)
            .setParameter("ownerEmail", owner.getEmail())
            .setFirstResult((page - 1) * limit)
            .setMaxResults(limit)
            .getResultList();
    }

    /**
     * Get old played event by a specific user. (The event date is past)
     * @param user The user you're looking for
     * @return A list of all old events played by the given user
     */
    public List<Event> getOldPlayedEventsByUser(final User user) {
        return em.createNamedQuery("Event.getOldPlayedEventsByUser", Event.class)
            .setParameter("userEmail", user.getEmail())
            .getResultList();
    }

    /**
     * Get old played event by a specific user. (The event date is past)
     * @param user The user you're looking for
     * @param limit The number of elements
     * @param page The page you want
     * @return A list of old events played by the given user, with the desired size
     */
    public List<Event> getOldPlayedEventsByUser(final User user, final int limit, final int page) {
        return em.createNamedQuery("Event.getOldPlayedEventsByUser", Event.class)
            .setParameter("userEmail", user.getEmail())
            .setFirstResult((page - 1) * limit)
            .setMaxResults(limit)
            .getResultList();
    }


    /**
    * Update the infos of an event.
    * @param event the event who want to change
    * @param newEvent the new event that event copy
    * @return a bool that confirm the changement
    */
    public boolean updateEvents(final Event event, final Event newEvent) {
        try {
            event.setSport(newEvent.getSport());
            event.setAddress(newEvent.getAddress());
            event.setEventType(newEvent.getEventType());
            event.setMaxPlayers(newEvent.getMaxPlayers());
            event.setDate(newEvent.getDate());
            event.setContact(newEvent.getContact());
            event.setDescription(newEvent.getDescription());
            return true;
        } catch (Exception e) {
            Logger.getLogger(EventDao.class.getName()).log(
                Level.WARNING, "Updating event: ", e
            );
            return false;
        }
    }
}
