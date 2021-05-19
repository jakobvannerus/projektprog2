//PROG2 VT2021, Inlämningsuppgift, del 1
//Grupp 042
//Ossian Däckfors osdc4143
//Jakob Vannerus java4663
//Sara Emnegard saem0275

import java.util.*;
import java.io.*;

public class ListGraph<T> implements Graph<T>, Serializable {

    private static final int MINIMUM_WEIGHT = 0;
    private final Map<T, Set<Edge<T>>> nodes = new HashMap<>();

    @Override
    public void add(T node) {
        nodes.putIfAbsent(node, new HashSet<>());
    }

    @Override
    public void connect(T node1, T node2, String name, int weight) {
        noNodeElement(node1, node2);
        if (weight < MINIMUM_WEIGHT) {
            throw new IllegalArgumentException("Weight cannot be negative");
        } else if (directConnectionExists(node1, node2)) {
            throw new IllegalStateException("Connection already exists");
        }
        Set<Edge<T>> set1 = nodes.get(node1);
        Edge<T> edge1 = new Edge<T> (node2, name, weight);
        set1.add(edge1);
        Set<Edge<T>> set2 = nodes.get(node2);
        Edge<T> edge2 = new Edge<T> (node1, name, weight);
        set2.add(edge2);
    }

    @Override
    public void setConnectionWeight(T node1, T node2, int weight) {
        noNodeElement(node1, node2);
        if (!directConnectionExists(node1, node2)) {
            throw new NoSuchElementException("Element does not exist");
        }
        getEdgeBetween(node1, node2).setWeight(weight);
        getEdgeBetween(node2, node1).setWeight(weight);
    }

    @Override
    public Set<T> getNodes() {
        return nodes.keySet();
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {
        noNodeElement(node);
        return nodes.get(node);
    }

    @Override
    public Edge<T> getEdgeBetween(T node1, T node2) {
        noNodeElement(node1, node2);
        for (Edge<T> e : nodes.get(node1)){
            if (e.getDestination() == node2){
                return e;
            }
        }
        return null;

    }

    @Override
    public void disconnect(T node1, T node2) {
        noNodeElement(node1, node2);
        if (!directConnectionExists(node1, node2)) {
            throw new IllegalStateException("Connection does not exist");
        }
        for (Edge<T> e : nodes.get(node1)){
            if (e.getDestination() == node2){
                nodes.get(node1).remove(e);
                break;
            }
        }
        for (Edge<T> e : nodes.get(node2)){
            if (e.getDestination() == node1){
                nodes.get(node2).remove(e);
                break;
            }
        }
    }


    @Override
    public void remove(T node) {
        noNodeElement(node);
        for (Edge<T> e1 : nodes.get(node)){
            for (Edge<T> e2 : nodes.get(e1.getDestination())){
                if (e2.getDestination() == node){
                    nodes.get(e1.getDestination()).remove(e2);
                    break;
                }
            }
        }
        nodes.remove(node);
    }

    @Override
    public boolean pathExists(T from, T to) {
        Set<T> visited = new HashSet<>();
        if (!nodes.containsKey(from) || !nodes.containsKey(to)){
            return false;
        }
        depthFirstSearch(from, visited);
        return visited.contains(to);
    }

    @Override
    public List<Edge<T>> getPath(T from, T to){
        Set<T> visited = new HashSet<>();
        Map<T, PathChart<T>> chart = new HashMap<>();
        depthFirstSearch(from, visited);
        if (!visited.contains(to)){
            return null;
        }
        for (T o : visited){
            chart.put(o, new PathChart<T>());
        }
        T whereFrom = from;
        chart.get(from).setWeight(0);

        dijkstrasAlgorithm(visited, chart, whereFrom);

        List<Edge<T>> path = new ArrayList<>();
        T where = to;
        while (!where.equals(from)){
            whereFrom = chart.get(where).getWhereFrom();
            Edge<T> e = getEdgeBetween(whereFrom, where);
            path.add(e);
            where = whereFrom;
        }
        Collections.reverse(path);
        return path;

    }

    private void dijkstrasAlgorithm(Set<T> visited, Map<T, PathChart<T>> chart, T whereFrom) {
        boolean morePotentialPaths = true;
        while (morePotentialPaths){
            chart.get(whereFrom).bestPathHasBeenFound();
            for(Edge<T> e : nodes.get(whereFrom)){
                if(chart.get(whereFrom).getWeight() + e.getWeight() < chart.get(e.getDestination()).getWeight()){
                    chart.get(e.getDestination()).setWeight(chart.get(whereFrom).getWeight() + e.getWeight());
                    chart.get(e.getDestination()).setWhereFrom(whereFrom);
                }
            }
            int lowestWeight = Integer.MAX_VALUE;
            morePotentialPaths = false;
            for (T o : visited){
                if(!chart.get(o).isBestPathFound()){
                    morePotentialPaths = true;
                    if(chart.get(o).getWeight() < lowestWeight){
                        lowestWeight = chart.get(o).getWeight();
                        whereFrom = o;
                    }
                }
            }
        }
    }

    private void depthFirstSearch(T where, T whereFrom,
                                  Set<T> visited, Map<T, T> connected){
        visited.add(where);
        connected.put(where, whereFrom);
        for (Edge<T> e : nodes.get(where)){
            if (!visited.contains(e.getDestination())){
                depthFirstSearch(e.getDestination(), where, visited, connected);
            }
        }
    }

    private void depthFirstSearch(T node, Set<T> visited) {
        visited.add(node);
        for (Edge<T> e : nodes.get(node)) {
            if (!visited.contains(e.getDestination())) {
                depthFirstSearch(e.getDestination(), visited);
            }
        }
    }

    private List<Edge<T>> gatherPath(T from, T to, Map<T, T> connected){
        List<Edge<T>> path = new ArrayList<>();
        T where = to;
        while (!where.equals(from)){
            T whereFrom = connected.get(where);
            Edge<T> e = getEdgeBetween(whereFrom, where);
            path.add(e);
            where = whereFrom;
        }
        Collections.reverse(path);
        return path;
    }


    private boolean directConnectionExists(T node1, T node2){
        boolean connectionExist = false;
        for (Edge<T> e : nodes.get(node1)){
            if (e.getDestination() == node2) {
                connectionExist = true;
                break;
            }
        }
        return  connectionExist;
    }

    private void noNodeElement(T node){
        if (!nodes.containsKey(node)) {
            throw new NoSuchElementException("Element does not exist");
        }
    }

    private void noNodeElement(T node1, T node2){
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("Element does not exist");
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : nodes.keySet()) {
            stringBuilder.append(o.toString() + ":\n");
            for (Edge<T> e : nodes.get(o)) {
                stringBuilder.append("\t" + e.toString() + "\n");
            }
        }
        return stringBuilder.toString();
    }

}