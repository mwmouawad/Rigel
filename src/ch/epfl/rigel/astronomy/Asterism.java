package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a group of stars.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class Asterism {

    private final List<Star> list;

    /**
     * Stores the stars in a new copy.
     * @param stars
     * @throws  IllegalArgumentException if the list is empty.
     */
    public Asterism(List<Star> stars){
        Preconditions.checkArgument(!stars.isEmpty());
        this.list = List.copyOf(stars);
    }

    /**
     * Returns the list of stars of the class
     * @return the list of stars.
     */

    public List<Star> stars(){ return list; }


}
