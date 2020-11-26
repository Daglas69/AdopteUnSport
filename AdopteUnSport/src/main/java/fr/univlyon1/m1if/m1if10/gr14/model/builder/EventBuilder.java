package fr.univlyon1.m1if.m1if10.gr14.model.builder;

import java.util.Date;
import fr.univlyon1.m1if.m1if10.gr14.model.Event;
import fr.univlyon1.m1if.m1if10.gr14.model.Sport;
import fr.univlyon1.m1if.m1if10.gr14.model.User;


/**
 * Builder of the model class Event.
 */
public final class EventBuilder {
    private User owner = null;
    private Sport sport = null;
    private String address = null;
    private String eventType = null;
    private int maxPlayers = -1;
    private Date date = null;
    private final int currentPlayers = 0; //Can not be modified
    private String contact = null;
    private String description = null;

    /**
     * Adds owner to the event.
     * @param user Owner of the event
     * @return EventBuilder
     */
    public EventBuilder withOwner(final User user) {
        this.owner = user;
        return this;
    }

    /**
     * Adds sport to the event.
     * @param sport Sport of the event
     * @return EventBuilder
     */
    public EventBuilder withSport(final Sport sport) {
        this.sport = sport;
        return this;
    }

    /**
     * Adds address to the event.
     * @param address Address of the event
     * @return EventBuilder
     */
    public EventBuilder atAddress(final String address) {
        this.address = address;
        return this;
    }

    /**
     * Adds type to the event.
     * @param eventType Type of the event
     * @return EventBuilder
     */
    public EventBuilder ofType(final String eventType) {
        this.eventType = eventType;
        return this;
    }

    /**
     * Adds the maximum number of players to the event.
     * @param maxPlayers Maximum number of players
     * @return EventBuilder
     */
    public EventBuilder withMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    /**
     * Adds the date of the event.
     * @param date Date of the event
     * @return EventBuilder
     */
    public EventBuilder atDate(final Date date) {
        this.date = date;
        return this;
    }

    /**
     * Adds the contact for the event.
     * @param contact Contact of the event
     * @return EventBuilder
     */
    public EventBuilder withContact(final String contact) {
        this.contact = contact;
        return this;
    }

    /**
     * Adds the description of the event.
     * @param description Description of the event
     * @return EventBuilder
     */
    public EventBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }


    /**
     * Returns an Event instance with the values added from builder.
     * All values must be added or it will return null.
     * @return New instance of Event model class
     */
    public Event build() {
        if (this.owner == null
            || this.sport == null
            || this.address == null
            || this.eventType == null
            || this.maxPlayers == -1
            || this.date == null
            || this.contact == null
            || this.description == null) {
                return null;
            }

        return new Event(
            this.owner,
            this.sport,
            this.address,
            this.eventType,
            this.maxPlayers,
            this.date,
            this.currentPlayers,
            this.contact,
            this.description
        );
    }
}
