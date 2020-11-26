package fr.univlyon1.m1if.m1if10.gr14.dao;

import fr.univlyon1.m1if.m1if10.gr14.model.Sport;

import java.util.List;

import javax.persistence.EntityManager;

/**
 * Class for queries related to Sport.
 */
public class SportDao {

    private final EntityManager em;

    /**
     * Constructor of SportDAO.
     * @param em Takes an EntityManager for queries
     */
    public SportDao(final EntityManager em) {
        this.em = em;
    }

    /**
     * Get all existing sports.
     * @return A list of all existing sports
     */
    public List<Sport> getAllSports() {
        return em.createNamedQuery("Sport.getAllSports", Sport.class).getResultList();
    }

    /**
     * Get a sport by a specific name.
     * @param name
     * @return The sport with the given name, return null if not found
     */
    public Sport getSportByName(final String name) {

        if (name == null) {
            return null;
        }
        return em.find(Sport.class, name);
    }

    /**
     * Create a sport. Does nothing if sport already exists.
     * @param name The name of the sport
     * @return The created sport
     */
    public Sport getOrCreate(final String name) {

        Sport tmp = getSportByName(name);

        if (tmp == null) {
            tmp = new Sport(name);
            em.persist(tmp);
        }

        return tmp;
    }

    /**
     * Tries to insert the given sport in the database.
     * @param sport the sport to insert
     * @return <code>true</code> if the sport has been inserted successfully,
     *         <code>false</code> otherwise (sport of same name already exists).
     */
    public boolean create(final Sport sport) {
        Sport tmp = getSportByName(sport.getName());

        if (tmp != null) {
            return false;
        }

        em.persist(sport);
        return true;
    }
}
