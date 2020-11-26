package fr.univlyon1.m1if.m1if10.gr14.dto;

import fr.univlyon1.m1if.m1if10.gr14.model.User;


/**
 * DTO for the User class.
 */
public class UserDto {
    private String email;
    private String username;

    /**
     * Default constructor. Does nothing.
     */
    public UserDto() { }

    /**
     * Constructor of the DTO.
     * @param email Email of the user
     * @param username Username of the user
     */
    public UserDto(final String email, final String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public String toString() {
        return this.username;
    }


    /**
     * Returns a new instance of the DTO, from a User object passed as argument.
     * @param user User model
     * @return New UserDto instance
     */
    public static UserDto newInstance(final User user) {
        return new UserDto(user.getEmail(), user.getUsername());
    }


    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }

        UserDto other = (UserDto) object;

        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }

        return true;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }
}
