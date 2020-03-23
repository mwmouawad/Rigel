package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Star catalogue.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class StarCatalogue {
    final private List<Star> stars;
    final private HashMap<Asterism, List<Integer>> catalogue;

    /**
     * Constructs a catalogue of stars and corresponding asterisms.
     *
     * @param stars     stars list.
     * @param asterisms asterisms list.
     * @throws IllegalArgumentException if a star in one of the asterisms is not contained in the stars list.
     */
    StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        this.catalogue = new HashMap<Asterism, List<Integer>>();

        //Check if there is a star in the asterisms  that is not in the stars list.
        for (Asterism ast : asterisms) {
            ArrayList<Integer> indexList = new ArrayList<>();
            List<Star> astStars = ast.stars();
            for (Star s : astStars) {

                int index = stars.indexOf(s);
                Preconditions.checkArgument(index != -1);

                //Add index in which star is.
                indexList.add(index);

            }
            //TODO : make immutable with a map, construct it, then catalogue = unmodifiable;
            this.catalogue.put(new Asterism(ast.stars()), indexList);
        }

        this.stars = List.copyOf(Objects.requireNonNull(stars));

    }

    public List<Star> stars() {
        //TODO: Add smth to make immutable?
        return this.stars;
    }

    public Set<Asterism> asterisms() {
        return this.catalogue.keySet();
    }

    /**
     * Get the index list of the stars for the input astermism.
     * @param asterism
     * @return index list of stars.
     * @throws IllegalArgumentException if the asterism is not contained in the instance.
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        List<Integer> indexList = this.catalogue.get(asterism);
        //TODO: We need to make sure no null is stored in the catalogue.
        Preconditions.checkArgument(indexList != null);

        return indexList;

    }

    /**
     * A builder intended to be used for manipulating it's associated star catalogue.
     *
     * @author Mark Mouawad (296508)
     * @author Leah Uzzan (302829)
     */
    public static final class Builder {
        //TODO : faux, creer attributs stars et asterisms.
        /**
         * Add star to the star catalogue.
         *
         * @param star star to be added to.
         * @return the builder instance.
         */
        public Builder addStar(Star star) {
            this.stars().stars.add(star);
            return this;
        }


        /**
         * Add an asterism to the catalogue.
         * All stars in the asterism must be in the star catalogue.
         *
         * @param asterism asterism to be added to.
         * @return the builder instance.
         */
        public Builder addAsterism(Asterism asterism) {
            ArrayList<Integer> starIndex = new ArrayList<Integer>();
            int indexOfStar;
            //Get the index of the stars in the star catalogue.
            for (Star s : asterism.stars()) {
                indexOfStar = this.starCatalogue.stars().indexOf(s);
                if (indexOfStar != -1)
                    //TODO: throw an error or smth?
                    starIndex.add(this.starCatalogue.stars().indexOf(s));

            }
            this.starCatalogue.catalogue.put(new Asterism(asterism.stars()), starIndex);
            return this;
        }

        /**
         * Returns an unmodifiable view of the stars catalogue.
         *
         * @return
         */
        public List<Star> stars() {
            return Collections.unmodifiableList(this.starCatalogue.stars);
        }


        /**
         * Returns an undomifiable view to the asterisms catalogue.
         *
         * @return
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(
                    new ArrayList<Asterism>(this.starCatalogue.asterisms())
            );
        }

        /**
         * Loads an input stream from a given loader.
         *
         * @param inputStream input stream to be loaded.
         * @param loader      loader to be used.
         * @return
         * @throws IOException
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * Build the star catalogue.
         *
         * @return
         */
        public StarCatalogue build() {
            //TODO: Is it making it immutable?
            StarCatalogue starCatalogue = new StarCatalogue(this.stars(), this.asterisms());
            return starCatalogue;
        }

    }


    public interface Loader {
        void load(InputStream inputStream, Builder builder) throws IOException;
    }
}
