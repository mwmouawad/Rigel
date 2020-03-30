package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

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
    final private List<Star> starsCatalogue;
    final private Map<Asterism, List<Integer>> catalogue;

    /**
     * Constructs a catalogue of stars and corresponding asterisms.
     *
     * @param stars     stars list.
     * @param asterisms asterisms list.
     * @throws IllegalArgumentException if a star in one of the asterisms is not contained in the stars list.
     */
    StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        Map<Star, Integer> starIndexMap = new HashMap<Star, Integer>();
        Map<Asterism, List<Integer>> catalogue = new HashMap<Asterism, List<Integer>>();

        for(Star s: stars){
            starIndexMap.put(s, starIndexMap.size());
        }

        int starIndex;
        List<Integer> indexList = new ArrayList<Integer>();

        for(Asterism ast: asterisms){
            indexList.clear();
            for(Star s: ast.stars()){
                starIndex = Objects.requireNonNull(starIndexMap.get(s));
                indexList.add(starIndex);
            }
            //TODO : not necessary to add copyOf ?
            catalogue.put(new Asterism(ast.stars()), List.copyOf(indexList));
        }


        this.catalogue = Collections.unmodifiableMap(catalogue);
        this.starsCatalogue = List.copyOf(Objects.requireNonNull(stars));

    }

    public List<Star> stars() {
        return this.starsCatalogue;
    }

    public Set<Asterism> asterisms() {
        return this.catalogue.keySet();
    }

    /**
     * Get the index list of the stars for the input astermism.
     *
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
         * @return
         */
        public StarCatalogue build() {
            StarCatalogue starCatalogue = new StarCatalogue(this.stars, this.asterisms);
            return starCatalogue;
        }

    }


    public interface Loader {

        /**Loads from input file in the input stream.
         * @param inputStream inputstream associated with the file.
         * @param builder   star catalogue builder with stars loaded.
         * @throws IOException
         * */
        void load(InputStream inputStream, Builder builder) throws IOException;
    }
}
