package fr.univlyon1.m1if.m1if10.gr14.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Test;

import fr.univlyon1.m1if.m1if10.gr14.model.User;
import fr.univlyon1.m1if.m1if10.gr14.utils.TestsHelper;

public class UserDaoTest {
    /* ### STATIC FIELDS ### */

    private static final EntityManager EM = TestsHelper.getEntityManager();

    private static final UserDao DAO = new UserDao(EM);

    private static final User USER_1 = new User("bidonx@gmail.com", "bidonx", "b1d0n");
    private static final User USER_2 = new User("bidulex@gmail.com", "bidulex", "b1dul3");



    /* ### SETUP ### */

    /**
     * Empties the 'users' table.
     */
    @After
    public void clean() {
        TestsHelper.clearUsers(EM);
    }



    /* ### TESTS ### */

    /**
     * Tests the wrapper version of the <code>UserDao#create</code> method.
     * (basically tests both versions at once)
     */
    @Test
    public void testCreate() {
        TestsHelper.persist(EM, USER_1);

        final int nbUsersBefore = DAO.getAllUsers().size();

        EM.getTransaction().begin();
        //Already created, returns null
        assertThat(
            DAO.create(USER_1),
            is((User) null)
        );

        //Does not exist yet, is inserted
        assertThat(
            DAO.create(USER_2),
            is(USER_2)
        );
        EM.getTransaction().commit();

        //Only one new entry
        assertThat(
            DAO.getAllUsers().size(),
            is(nbUsersBefore + 1)
        );
    }

    /**
     * Test of the <code>UserDao#getAllUsers</code> method.
     */
    @Test
    public void testGetAllUsers() {
        TestsHelper.persistAll(EM, USER_1, USER_2);

        assertThat(
            DAO.getAllUsers(),
            is(Arrays.asList(USER_1, USER_2))
        );
    }

    /**
     * Test of the <code>UserDao#getUserByEmail</code> method.
     */
    @Test
    public void testGetUserByEmail() {

        //Is created, is found
        TestsHelper.persist(EM, USER_1);
        assertThat(
            DAO.getUserByEmail(USER_1.getEmail()),
            is(USER_1)
        );

        //Does not exist, returns null
        assertThat(
            DAO.getUserByEmail(USER_2.getEmail()),
            is((User) null)
        );
    }

    /**
     * Test of the <code>UserDao#getUserByUsername</code> method.
     */
    @Test
    public void testGetUserByUsername() {
        //Is created, is found
        TestsHelper.persist(EM, USER_1);
        assertThat(
            DAO.getUserByUsername(USER_1.getUsername()),
            is(USER_1)
        );

        //Does not exist, returns null
        assertThat(
            DAO.getUserByUsername(USER_2.getUsername()),
            is((User) null)
        );
    }

    /**
     * Test of the <code>UserDao#changePasswordOf</code> method.
     */
    @Test
    public void testChangePasswordOf() {
        //Copy the USER_1 so it does not get changed and we can check the change.
        final User actual = new User(
            USER_1.getEmail(), USER_1.getUsername(), USER_1.getPassword()
        );
        final String newPassword = "someHash";

        EM.getTransaction().begin();
        EM.persist(actual);
        DAO.changePasswordOf(actual, newPassword);
        EM.getTransaction().commit();

        assertThat(
            DAO.getUserByEmail(actual.getEmail()).getPassword(),
            is(not(USER_1.getPassword()))
        );
        assertThat(
            DAO.getUserByEmail(actual.getEmail()).getPassword(),
            is(newPassword)
        );
    }
}
