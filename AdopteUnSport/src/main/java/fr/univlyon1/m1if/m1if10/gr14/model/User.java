package fr.univlyon1.m1if.m1if10.gr14.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import fr.univlyon1.m1if.m1if10.gr14.model.constraint.Username;


/**
 * Represent the table "users" in database.
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "User.getAllUsers",
        query = "SELECT u FROM User u"),

    @NamedQuery(name = "User.getUserByUsername",
        query = "SELECT u FROM User u "
            + "WHERE LOWER(u.username) = LOWER(:username)")
})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Email(message = "Email incorrect.") //Constraint validation
    private String email;

    @Column(unique = true)
    @Username //Constraint validation
    private String username;

    //No constraint validation for the moment
    private String password;

    /**
     * Default constructor. Does nothing.
     */
    public User() {
    }

    /**
     * Constructor of User.
     * Email is stored in lowercases.
     * @param email    The email of the user
     * @param username the username of the user
     * @param password The password of the user
     */
    public User(final String email, final String username, final String password) {
        this.email = email.toLowerCase();
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        final User other = (User) obj;
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
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

}
