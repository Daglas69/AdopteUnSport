package fr.univlyon1.m1if.m1if10.gr14.dto;

import fr.univlyon1.m1if.m1if10.gr14.model.Event;

import java.util.GregorianCalendar;


/**
 * DTO for the Event class.
 */
public class EventDto {
    private int idEvent;
    private UserDto owner;
    private SportDto sport;
    private String address;
    private String town;
    private String eventType;
    private int maxPlayers;
    private String date;
    private String hour;
    private int currentPlayers;
    private String contact;
    private String description;
    private boolean terminated;
    private boolean cancelled;

    /**
     * Default constructor. Does nothing.
     */
    public EventDto() { }

    /**
     * Constructor of the DTO.
     * @param idEvent ID of the event
     * @param owner The owner DTO of the event
     * @param sport The sport DTO of the event
     * @param address The address of the event
     * @param town The town of the event
     * @param eventType The type of the event
     * @param maxPlayers The max of players that can join the event
     * @param date The date as a string of the event
     * @param hour The hour as a string of the event
     * @param currentPlayers The number of players that joined the event
     * @param contact The contact of the owner
     * @param description The description of the event
     * @param terminated True if the event is finished
     * @param cancelled True if the event has been cancelled
     */
    public EventDto(final int idEvent,
                final UserDto owner,
                final SportDto sport,
                final String address,
                final String town,
                final String eventType,
                final int maxPlayers,
                final String date,
                final String hour,
                final int currentPlayers,
                final String contact,
                final String description,
                final boolean terminated,
                final boolean cancelled) {

        this.idEvent = idEvent;
        this.owner = owner;
        this.sport = sport;
        this.address = address;
        this.town = town;
        this.eventType = eventType;
        this.maxPlayers = maxPlayers;
        this.date = date;
        this.hour = hour;
        this.currentPlayers = currentPlayers;
        this.contact = contact;
        this.description = description;
        this.terminated = terminated;
        this.cancelled = cancelled;
    }

    public int getIdEvent() {
        return this.idEvent;
    }

    public UserDto getOwner() {
        return this.owner;
    }

    public SportDto getSport() {
        return this.sport;
    }

    public String getAddress() {
        return this.address;
    }

    public String getTown() {
        return this.town;
    }

    public String getEventType() {
        return this.eventType;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public String getDate() {
        return this.date;
    }

    public String getHour() {
        return this.hour;
    }

    public boolean getTerminated() {
        return this.terminated;
    }

    public int getCurrentPlayers() {
        return this.currentPlayers;
    }

    public String getContact() {
        return this.contact;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getCancelled() {
        return this.cancelled;
    }


    /**
     * Returns a new instance of the DTO, from an Event object passed as argument.
     * @param event Event model
     * @return new EventDto instance
     */
    public static EventDto newInstance(final Event event) {

        //Set date and hour
        //We could have used a dateFormatter, but the developper whom coded this did not
        //know about that. We were so much surprised when we read his code,
        //that we decided to keep it, for the LORE
        //private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(event.getDate());
        String date;
        String hour;
        date = String.valueOf(calendar.get(GregorianCalendar.YEAR)) + "-";
        if (calendar.get(GregorianCalendar.MONTH) < 9) {
            date = date + "0" + String.valueOf(calendar.get(GregorianCalendar.MONTH) + 1) + "-";
        }
        else {
             date = date + String.valueOf(calendar.get(GregorianCalendar.MONTH) + 1) + "-";
        }
        if (calendar.get(GregorianCalendar.DAY_OF_MONTH) < 10) {
            date = date + "0" + String.valueOf(calendar.get(GregorianCalendar.DAY_OF_MONTH));
        }
        else {
             date = date + String.valueOf(calendar.get(GregorianCalendar.DAY_OF_MONTH));
        }
        if (calendar.get(GregorianCalendar.HOUR_OF_DAY) < 10) {
            hour = "0" + String.valueOf(calendar.get(GregorianCalendar.HOUR_OF_DAY)) + ":";
        }
        else {
            hour = String.valueOf(calendar.get(GregorianCalendar.HOUR_OF_DAY)) + ":";
        }
        if (calendar.get(GregorianCalendar.MINUTE) < 10) {
            hour = hour + "0" + String.valueOf(calendar.get(GregorianCalendar.MINUTE));
        }
        else {
             hour = hour + String.valueOf(calendar.get(GregorianCalendar.MINUTE));
        }

        //set address and town
        String address = event.getAddress().split(", ")[0];
        String town = event.getAddress().split(", ")[1];

        return new EventDto(
            event.getIdEvent(),
            UserDto.newInstance(event.getOwner()),
            SportDto.newInstance(event.getSport()),
            address,
            town,
            event.getEventType(),
            event.getMaxPlayers(),
            date,
            hour,
            event.getCurrentPlayers(),
            event.getContact(),
            event.getDescription(),
            event.isTerminated(),
            event.getCancelled()
        );
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + (cancelled ? 1231 : 1237);
        result = prime * result + ((contact == null) ? 0 : contact.hashCode());
        result = prime * result + currentPlayers;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + ((hour == null) ? 0 : hour.hashCode());
        result = prime * result + idEvent;
        result = prime * result + maxPlayers;
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((sport == null) ? 0 : sport.hashCode());
        result = prime * result + (terminated ? 1231 : 1237);
        result = prime * result + ((town == null) ? 0 : town.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        EventDto other = (EventDto) obj;
        if (terminated != other.terminated
            || cancelled != other.cancelled
            || currentPlayers != other.currentPlayers
            || idEvent != other.idEvent
            || maxPlayers != other.maxPlayers
        ) {
            return false;
        }

        if (address == null) {
            if (other.address != null) {
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
        if (hour == null) {
            if (other.hour != null) {
                return false;
            }
        } else if (!hour.equals(other.hour)) {
            return false;
        }
        if (owner == null) {
            if (other.owner != null) {
                return false;
            }
        } else if (!owner.equals(other.owner)) {
            return false;
        }
        if (sport == null) {
            if (other.sport != null) {
                return false;
            }
        } else if (!sport.equals(other.sport)) {
            return false;
        }
        if (town == null) {
            if (other.town != null) {
                return false;
            }
        } else if (!town.equals(other.town)) {
            return false;
        }

        return true;
    }
}
