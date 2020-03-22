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


    public Asterism(List<Star> stars){
        Preconditions.checkArgument(!stars.isEmpty());
        this.list = List.copyOf(Objects.requireNonNull(stars));
    }

    //TODO: should this be copyOf or unmodifiable view?
    public List<Star> stars(){ return list; }
}
