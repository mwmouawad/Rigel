package ch.epfl.rigel.structure;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * Implementation of the Trie or Prefix tree type data structure
 * using genericity to store objects and not just strings.
 *
 * @param <T> Type of the object needs to be a trieable object.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class Trie<T extends TrieableObject>  {

    private Node root;
    private boolean isCapitalSensitive;

    /**
     * Creates an empty trie.
     * @param capitalSensitive true if the trie should be capital sensitive.
     */
    public Trie(boolean capitalSensitive) {
        this.root = new Node();
        this.isCapitalSensitive = capitalSensitive;

    }

    /**
     * Puts a trieable object into the Trie instance
     * @param trieableObject  the object to insert.
     * @param <T> the type of the object the TrieableObject.
     */
    public  <T extends TrieableObject> void put(T trieableObject) {
        String word;
        if(!isCapitalSensitive)
            word = trieableObject.getNameTrieable().toLowerCase();
        else
            word = trieableObject.getNameTrieable();


        Node<T> current = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            Node<T> node = current.childrenMap.get(c);
            if (node == null) {
                node = new Node();
                current.childrenMap.put(c, node);
            }
            current = node;
        }
        // Set node as end node, and add a trieable associated object.
        current.end = true;
        current.endTrieableObject = trieableObject;
    }

    /**
     * Queries all children containing the prefix.
     * @param prefix the prefix to query with.
     * @param node the node to begin the query.
     * @param queryLimit the limit to the query.
     * @param <T> the type of the object the TrieableObject
     * @return a list with all query results trieableObjects.
     */
    private <T extends TrieableObject> List<T> queryAllChildren(String prefix, Node<T> node, int queryLimit) {
        List<T> r = new ArrayList<T>();
        if (node.end) {
            r.add(node.endTrieableObject);
        }
        for (Map.Entry<Character, Node<T>> child : node.childrenMap.entrySet()) {
            List<T> subSuffix = queryAllChildren(prefix + child.getKey(), child.getValue(), queryLimit);
            r.addAll(subSuffix);
            if(r.size() >= queryLimit){
                return r;
            }
        }
        return r;
    }

    /**
     * Queries for all objects in the trie containing the same prefix as input.
     * @param prefix the prefix to query.
     * @param queryLimit the limit of objects to the query stop.
     * @return a List containing all queried object containing the same prefix.
     */
    public List<T> query(String prefix, int queryLimit) {
        List<T> r = new ArrayList<T>();
        Node<T> current = root;
        if(!isCapitalSensitive)
            prefix = prefix.toLowerCase();
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            Node<T> node = current.childrenMap.get(c);
            if (node == null) {
                // Not found
                return r;
            }
            current = node;
        }

        return queryAllChildren(prefix, current, queryLimit);
    }


    /**
     * Node of the trie object.
     * @param <T>
     */
    public class  Node<T extends TrieableObject>  {
        private boolean end;
        private HashMap<Character, Node<T>> childrenMap;
        private T endTrieableObject;

        public Node()  {
            this.childrenMap = new HashMap<Character, Node<T>>();
            this.end = false;
        }



    }


}







