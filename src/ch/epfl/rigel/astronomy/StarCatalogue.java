package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 * A catalogue of stars and asterisms. Stores an ordered list of all the stars,
 * and different asterisms containing some or all of this stars.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class StarCatalogue {
    private final List<Star> starList;
    private final Map<Asterism, List<Integer>> catalogue;

    /**
     * Constructs a catalogue of stars and corresponding its corresponding asterisms.
     * The stars list  and the asterisms are mapped with the corresponding index of the stars in the
     * stars list they contain.
     *
     * @param stars     stars list.
     * @param asterisms asterisms list.
     * @throws IllegalArgumentException if a star in one of the asterisms is not contained in the stars list.
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        HashMap<Star, Integer> starIndexMap = new HashMap<Star, Integer>();
        Map<Asterism, List<Integer>> catalogue = new HashMap<Asterism, List<Integer>>();

        for(Star s: stars){
            starIndexMap.put(s, starIndexMap.size());
        }

        int starIndex;
        List<Integer> indexList = new ArrayList<Integer>();

        for(Asterism ast: asterisms){
            indexList.clear();
            for(Star s: ast.stars()){
                starIndex = (starIndexMap.getOrDefault(s, -1));
                Preconditions.checkArgument(starIndex >= 0);
                indexList.add(starIndex);
            }
            catalogue.put(ast, List.copyOf(indexList));
        }


        this.catalogue = Collections.unmodifiableMap(catalogue);
        this.starList = List.copyOf(Objects.requireNonNull(stars));

    }

    /**
     * Returns the star list.
     * @return the star list.
     */
    public List<Star> stars() {
        return this.starList;
    }

    /**
     * Returns a set of all asterisms.
     * @return a set of all asterims.
     */
    public Set<Asterism> asterisms() {
        return this.catalogue.keySet();
    }

    /**
     * Returns a list with the index of the stars for the input astermism.
     *
     * @param asterism
     * @return list with the index of the stars in the stars list.
     * @throws IllegalArgumentException if the asterism is not stored in the StarCatalogue
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        List<Integer> indexList = this.catalogue.get(asterism);
        //We need to make sure no null is stored in the catalogue.
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
        private List<Star> stars = new ArrayList<Star>();
        private List<Asterism> asterisms = new ArrayList<Asterism>();

        /**
         * Add star to the star catalogue.
         *
         * @param star star to be added to.
         * @return the builder instance.
         */
        public Builder addStar(Star star) {
            this.stars.add(star);
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
            this.asterisms.add(asterism);
            return this;
        }

        /**
         * Returns an unmodifiable view of the stars catalogue.
         *
         * @return
         */
        public List<Star> stars() {
            return Collections.unmodifiableList(this.stars);
        }


        /**
         * Returns an undomifiable view to the asterisms catalogue.
         *
         * @return
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(
                    this.asterisms
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
         * @return Star Catalogue
         */
        public StarCatalogue build() {
            return new StarCatalogue(this.stars, this.asterisms);
        }

    }

    /**
     * Represents a loader that allows to load an input file.
     */
    public interface Loader {

        /**Loads from input file in the input stream.
         * @param inputStream inputstream associated with the file.
         * @param builder   star catalogue builder with stars loaded.
         * @throws IOException
         * */
        void load(InputStream inputStream, Builder builder) throws IOException;
    }
}
