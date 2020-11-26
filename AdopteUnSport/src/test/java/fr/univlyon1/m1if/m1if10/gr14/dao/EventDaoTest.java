package fr.univlyon1.m1if.m1if10.gr14.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.univlyon1.m1if.m1if10.gr14.model.Event;
import fr.univlyon1.m1if.m1if10.gr14.model.Sport;
import fr.univlyon1.m1if.m1if10.gr14.model.User;
import fr.univlyon1.m1if.m1if10.gr14.model.builder.EventBuilder;
import fr.univlyon1.m1if.m1if10.gr14.utils.TestsHelper;

public class EventDaoTest {
    /* ### PRIVATE FIELDS ### */

    private static final EntityManager EM = TestsHelper.getEntityManager();

    private static final EventDao EVENT_DAO = new EventDao(EM);

    //These fields should always be persisted (see setupPersist() method).
    private static final User USER_1 = new User("emailbidon@bidon.bidon", "Bidon", "b1d0n");
    private static final User USER_2 = new User("player1@adopteunsport.test", "Player1", "mdpp1");
    private static final Sport SPORT_1 = new Sport("football");
    private static final Sport SPORT_2 = new Sport("basket");

    private static final EventBuilder BUILDER = new EventBuilder();

    private static final int MAX_PLAYERS = 10;

    /* Variables used in limit/page tests,
       allowing for two pages, one full, and the second with only one result */
    private static final int NB_EVENTS_PER_PAGE = 10;
    private static final int NB_EVENTS_TOTAL = NB_EVENTS_PER_PAGE + 1;

    private final Date pastDate;
    private final Date futureDate;



    /* ### SETUP ### */

    /**
     * Sets up the fields that cannot be staticly initialized
     * (i.e. the dates and the constant fields of the event builder).
     */
    public EventDaoTest() {
        //Dates instanciation
        Calendar cal = Calendar.getInstance();

        //Date from a week ago
        cal.setTime(new Date());
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        pastDate = cal.getTime();

        //Date in a week
        cal.setTime(new Date());
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        futureDate = cal.getTime();

        //Setting up the fields common to every Event in the tests
        BUILDER
            .atAddress("address")
            .ofType("libre")
            .withContact("contact")
            .withDescription("some description")
            .withMaxPlayers(MAX_PLAYERS);
    }

    /**
     * Sets up the data needed in all tests in the database.
     * NB: is not a BeforeClass since it would be executed when tests are globally setup,
     * causing troubles in other test suites.
     */
    @Before
    public void setupPersist() {
        //Persisting fields needed in every test
        TestsHelper.persistAll(EM,
            USER_1,
            USER_2,
            SPORT_1,
            SPORT_2
        );
    }

    /**
     * Clears all the inserted fields.
     */
    @After
    public void clean() {
        TestsHelper.clearDatabase(EM);
    }



    /* ### TESTS ### */


    /* # MODIFY EVENT # */

    /**
     * Test of the <code>EventDao#addUserToEvent</code> and
     * <code>EventDao#removeUserFromEvent</code> methods.
     */
    @Test
    public void testAddAndRemoveUserFromEvent() {
        final Event event = BUILDER.withSport(SPORT_1)
                                   .withOwner(USER_1)
                                   .atDate(futureDate)
                                   .build();

        TestsHelper.persist(EM, event);
        final int initialSize = EVENT_DAO.getEventById(event.getIdEvent()).getPlayers().size();
        int code;

        //Adding the user: returns OK and size increases
        EM.getTransaction().begin();
        code = EVENT_DAO.addUserToEvent(USER_2, event);
        EM.getTransaction().commit();
        assertThat(
            code,
            is(EventDao.CODE_SUCCESS)
        );
        assertThat(
            EVENT_DAO.getEventById(event.getIdEvent()).getPlayers().size(),
            is(initialSize + 1)
        );

        //Removing the user: returns OK and size decreases
        EM.getTransaction().begin();
        code = EVENT_DAO.removeUserFromEvent(USER_2, event);
        EM.getTransaction().commit();
        assertThat(
            code,
            is(EventDao.CODE_SUCCESS)
        );
        assertThat(
            EVENT_DAO.getEventById(event.getIdEvent()).getPlayers().size(),
            is(initialSize)
        );
    }

    /**
     * Test of the <code>EventDao#addUserToEvent</code> with
     * an unknown user.
     */
    @Test
    public void testAddUserToEventInvalidUser() {
        final User invalidUser = new User("e", "", "");
        final Event event = BUILDER.withSport(SPORT_1)
                                   .withOwner(USER_1)
                                   .atDate(futureDate)
                                   .build();

        TestsHelper.persist(EM, event);

        EM.getTransaction().begin();
        final int code = EVENT_DAO.addUserToEvent(invalidUser, event);
        EM.getTransaction().commit();

        assertThat(
            code,
            is(EventDao.CODE_INCORRECT_PARAM)
        );

        //Size does not change
        assertThat(
            EVENT_DAO.getEventById(event.getIdEvent()).getPlayers().size(),
            is(0)
        );
    }

    /**
     * Test of the <code>EventDao#addUserToEvent</code> with
     * invalid events (not inserted, full, or cancelled).
     */
    @Test
    public void testAddUserToEventInvalidEvent() {
        //Is never inserted
        final Event eventInvalid = BUILDER.withSport(SPORT_1)
                                    .withOwner(USER_1)
                                    .atDate(futureDate)
                                    .build();

        final Event eventFull = BUILDER.build();
        final Event eventCancelled = BUILDER.build();
        TestsHelper.persistAll(EM, eventFull, eventCancelled);

        EM.getTransaction().begin();

        eventFull.setCurrentPlayers(MAX_PLAYERS);   //event is full
        eventCancelled.cancel();                    //event has been cancelled

        int codeInvalid   = EVENT_DAO.addUserToEvent(USER_1, eventInvalid);
        int codeFull      = EVENT_DAO.addUserToEvent(USER_1, eventFull);
        int codeCancelled = EVENT_DAO.addUserToEvent(USER_1, eventCancelled);

        EM.getTransaction().commit();

        //Event does not exist
        assertThat(
            codeInvalid,
            is(EventDao.CODE_INCORRECT_PARAM)
        );

        //Event is already full
        assertThat(
            codeFull,
            is(EventDao.CODE_EVENT_FULL)
        );

        //Event has been cancelled
        assertThat(
            codeCancelled,
            is(EventDao.CODE_EVENT_CANCEL)
        );
    }

    /**
     * Test of the <code>EventDao#removeUserFromEvent</code> with
     * invalid users (unknown or has not joined).
     */
    @Test
    public void testRemoveUserFromEventInvalidUser() {
        final Event event = BUILDER.withSport(SPORT_1)
                                   .withOwner(USER_1)
                                   .atDate(futureDate)
                                   .build();

        final User invalidUser = new User("e", "", "");

        TestsHelper.persistAll(EM, event, USER_1);

        EM.getTransaction().begin();
        final int codeInvalid = EVENT_DAO.removeUserFromEvent(invalidUser, event);
        final int codeNotJoined = EVENT_DAO.removeUserFromEvent(USER_1, event);
        EM.getTransaction().commit();

        //invalidUser is unknown
        assertThat(
            codeInvalid,
            is(EventDao.CODE_INCORRECT_PARAM)
        );
        //USER_1 did not join
        assertThat(
            codeNotJoined,
            is(EventDao.CODE_USER_NOT_PARTICIPATING)
        );

        //Size does not change
        assertThat(
            EVENT_DAO.getEventById(event.getIdEvent()).getPlayers().size(),
            is(0)
        );
    }

    /**
     * Test of the <code>EventDao#removeUserFromEvent</code> with
     * an invalid (unknown) event.
     */
    @Test
    public void testRemoveUserFromEventInvalidEvent() {
        //Is never inserted
        final Event event = BUILDER.withSport(SPORT_1)
                                   .withOwner(USER_1)
                                   .atDate(futureDate)
                                   .build();

        //Removing returns -1 code
        assertThat(
            EVENT_DAO.removeUserFromEvent(USER_1, event),
            is(EventDao.CODE_INCORRECT_PARAM)
        );
    }


    /* # ALL EVENTS # */

    /**
     * Test of the <code>EventDao#getEventById</code> method.
     */
    @Test
    public void testGetEventById() {
        //Some random event
        final Event event = BUILDER.withSport(SPORT_1)
                                   .atDate(futureDate)
                                   .withOwner(USER_1)
                                   .build();

        //not created, not found
        assertThat(
            EVENT_DAO.getEventById(event.getIdEvent()),
            is((Event) null)
        );

        //created, is found
        TestsHelper.persist(EM, event);
        assertThat(
            EVENT_DAO.getEventById(event.getIdEvent()),
            is(event)
        );
    }

    /**
     * Test of the <code>EventDao#getAllEventsByUser</code> method.
     */
    @Test
    public void testGetAllEventsByUser() {
        //Two different events, USER_1 is the owner
        final Event event1 = BUILDER.withSport(SPORT_1)
                                    .atDate(futureDate)
                                    .withOwner(USER_1)
                                    .build();

        final Event event2 = BUILDER.build();

        TestsHelper.persistAll(EM, event1, event2);

        //User is in one event, but not in the other
        EM.getTransaction().begin();
        EVENT_DAO.addUserToEvent(USER_1, event1);
        EM.getTransaction().commit();

        assertThat(
            EVENT_DAO.getAllEventsByUser(USER_1),
            is(Arrays.asList(event1))
        );
    }

    /**
     * Test of the <code>EventDao#getPlayersByevent</code> method.
     */
    @Test
    public void testGetPlayersByEvent() {
        final Event event = BUILDER.withSport(SPORT_1)
                                    .atDate(futureDate)
                                    .withOwner(USER_1)
                                    .build();

        TestsHelper.persist(EM, event);
        List<User> expected = new ArrayList<>();

        assertThat(
            EVENT_DAO.getPlayersByEvent(event),
            is(expected)
        );

        EM.getTransaction().begin();
        EVENT_DAO.addUserToEvent(USER_1, event);
        EVENT_DAO.addUserToEvent(USER_2, event);
        EM.getTransaction().commit();

        expected = Arrays.asList(USER_1, USER_2);
        assertThat(
            EVENT_DAO.getPlayersByEvent(event),
            is(expected)
        );
    }

    /**
     * Test of the <code>EventDao#getPlayersByevent</code> method,
     * with an invalid Event.
     */
    @Test
    public void testGetPlayersByEventInvalid() {
        final Event event = BUILDER.withSport(SPORT_1)
                                    .atDate(futureDate)
                                    .withOwner(USER_1)
                                    .build();

        assertThat(
            EVENT_DAO.getPlayersByEvent(event),
            is((List<User>) null)
        );
    }

    /**
     * Test of the <code>EventDao#getAllOwnedEventsByUser</code> method.
     * TODO: Parametrize it with past/current dates, full or not ?
     */
    @Test
    public void testGetAllOwnedEventsByUser() {
        //Two events of different owner
        final Event event1 = BUILDER.withOwner(USER_1)
                                    .withSport(SPORT_1)
                                    .atDate(futureDate)
                                    .build();

        final Event event2 = BUILDER.withOwner(USER_2).build();

        final List<Event> expected = new ArrayList<>();

        TestsHelper.persist(EM, event2);

        //event 1 does not exist yet, no results for USER_1
        assertThat(
            EVENT_DAO.getAllOwnedEventsByUser(USER_1),
            is(expected)
        );

        //Once created, appears
        TestsHelper.persist(EM, event1);
        expected.add(event1);
        assertThat(
            EVENT_DAO.getAllOwnedEventsByUser(USER_1),
            is(expected)
        );
    }

    /**
     * Test of the <code>EventDao#cancelEvent</code> method,
     * with both valid and invalid events.
     */
    @Test
    public void testCancelEvent() {
        final Event event = BUILDER.withOwner(USER_1)
                                    .withSport(SPORT_1)
                                    .atDate(futureDate)
                                    .build();

        boolean result;

        EM.getTransaction().begin();
        result = EVENT_DAO.cancelEvent(event);
        EM.getTransaction().commit();

        assertFalse(result);

        TestsHelper.persist(EM, event);
        EM.getTransaction().begin();
        result = EVENT_DAO.cancelEvent(event);
        EM.getTransaction().commit();

        assertTrue(result);
    }

    /* # CURRENT EVENTS # */

    /**
     * Test of the <code>EventDao#getCurrentEvents</code> method.
     */
    @Test
    public void testGetCurrentEvents() {
        //One current event
        final Event currentEvent = BUILDER.withOwner(USER_1)
                                          .withSport(SPORT_1)
                                          .atDate(futureDate)
                                          .build();

        //And an old one
        final Event oldEvent = BUILDER.atDate(pastDate).build();

        TestsHelper.persistAll(EM, currentEvent, oldEvent);

        //Only the current event is listed
        assertThat(
            EVENT_DAO.getCurrentEvents(),
            is(Arrays.asList(currentEvent))
        );
    }

    /**
     * Test of the <code>EventDao#getCurrentEvents</code> method, with the
     * <code>limit</code> and <code>page</code> arguments.
     */
    @Test
    public void testGetCurrentEventsWithLimitAndPage() {
        BUILDER.withOwner(USER_1)
               .withSport(SPORT_1)
               .atDate(futureDate);

        for (int i = 0; i < NB_EVENTS_TOTAL; i++) {
            TestsHelper.persist(EM, BUILDER.build());
        }

        //First page is full...
        assertThat(
            EVENT_DAO.getCurrentEvents(NB_EVENTS_PER_PAGE, 1).size(),
            is(NB_EVENTS_PER_PAGE)
        );

        //...second page only contains one entry
        assertThat(
            EVENT_DAO.getCurrentEvents(NB_EVENTS_PER_PAGE, 2).size(),
            is(NB_EVENTS_TOTAL - NB_EVENTS_PER_PAGE)
        );
    }


    /* # AVAILABLE EVENTS # */

    /**
     * Test of the <code>EventDao#getAvailableEvents</code> method.
     */
    @Test
    public void testGetAvailableEvents() {
        final Event availableEvent = BUILDER.withOwner(USER_1)
                                            .withSport(SPORT_1)
                                            .atDate(futureDate)
                                            .build();

        final Event fullEvent = BUILDER.build();
        fullEvent.setCurrentPlayers(MAX_PLAYERS);

        final Event oldEvent = BUILDER.atDate(pastDate).build();

        TestsHelper.persistAll(EM,
            availableEvent, fullEvent, oldEvent
        );

        //Only the available one is returned
        assertThat(
            EVENT_DAO.getAvailableEvents(),
            is(Arrays.asList(availableEvent))
        );
    }

    /**
     * Test of the <code>EventDao#getAvailableEvents</code> method,
     * with <code>limit</code> and <code>page</code> arguments.
     */
    @Test
    public void testGetAvailableEventsWithLimitandPage() {
        BUILDER.withOwner(USER_1)
               .withSport(SPORT_1)
               .atDate(futureDate);

        for (int i = 0; i < NB_EVENTS_TOTAL; i++) {
            TestsHelper.persist(EM, BUILDER.build());
        }

        //first page is full
        assertThat(
            EVENT_DAO.getAvailableEvents(NB_EVENTS_PER_PAGE, 1).size(),
            is(NB_EVENTS_PER_PAGE)
        );

        //Second one only has one entry
        assertThat(
            EVENT_DAO.getAvailableEvents(NB_EVENTS_PER_PAGE, 2).size(),
            is(NB_EVENTS_TOTAL - NB_EVENTS_PER_PAGE)
        );
    }

    /**
     * Test of the <code>EventDao#getAvailableEventsByOwner</code> method.
     */
    @Test
    public void testGetAvailableEventsByOwner() {
        final Event eventOwned = BUILDER.withOwner(USER_1)
                                        .withSport(SPORT_1)
                                        .atDate(futureDate)
                                        .build();

        final Event eventNotOwned = BUILDER.withOwner(USER_2).build();

        TestsHelper.persistAll(EM, eventOwned, eventNotOwned);

        //Only the owned event shows.
        assertThat(
            EVENT_DAO.getAvailableEventsByOwner(USER_1),
            is(Arrays.asList(eventOwned))
        );
    }

    /**
     * Test of the <code>EventDao#getAvailableEventsByOwner</code> method,
     * with an invalid owner.
     */
    @Test
    public void testGetAvailableEventsByOwnerInvalidUser() {
        User invalid = new User("email", "username", "hash");

        assertTrue(
            EVENT_DAO.getAvailableEventsByOwner(invalid).isEmpty()
        );
    }

    /**
     * Test of the <code>EventDao#getAvailableEventsBySport</code> method.
     */
    @Test
    public void testGetAvailableEventsBySport() {
        final Event event1 = BUILDER.withOwner(USER_1)
                                        .withSport(SPORT_1)
                                        .atDate(futureDate)
                                        .build();

        final Event event2 = BUILDER.withSport(SPORT_2).build();

        TestsHelper.persistAll(EM, event1, event2);

        assertThat(
            EVENT_DAO.getAvailableEventsBySport(SPORT_1),
            is(Arrays.asList(event1))
        );
    }

    /**
     * Test of the <code>EventDao#getAvailableEventsBySport</code> method,
     * with an invalid sport.
     */
    @Test
    public void testGetAvailableEventsBySportInvalidSport() {
        final Sport invalid = new Sport("name");

        assertTrue(
            EVENT_DAO.getAvailableEventsBySport(invalid).isEmpty()
        );
    }


    /* # OLD EVENTS # */

    /**
     * Test of the <code>EventDao#getOldOwnedEventsByUser</code> method.
     */
    @Test
    public void testGetOldEventsOwnedByUser() {
        final Event availableEvent = BUILDER.withSport(SPORT_1)
                                            .withOwner(USER_1)
                                            .atDate(futureDate)
                                            .build();

        final Event oldEvent = BUILDER.atDate(pastDate).build();
        final Event notOwnedEvent = BUILDER.withOwner(USER_2).build();

        TestsHelper.persistAll(EM,
            availableEvent, oldEvent, notOwnedEvent
        );

        //Only the past AND owned event is found
        assertThat(
            EVENT_DAO.getOldOwnedEventsByUser(USER_1),
            is(Arrays.asList(oldEvent))
        );
    }

    /**
     * Test of the <code>EventDao#getOldOwnedEventsByUser</code> method,
     * with <code>limit</code> and <code>page</code> arguments.
     */
    @Test
    public void testGetOldEventsOwnedByUserWithLimitAndPage() {
        BUILDER.withOwner(USER_1)
               .withSport(SPORT_1)
               .atDate(pastDate);

        for (int i = 0; i < NB_EVENTS_TOTAL; i++) {
            TestsHelper.persist(EM, BUILDER.build());
        }

        assertThat(
            EVENT_DAO.getOldOwnedEventsByUser(USER_1, NB_EVENTS_PER_PAGE, 1).size(),
            is(NB_EVENTS_PER_PAGE)
        );
        assertThat(
            EVENT_DAO.getOldOwnedEventsByUser(USER_1, NB_EVENTS_PER_PAGE, 2).size(),
            is(NB_EVENTS_TOTAL - NB_EVENTS_PER_PAGE)
        );
    }

    /**
     * Test of the <code>EventDao#getOldEventsPlayedByUser</code> method.
     */
    @Test
    public void testGetOldEventsPlayedByUser() {
        final Event availableEvent = BUILDER.withSport(SPORT_1)
                                            .withOwner(USER_1)
                                            .atDate(futureDate)
                                            .build();

        final Event oldEvent = BUILDER.atDate(pastDate).build();
        final Event notJoinedEvent = BUILDER.build();
        final Event ownedNotJoinedEvent = BUILDER.withOwner(USER_2).build();

        TestsHelper.persistAll(EM,
            availableEvent, oldEvent, notJoinedEvent, ownedNotJoinedEvent
        );

        EM.getTransaction().begin();
        EVENT_DAO.addUserToEvent(USER_2, availableEvent);
        EVENT_DAO.addUserToEvent(USER_2, oldEvent);
        EM.getTransaction().commit();

        //Only the past AND joined event is found
        assertThat(
            EVENT_DAO.getOldPlayedEventsByUser(USER_2),
            is(Arrays.asList(oldEvent))
        );
    }

    /**
     * Test of the <code>EventDao#getOldEventsPlayedByUser</code> method,
     * with <code>limit</code> and <code>page</code> arguments.
     */
    @Test
    public void testGetOldEventsPlayedByUserWithLimitAndPage() {
        BUILDER.withOwner(USER_1)
               .withSport(SPORT_1)
               .atDate(pastDate);

        for (int i = 0; i < NB_EVENTS_TOTAL; i++) {
            Event e = BUILDER.build();
            TestsHelper.persist(EM, e);
            EM.getTransaction().begin();
            EVENT_DAO.addUserToEvent(USER_2, e);
            EM.getTransaction().commit();
        }

        assertThat(
            EVENT_DAO.getOldPlayedEventsByUser(USER_2, NB_EVENTS_PER_PAGE, 1).size(),
            is(NB_EVENTS_PER_PAGE)
        );
        assertThat(
            EVENT_DAO.getOldPlayedEventsByUser(USER_2, NB_EVENTS_PER_PAGE, 2).size(),
            is(NB_EVENTS_TOTAL - NB_EVENTS_PER_PAGE)
        );
    }
}
