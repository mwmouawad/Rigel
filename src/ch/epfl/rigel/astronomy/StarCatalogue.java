package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Star catalogue.
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public final class StarCatalogue {
    final private List<Star> stars;
    final private HashMap<Asterism, List<Integer>> catalogue;

    /**
     * If all
     * @param stars stars list.
     * @param asterisms
     * @throws IllegalArgumentException if a star in asterisms is not contained in the stars list.
     */
    StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        this.catalogue = new HashMap<Asterism, List<Integer>>();

        //TODO: Make sure this is immutable
        //Check if there is a star in the asterisms  that is not in the stars list.
        for(Asterism ast : asterisms){
            ArrayList<Integer> indexList = new ArrayList<Integer>();
            List<Star> iterateAst = ast.stars();
            for(var i = 0; i < iterateAst.size(); i++){

                int index = stars.indexOf(iterateAst.get(i));

                Preconditions.checkArgument( index != -1);

                //Add index in which star is.
                indexList.add(index);

            }
            //We have to make it inmutable.
            this.catalogue.put( new Asterism(ast.stars()), indexList);
        }


        this.stars = Objects.requireNonNull(stars);
        //Chose a HashSet over TreeSet because order is not important

    }

    public List<Star> stars(){
        //TODO: Add smth to make immutable?
        return this.stars;
    }

    public Set<Asterism> asterisms(){
        return this.catalogue.keySet();
    }

    /**
     * Get the index list of the stars for the input astermism.
     * @param asterism
     * @return index list of stars.
     * @throws IllegalArgumentException if the asterism is not contained in the instance.
     */
    public List<Integer> asterismIndices(Asterism asterism){
        List<Integer> indexList = this.catalogue.get(asterism);

        //TODO: We need to make sure no null is stored in the catalogue.
        Preconditions.checkArgument(indexList != null);

        return indexList;

    }

    static final class Builder{
        Builder(){

        }

        Builder addStar(Star star){
            return null;
        }

    }




    public interface Loader {
        void load(InputStream inputStream, Builder builder) throws IOException;
    }
}
