package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public final class StarCatalogue {
    static final class Builder{
        Builder(){

        }

        Builder addStar(Star star){
            return null;
        }

    }

    private List<Star> stars;
    private List<Asterism> asterisms;

    StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        this.stars = Objects.requireNonNull(stars);


    }

    public interface Loader {
        void load(InputStream inputStream, Builder builder) throws IOException;
    }
}
