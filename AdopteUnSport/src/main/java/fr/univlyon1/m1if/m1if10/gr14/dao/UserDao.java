package fr.univlyon1.m1if.m1if10.gr14.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import fr.univlyon1.m1if.m1if10.gr14.model.User;

/**
 * Class for queries related to User.
 */
public class UserDao {
    private final EntityManager em;

    /**
    * Constructor of UserDAO.
    * @param em EntityManager that manage the entity of the DB.
    */
    public UserDao(final EntityManager em) {
        this.em = em;
    }

    /**
     * Create a user. Does nothing if the user already exists.
     * @param email Email of user
     * @param username Username of user
     * @param password Password of user
     * @return The created user
     */
    public User create(final String email, final String username,  final String password) {
        return create(new User(email, username, password));
    }

    /**
    * Create a user. Does nothing if the user already exists.
    * @param user The user you want to create
    * @return The created user
    */
    public User create(final User user) {
        if (getUserByEmail(user.getEmail()) != null) {
            return null;
        }

        em.persist(user);

        return user;
    }

    /**
    * Get the user by the username passed in the parameter, it it exists.
    * @param username Username of the searched user.
    * @return the user searched by the username, or null
    */
    public User getUserByUsername(final String username) {
        List<User> res = em.createNamedQuery("User.getUserByUsername", User.class)
            .setParameter("username", username)
            .getResultList();

        //Username must be unique so the result will be a list of 1 or 0 element
        //We check if the query found a result, if not we return null
        return (res.size() > 0) ? res.get(0) : null;
    }

    /**
    * Get the user by the email passed in the parameter.
    * Email comparison is made in lowercases.
    * @param email the email of an user.
    * @return the user that we search by the email.
    */
    public User getUserByEmail(final String email) {
        return em.find(User.class, email.toLowerCase());
    }

    /**
     * Get all users.
     * @return List of users
     */
    public List<User> getAllUsers() {
        return em.createNamedQuery("User.getAllUsers", User.class).getResultList();
    }

    /**
    * Change the password of an user.
    * @param user the user who want to change.
    * @param newPassword the new password.
    * @return a bool that confirm the changement.
    */
    public boolean changePasswordOf(final User user, final String newPassword) {
        try {
            User us = em.find(User.class, user.getEmail());
            us.setPassword(newPassword);
            return true;
        } catch (Exception e) {
            Logger.getLogger(UserDao.class.getName()).log(
                Level.WARNING, "While changing password: ", e);
            return false;
        }
    }

}
