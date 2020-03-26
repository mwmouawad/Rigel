package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Represents a list of Stars.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class Asterism {

    private final List<Star> list;

    /**
     * Creates a new List of stars,
     * @param stars
     * @throws  IllegalArgumentException if the list is empty.
     */
    public Asterism(List<Star> stars){
        //TODO : check condition, requirenonnull ?
        Preconditions.checkArgument(!stars.isEmpty());
        this.list = List.copyOf(stars);
    }

    //TODO: should this be copyOf or unmodifiable view?

    /**
     *
     * @return the list of stars.
     */
    public List<Star> stars(){ return list; }


}
