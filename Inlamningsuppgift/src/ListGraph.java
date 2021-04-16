//PROG2 VT2021, Inlämningsuppgift, del 1
//Grupp 042
//Ossian Däckfors osdc4143
//Jakob Vannerus java4663
//Sara Emnegard saem0275

import java.util.*;
import java.io.*;

public class ListGraph<T> implements Graph, Serializable {

    private static final int MINIMUM_WEIGHT = 0;
    private Map<Object, Set<Edge>> nodes = new HashMap<>();

    @Override
    public void add(Object node) {
        nodes.putIfAbsent(node, new HashSet<>());
    }

    @Override
    public void connect(Object node1, Object node2, String name, int weight) {

        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("Element does not exist");
        } else if (weight < MINIMUM_WEIGHT) {
            throw new IllegalArgumentException("Weight cannot be negative");
        } else if (pathExists(node1, node2)) {
            throw new IllegalStateException("Connection already exists");
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
        if (!nodes.containsKey(node)) {
            throw new NoSuchElementException("Node does not exist");
        }
    }

    private void depthFirstSearch(Object node, Set<Object> visited) {
        visited.add(node);
        for (Edge e : nodes.get(node)) {
            if (!visited.contains(e.getDestination())) {
                depthFirstSearch(e.getDestination(), visited);
            } else {
                return;
            }
        }
    }

    @Override
    public boolean pathExists(Object from, Object to) {
        Set<Object> visited = new HashSet<>();
        depthFirstSearch(from, visited);
        return visited.contains(to);
    }

    @Override
    public List<Edge<T>> getPath(Object from, Object to) {
        return null;
    }
}
