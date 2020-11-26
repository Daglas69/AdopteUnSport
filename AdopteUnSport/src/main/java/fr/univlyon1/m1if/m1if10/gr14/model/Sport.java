package fr.univlyon1.m1if.m1if10.gr14.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import fr.univlyon1.m1if.m1if10.gr14.model.constraint.Text;

/**
 * Represent the table "sports" in database.
 */
@Entity
@Table(name = "sports")
@NamedQuery(name = "Sport.getAllSports",
    query = "SELECT s FROM Sport s")
public class Sport implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Text
    private String name;

    /**
     * Default constructor. Does nothing.
     */
    public Sport() { }

    /**
     * Constructor of Sport.
     * @param name The name of the sport
     */
    public Sport(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = Objects.hashCode(this.name);
        return hash;
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
        final Sport other = (Sport) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
