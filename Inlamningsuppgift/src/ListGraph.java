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
        noNodeElement(node1, node2);
        if (weight < MINIMUM_WEIGHT) {
            throw new IllegalArgumentException("Weight cannot be negative");
        } else if (directConnectionExists(node1, node2)) {
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
        noNodeElement(node1, node2);
        if (!directConnectionExists(node1, node2)) {
            throw new NoSuchElementException("Element does not exist");
        } else if (weight < MINIMUM_WEIGHT) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
        getEdgeBetween(node1, node2).setWeight(weight);
        getEdgeBetween(node2, node1).setWeight(weight);

    }

    @Override
    public Set getNodes() {
        return nodes.keySet();
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(Object node) {
        noNodeElement(node);
        return (Collection)nodes.get(node);
    }

    @Override
    public Edge<T> getEdgeBetween(Object node1, Object node2) {
        noNodeElement(node1, node2);
        if (directConnectionExists(node1, node2)) {
            throw new IllegalStateException("Connection already exists");
        }
        for (Edge e : nodes.get(node1)){
            if (e.getDestination() == node2){
                return e;
            }
        }
        return null;

    }

    @Override
    public void disconnect(Object node1, Object node2) {
        noNodeElement(node1, node2);
        if (directConnectionExists(node1, node2)) {
            throw new IllegalStateException("Connection does not exists");
        }
        remove(getEdgeBetween(node1, node2));
        remove(getEdgeBetween(node2, node1));
    }

    @Override
    public void remove(Object node) {
        noNodeElement(node);
        for (Edge e1 : nodes.get(node)){
            for (Edge e2 : nodes.get(e1.getDestination())){
                if (e2.getDestination() == e1){
                    remove(e2);
                    break;
                }
            }
        }
        nodes.remove(node);
    }

    private void depthFirstSearch(Object node, Set<Object> visited) {
        visited.add(node);
        for (Edge e : nodes.get(node)) {
            if (!visited.contains(e.getDestination())) {
                depthFirstSearch(e.getDestination(), visited);
            }
        }
    }

    @Override
    public boolean pathExists(Object from, Object to) {
        Set<Object> visited = new HashSet<>();
        depthFirstSearch(from, visited);
        return visited.contains(to);
    }

    // anyPath!
//    @Override
//    public List<Edge<T>> getPath(Object from, Object to) {
//        Set<Object> visited = new HashSet<>();
//        Map<Object, Object> connected = new HashMap<>();
//        depthFirstSearch(from, null, visited, connected);
//        if (!visited.contains(to)) {
//            return null;
//        }
//        else return gatherPath(from, to, connected);
//    }


    private void depthFirstSearch(Object where, Object whereFrom,
                                  Set<Object> visited, Map<Object, Object> connected){
        visited.add(where);
        connected.put(where, whereFrom);
        for (Edge e : nodes.get(where)){
            if (!visited.contains(e.getDestination())){
                depthFirstSearch(e.getDestination(), where, visited, connected);
            }
        }
    }
//    shortestPath
//    @Override
//    public List<Edge<T>> getPath(Object from, Object to) {
//        LinkedList<Object> queue = new LinkedList<>();
//        Set<Object> visited = new HashSet<>();
//        Map<Object, Object> connected = new HashMap<>();
//        visited.add(from);
//        queue.addLast(from);
//        while (!queue.isEmpty()){
//            Object whereFrom = queue.pollFirst();
//            for (Edge e : nodes.get(whereFrom)){
//                Object where = e.getDestination();
//                if (!visited.contains(where)){
//                    visited.add(where);
//                    queue.addLast(where);
//                    connected.put(where, whereFrom);
//                }
//            }
//        }
//        if (!visited.contains(to)) {
//            return null;
//        }
//        else return gatherPath(from, to, connected);
//    }

    //    fastestPath
    @Override
    public List<Edge<T>> getPath(Object from, Object to){
        Set<Object> visited = new HashSet<>();
        Map<Object, PathChart> chart = new HashMap<>();
        depthFirstSearch(from, to, visited);
        if (!visited.contains(to)){
            return null;
        }
        for (Object o : visited){
            chart.put(o, new PathChart());
        }
        Object whereFrom = from;
        chart.get(from).setWeight(0);
        boolean morePotentialPaths = true;
        while (morePotentialPaths){
            chart.get(whereFrom).bestPathHasBeenFound();
            for(Edge e : nodes.get(whereFrom)){
                if(chart.get(whereFrom).getWeight() + e.getWeight() < chart.get(e.getDestination()).getWeight()){
                    chart.get(e.getDestination()).setWeight(chart.get(whereFrom).getWeight() + e.getWeight());
                    chart.get(e.getDestination()).setWhereFrom(whereFrom);
                }
            }
            int lowestWeight = Integer.MAX_VALUE;
            morePotentialPaths = false;
            for (Object o : visited){
                if(!chart.get(o).isBestPathFound()){
                    if(chart.get(o).getWeight() < lowestWeight){
                        lowestWeight = chart.get(o).getWeight();
                        whereFrom = chart.get(o);
                        morePotentialPaths = true;

                    }
                }
            }
        }
        List<Edge<T>> path = new ArrayList<>();
        Object where = to;
        while (!where.equals(from)){
            whereFrom = chart.get(where).getWhereFrom();
            Edge e = getEdgeBetween(whereFrom, where);
            path.add(e);
            where = whereFrom;
        }
        Collections.reverse(path);
        return path;

    }

    private void depthFirstSearch(Object where, Object whereFrom, Set<Object> visited){
        visited.add(where);
        for (Edge e : nodes.get(where)){
            if (!visited.contains(e.getDestination())){
                depthFirstSearch(e.getDestination(), where, visited);
            }
        }
    }

    private List<Edge<T>> gatherPath(Object from, Object to, Map<Object, Object> connected){
        List<Edge<T>> path = new ArrayList<>();
        Object where = to;
        while (!where.equals(from)){
            Object whereFrom = connected.get(where);
            Edge e = getEdgeBetween(whereFrom, where);
            path.add(e);
            where = whereFrom;
        }
        Collections.reverse(path);
        return path;
    }


    private boolean directConnectionExists(Object node1, Object node2){
        boolean connectionExist = false;
        for (Edge e : nodes.get(node1)){
            if (e.getDestination() == node2){
                connectionExist = true;
            }
        }
        return  connectionExist;
    }

    private void noNodeElement(Object node){
        if (!nodes.containsKey(node)) {
            throw new NoSuchElementException("Element does not exist");
        }
    }
    private void noNodeElement(Object node1, Object node2){
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("Element does not exist");
        }
    }
}