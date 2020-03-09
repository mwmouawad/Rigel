package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;
import java.util.Objects;


public final class Asterism {

    private List<Star> list;

    //TODO check
    public Asterism(List<Star> stars){
        Preconditions.checkArgument(stars.isEmpty());
        list = Objects.requireNonNull(stars);
    }

    public List<Star> stars(){ return list; }
}
