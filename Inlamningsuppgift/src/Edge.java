import java.io.*;
import java.util.Objects;

public class Edge<T> implements Serializable {


    private static final int MINIMUM_WEIGHT = 0;
    private Object destination;
    private String name;
    private int weight;

    public Edge(Object destination, String name, int weight) {
        this.destination = destination;
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if (weight < MINIMUM_WEIGHT) {
            throw new IllegalArgumentException("Invalid weight");
        } else {
            this.weight = weight;

        }
    }

    public Object getDestination(){
        return destination;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Edge){
            Edge r = (Edge) other;
            return this.getDestination() == r.getDestination();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination);
    }

    @Override
    public String toString() {
        return "to " + destination +
                " by " + name +
                " takes " + weight;
    }
}
