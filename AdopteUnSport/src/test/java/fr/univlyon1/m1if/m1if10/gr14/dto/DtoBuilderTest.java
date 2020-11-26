package fr.univlyon1.m1if.m1if10.gr14.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import fr.univlyon1.m1if.m1if10.gr14.model.Event;
import fr.univlyon1.m1if.m1if10.gr14.model.Sport;
import fr.univlyon1.m1if.m1if10.gr14.model.User;

public class DtoBuilderTest {

    /**
     * Tests the <code>DtoBuilder#build</code> method with valid parameters.
     */
    @Test
    public void buildTest() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        final Date eventDate = cal.getTime();

        //Setup common model between expected and actual results
        final Sport sport = new Sport("Sport");
        final User user = new User("email", "username", "hash");
        final Event event = new Event(
            user, sport, "Adresse, Ville", "Libre",
            10, eventDate, 0, "contact@contact.fr", "Descrption de l'évènement"
        );

        //ExpectedResults
        final SportDto ds = DtoBuilder.build(sport, SportDto.class);
        final UserDto du = DtoBuilder.build(user, UserDto.class);
        final EventDto de = DtoBuilder.build(event, EventDto.class);

        assertThat(
            ds,
            is(new SportDto(sport.getName()))
        );
        assertThat(
            du,
            is(new UserDto(
                user.getEmail(), user.getUsername()
            ))
        );

        //Event actual result
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final String[] splitDate = dateFormatter.format(eventDate).split(" ");
        final String[] splitAddress = event.getAddress().split(", ");

        assertThat(
            de,
            is(new EventDto(
                event.getIdEvent(), du, ds,
                splitAddress[0], splitAddress[1],
                event.getEventType(),
                event.getMaxPlayers(),
                splitDate[0], splitDate[1],
                event.getCurrentPlayers(),
                event.getContact(), event.getDescription(),
                false,
                event.getCancelled()
            ))
        );
    }

    /**
     * Tests the <code>DtoBuilder#build</code> method with invalid parameters.
     */
    @Test
    public void buildExceptionTest() {
        //Object is not a valid Model
        assertThat(
            DtoBuilder.build(new Object(), SportDto.class),
            is((SportDto) null)
        );

        //Object.class is not a valid DTO class
        assertThat(
            DtoBuilder.build(new Object(), Object.class),
            is((Object) null)
        );
    }

    /**
     * Tests the <code>DtoBuilder#buildList</code> method.
     */
    @Test
    public void buildListTest() {
        final List<Sport> models = Arrays.asList(
            new Sport("Sport1"),
            new Sport("Sport2"),
            new Sport("Sport3")
        );
        final List<SportDto> resultList = DtoBuilder.buildList(models, SportDto.class);
        final List<SportDto> expectedList = new ArrayList<>();
        for (Sport s: models) {
            expectedList.add(new SportDto(s.getName()));
        }

        assertThat(
            resultList,
            is(expectedList)
        );
    }

    /**
     * Tests the <code>DtoBuilder#buildList</code> method with invalid parameters.
     */
    @Test
    public void buildListExceptionTest() {
        final Sport sport1 = new Sport("Sport1");
        final Sport sport3 = new Sport("Sport3");
        final List<Object> models = Arrays.asList(
            sport1,
            new Object(),
            sport3
        );

        assertThat(
            DtoBuilder.buildList(models, SportDto.class),
            is(Arrays.asList(
                new SportDto(sport1.getName()),
                null,
                new SportDto(sport3.getName())
            ))
        );
    }
}
