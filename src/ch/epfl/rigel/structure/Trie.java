package ch.epfl.rigel.structure;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * Implementation of the Trie or Prefix tree type data structure
 * using genericity.
 *
 * @param <T> Type of the object needs to be a trieable object.
 *
 * @author Mark Mouawad (296508)
 * @author Leah Uzzan (302829)
 */
public class Trie<T extends TrieableObject>  {

    private Node root;
    private boolean isCapitalSensitive;

    public Trie(boolean capitalSensitive) {
        this.root = new Node();
        this.isCapitalSensitive = capitalSensitive;

    }

    public  <T extends TrieableObject> void insert(T trieableObject) {
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
        // Set end to true
        current.end = true;
        current.endTrieableObject = trieableObject;
    }

    public void insert(String word) {
         word = word.toLowerCase();
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
        // Set end to true
        current.end = true;
    }



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

    public List<T> query(String str, int queryLimit) {
        List<T> r = new ArrayList<T>();
        Node<T> current = root;
        str = str.toLowerCase();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            Node<T> node = current.childrenMap.get(c);
            if (node == null) {
                // Not found
                return r;
            }
            current = node;
        }

        return queryAllChildren(str, current, queryLimit);
    }




    public class  Node<T extends TrieableObject>  {
        private boolean end;
        private HashMap<Character, Node<T>> childrenMap;
        private T endTrieableObject;

        public Node()  {
            this.childrenMap = new HashMap<Character, Node<T>>();
            this.end = false;
        }


        public HashMap<Character, Node<T>> getChildrenMap() {
            return childrenMap;
        }
    }


}







