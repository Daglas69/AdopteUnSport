package fr.univlyon1.m1if.m1if10.gr14.dto;

import fr.univlyon1.m1if.m1if10.gr14.model.Sport;


/**
 * DTO for the Sport class.
 */
public class SportDto {

    private String name;

    /**
     * Default constructor. Does nothing.
     */
    public SportDto() { }

    /**
     * Constructor of the DTO.
     * @param name The name of the sport
     */
    public SportDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }


    /**
     * Returns a new instance of the DTO, from a Sport object passed as argument.
     * @param sport Sport model
     * @return New SportDto intance
     */
    public static SportDto newInstance(final Sport sport) {
        return new SportDto(sport.getName());
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

        SportDto other = (SportDto) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}
