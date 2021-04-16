import java.util.*;
import java.io.*;

public class ListGraph<T> implements Graph, Serializable {

    private Map<Object, Set<Edge>> nodes = new HashMap<>();

    @Override
    public void add(Object node) {
        nodes.putIfAbsent(node, new HashSet<>());
    }

    @Override
    public void connect(Object node1, Object node2, String name, int weight) {
        boolean alreadyExistingConnection = false;
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("Element does not exist");
        }
        if (weight < 0){
            throw new IllegalArgumentException("Weight is negativ");
        }
        for (Edge e : nodes.get(node1)){
            if (nodes.get(node2).contains(new Edge(node1, )))
        }
        Set<Edge> set1 = nodes.get(node1);
        Edge edge1 = new Edge (node2, name, weight);
        set1.add(edge1);
        Set<Edge> set2 = nodes.get(node2);
        Edge edge2 = new Edge (node1, name, weight);
        set2.add(edge2);

    }

    @Override
    public void setConnectionWeight(Object node1, Object node2, int weight) {

    }

    @Override
    public Set getNodes() {
        return null;
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(Object node) {
        return null;
    }

    @Override
    public Edge<T> getEdgeBetween(Object node1, Object node2) {
        return null;
    }

    @Override
    public void disconnect(Object node1, Object node2) {

    }

    @Override
    public void remove(Object node) {

    }

    @Override
    public boolean pathExists(Object from, Object to) {
        return false;
    }

    @Override
    public List<Edge<T>> getPath(Object from, Object to) {
        return null;
    }

    class WeightComparator implements Comparator<Edge>{
        public int compare(Edge e1, Edge e2){
            return e1.getWeight()- e2.getWeight();
        }
    }
}
