import java.io.*;

public class Edge<T> implements Serializable {


    private static final int MINIMUM_WEIGHT = 0;
    private Object node;
    private String name;
    private int weight;
    private ListGraph<T> destination;

    public Edge(Object node, String name, int weight) {

        this.node = node;
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

        }
    }

    public Object getDestination(Object node){
        return destination;
    }

}
