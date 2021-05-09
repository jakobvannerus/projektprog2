//PROG2 VT2021, Inlämningsuppgift, del 1
//Grupp 042
//Ossian Däckfors osdc4143
//Jakob Vannerus java4663
//Sara Emnegard saem0275

import java.util.*;
import java.io.*;

public class Edge<T> implements Serializable {

    private static final int MINIMUM_WEIGHT = 0;
    private T destination;
    private String name;
    private int weight;

    public Edge(T node, String name, int weight) {
        this.name = name;
        this.weight = weight;
        this.destination = node;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public T getDestination() {
        return destination;
    }

    public void setWeight(int weight) {
        if (weight < MINIMUM_WEIGHT) {
            throw new IllegalArgumentException("Invalid weight");
        } else {
            this.weight = weight;
        }
    }

    public String toString() {
        return String.format(" till " + destination +" med %s tar %d", name, weight);
    }
}