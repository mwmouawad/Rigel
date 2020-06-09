package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.structure.Trie;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * City Catalogue storing a Trie and a HashMap.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class CityCatalogue {

    private final HashMap<Integer, City> cityCatalogue;
    private final Trie<City> prefixTree;


    /**
     * Constructs a city catalogue.
     * @param cityCatalogue hash map mapping the geoname id and the city.
     * @param prefixTree tries containg all.
     */
    public CityCatalogue(HashMap<Integer, City> cityCatalogue, Trie prefixTree){
        this.cityCatalogue = cityCatalogue;
        this.prefixTree = prefixTree;
    }

    /**
     * Queries a prefix in the trie.
     * @param query
     * @param queryLimit
     * @return a list with the result cities from the query.
     */
    public List<City> search(String query, int queryLimit){
        List<City> result = this.prefixTree.query(query, queryLimit);

        if(result == null)
            System.out.println("null");
        return result == null ? new ArrayList<City>() : List.copyOf(result);
    }

    /**
     * Get using geonames id.
     * @param id
     * @return
     */
    public City get(int id){
        return this.cityCatalogue.get(id);
    }


    /**
     * A builder intended to be used constructing a city catalogue.
     *
     * @author Mark Mouawad (296508)
     * @author Leah Uzzan (302829)
     */
    public static final class Builder {

        private final HashMap<Integer, City> cityHashMap = new HashMap<Integer, City>();
        private final Trie prefixTree = new Trie(false);


        public Builder addCity(City city){
            this.cityHashMap.put(city.getId(), city);
            this.prefixTree.put(city);
            return this;
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
         * Build the city catalogue.
         *
         * @return City Catalogue
         */
        public CityCatalogue build() {
            return new CityCatalogue(cityHashMap, prefixTree);
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
