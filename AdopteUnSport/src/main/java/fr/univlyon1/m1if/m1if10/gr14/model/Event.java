package fr.univlyon1.m1if.m1if10.gr14.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import fr.univlyon1.m1if.m1if10.gr14.model.constraint.Address;
import fr.univlyon1.m1if.m1if10.gr14.model.constraint.CurrentPlayerNumber;
import fr.univlyon1.m1if.m1if10.gr14.model.constraint.Description;
import fr.univlyon1.m1if.m1if10.gr14.model.constraint.FutureDate;
import fr.univlyon1.m1if.m1if10.gr14.model.constraint.MaxPlayerNumber;
import fr.univlyon1.m1if.m1if10.gr14.model.constraint.Text;


/**
 * Represent the table "events" in database.
 */
@Entity
@Table(name = "events")
@NamedQueries({
    @NamedQuery(name = "Event.getCurrentEvents",
        query = "SELECT e FROM Event e "
            + "WHERE e.date > CURRENT_TIMESTAMP "
            + "ORDER BY e.date ASC"),

    @NamedQuery(name = "Event.getAvailableEvents",
        query = "SELECT e FROM Event e "
            + "WHERE e.date > CURRENT_TIMESTAMP "
            + "AND e.currentPlayers < e.maxPlayers "
            + "AND e.cancelled = FALSE "
            + "ORDER BY e.date ASC"),

    @NamedQuery(name = "Event.getAvailableEventsByOwner",
        query = "SELECT e FROM Event e "
            + "WHERE e.date > CURRENT_TIMESTAMP "
            + "AND e.currentPlayers < e.maxPlayers "
            + "AND LOWER(e.owner.email) = LOWER(:ownerEmail) "
            + "AND e.cancelled = FALSE "
            + "ORDER BY e.date ASC"),

    @NamedQuery(name = "Event.getAvailableEventsBySport",
        query = "SELECT e FROM Event e "
            + "WHERE e.date > CURRENT_TIMESTAMP "
            + "AND e.currentPlayers < e.maxPlayers "
            + "AND LOWER(e.sport.name) = LOWER(:sportName) "
            + "AND e.cancelled = FALSE "
            + "ORDER BY e.date ASC"),

    @NamedQuery(name = "Event.getAllEventsByUser",
        query = "SELECT e FROM Event e "
            + "INNER JOIN e.players u "
            + "ON LOWER(u.email) = LOWER(:userEmail) "
            + "ORDER BY e.date ASC"),

    @NamedQuery(name = "Event.getAllOwnedEventsByUser",
        query = "SELECT e FROM Event e "
            + "WHERE LOWER(e.owner.email) = LOWER(:ownerEmail) "
            + "ORDER BY e.date ASC"),

    @NamedQuery(name = "Event.getOldOwnedEventsByUser",
        query = "SELECT e FROM Event e "
            + "WHERE e.date < CURRENT_TIMESTAMP "
            + "AND LOWER(e.owner.email) = LOWER(:ownerEmail) "
            + "ORDER BY e.date ASC"),

    @NamedQuery(name = "Event.getOldPlayedEventsByUser",
        query = "SELECT e FROM Event e "
            + "INNER JOIN e.players u "
            + "WHERE e.date < CURRENT_TIMESTAMP "
            + "AND LOWER(u.email) = LOWER(:userEmail) "
            + "ORDER BY e.date ASC"),

    //Only used for testing purposes (see TestsHelper.java)
    @NamedQuery(name = "Event.getAllEvents",
        query = "SELECT e FROM Event e"),
})
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MIN_DAYS_DATE = 1; //Event cant be created for the current day

    @Id
    @GeneratedValue()
    //No constraint validation needed
    private int idEvent;

    @ManyToOne
    @JoinColumn(name = "owner")
    //No constraint validation needed
    private User owner;

    @ManyToMany
    @JoinTable(name = "users_events", joinColumns = @JoinColumn(name = "idevent"),
        inverseJoinColumns = @JoinColumn(name = "email"))
    private List<User> players;

    @ManyToOne
    @JoinColumn(name = "sport")
    //No constraint validation needed
    private Sport sport;

    @Address //Constraint validation
    private String address;

    @Text //Constraint validation
    private String eventType;

    @MaxPlayerNumber //Constraint validation
    private int maxPlayers;

    @Temporal(TemporalType.TIMESTAMP)
    @FutureDate //Constraint validation
    private Date date;

    @CurrentPlayerNumber //Constraint validation
    private int currentPlayers;

    @Email(message = "Le contact doit être une adresse email.") //Constraint validation
    private String contact;

    @Lob
    @Description //Constraint validation
    private String description;

    private boolean cancelled;

    /**
     * Default constructor. Does nothing.
     */
    public Event() {
    }

    /**
     * Constructor of Event.
     *
     * @param owner          The owner of the event
     * @param sport          The sport of the event
     * @param address        The address of the event
     * @param eventType      The type of the event
     * @param maxPlayers     The max of players that can join the event
     * @param date           The date of the event
     * @param currentPlayers The number of players that joined the event
     * @param contact        The contact of the owner
     * @param description    The description of the event
     */
    public Event(
        final User owner, final Sport sport, final String address, final String eventType,
        final int maxPlayers, final Date date, final int currentPlayers, final String contact,
        final String description) {
        this.owner = owner;
        this.sport = sport;
        this.address = address;
        this.eventType = eventType;
        this.maxPlayers = maxPlayers;
        this.date = date;
        this.currentPlayers = currentPlayers;
        this.contact = contact;
        this.description = description;
        this.cancelled = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((contact == null) ? 0 : contact.hashCode());
        result = prime * result + currentPlayers;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + idEvent;
        result = prime * result + maxPlayers;
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((players == null) ? 0 : players.hashCode());
        result = prime * result + ((sport == null) ? 0 : sport.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Event other = (Event) obj;
        if (address == null) {
            if (other.address != null)  {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }

        if (contact == null) {
            if (other.contact != null) {
                return false;
            }
        } else if (!contact.equals(other.contact)) {
            return false;
        }

        if (currentPlayers != other.currentPlayers) {
            return false;
        }
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }

        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }

        if (eventType == null) {
            if (other.eventType != null) {
                return false;
            }
        } else if (!eventType.equals(other.eventType)) {
            return false;
        }

        if (idEvent != other.idEvent) {
            return false;
        }

        if (maxPlayers != other.maxPlayers) {
            return false;
        }
        if (owner == null) {
            if (other.owner != null) {
                return false;
            }
        } else if (!owner.equals(other.owner)) {
            return false;
        }

        if (players == null) {
            if (other.players != null) {
                return false;
            }
        } else if (!players.equals(other.players)) {
            return false;
        }

        if (sport == null) {
            if (other.sport != null) {
                return false;
            }
        } else if (!sport.equals(other.sport)) {
            return false;
        }

        return cancelled == other.cancelled;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }

    public List<User> getPlayers() {
        return players;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(final Sport sport) {
        this.sport = sport;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public boolean isTerminated() {
        return this.date.compareTo(new Date()) < 0;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(final int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(final String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean getCancelled() {
        return cancelled;
    }

    /**
     * Set the event as cancelled.
     */
    public void cancel() {
        this.cancelled = true;
    }


    /**
     * Static method to verify if the date passed as argument is far enough
     * from the current date, according to event creation policy.
     * @param date Date to check
     * @return True if the date is far enough from the current date
     */
    public static boolean isValidDate(final Date date) {
        if (date == null) {
            return false;
        }
        long diffInMs = date.getTime() - new Date().getTime();
        long diffInDay = (diffInMs / (1000 * 60 * 60 * 24)) % 365;
        return diffInDay >= MIN_DAYS_DATE - 1;
    }
}
