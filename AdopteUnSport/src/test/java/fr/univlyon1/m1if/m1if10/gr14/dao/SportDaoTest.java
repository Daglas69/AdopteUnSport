package fr.univlyon1.m1if.m1if10.gr14.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Test;

import fr.univlyon1.m1if.m1if10.gr14.model.Sport;
import fr.univlyon1.m1if.m1if10.gr14.utils.TestsHelper;


public class SportDaoTest {
    /* ### STATIC FIELDS ### */

    private static final EntityManager EM = TestsHelper.getEntityManager();

    private static final SportDao DAO = new SportDao(EM);
    private static final Sport SPORT_1 = new Sport("Football");
    private static final Sport SPORT_2 = new Sport("Tennis");



    /* ### SETUP ### */

    /**
     * Empties the 'sports' table.
     */
    @After
    public void clean() {
        TestsHelper.clearSports(EM);
    }



    /* ### TESTS ### */

    /**
     * Test of the <code>SportDao#getAllSports</code> method.
     */
    @Test
    public void testGetAllSports() {
        List<Sport> expected = new ArrayList<>();

        //If nothing is created, list is empty
        //(using "assertThat" to show contents in case of failure)
        assertThat(
            DAO.getAllSports(),
            is(expected)
        );

        //After insertion
        expected = Arrays.asList(SPORT_1, SPORT_2);
        TestsHelper.persistList(EM, expected);

        //We should get both sports, no other
        assertThat(
            DAO.getAllSports(),
            is(expected)
        );
    }

    /**
     * Test of the <code>SportDao#getSportByName</code> method.
     */
    @Test
    public void testGetSportByName() {
        TestsHelper.persist(EM, SPORT_1);

        assertThat(
            DAO.getSportByName(SPORT_1.getName()),
            is(SPORT_1)
        );
    }

    /**
     * Test of the <code>SportDao#getSportByName</code> method,
     * but passing a <code>null</code> argument.
     */
    @Test
    public void testGetSportByNameNullName() {
        assertThat(
            DAO.getSportByName(null),
            is((Sport) null)
        );
    }

    /**
     * Test of the <code>SportDao#create</code> method.
     */
    public void testCreate() {
        //Only insert one
        TestsHelper.persist(EM, SPORT_1);
        final int nbSports = DAO.getAllSports().size();
        boolean result;

        //Already exists, cannot be created
        EM.getTransaction().begin();
        result = DAO.create(SPORT_1);
        EM.getTransaction().commit();
        assertThat(result, is(false));

        //Does not exist, is created
        EM.getTransaction().begin();
        result = DAO.create(SPORT_2);
        EM.getTransaction().commit();
        assertThat(result, is(true));

        //One new entry in the database
        assertThat(
            DAO.getAllSports().size(),
            is(nbSports + 1)
        );
    }

    /**
     * Test of the <code>SportDao#getOrCreate</code> method
     * with a Sport that already exists in the database.
     */
    @Test
    public void testGetOrCreateAlreadyExist() {
        //Insert it
        TestsHelper.persist(EM, SPORT_1);
        final int nbSports = DAO.getAllSports().size();

        //Already exists, is returned
        EM.getTransaction().begin();
        final Sport result = DAO.getOrCreate(SPORT_1.getName());
        EM.getTransaction().commit();

        assertThat(
            result,
            is(SPORT_1)
        );

        //The total number of sports in database should not have changed
        assertThat(
            DAO.getAllSports().size(),
            is(nbSports)
        );
    }

    /**
     * Test of the <code>SportDao#getOrCreate</code> method
     * with a Sport that hasn't been inserted yet.
     */
    @Test
    public void testGetOrCreateNotYetInserted() {
        //Should be 0
        final int nbSports = DAO.getAllSports().size();

        //Created and returned
        EM.getTransaction().begin();
        final Sport result = DAO.getOrCreate(SPORT_1.getName());
        EM.getTransaction().commit();
        assertThat(
            result,
            is(SPORT_1)
        );

        //Number of sports should've increased by one
        assertThat(
            DAO.getAllSports().size(),
            is(nbSports + 1)
        );
    }
}
