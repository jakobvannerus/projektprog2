//PROG2 VT2021, Inlämningsuppgift, del 1
//Grupp 042
//Ossian Däckfors osdc4143
//Jakob Vannerus java4663
//Sara Emnegard saem0275

public class PathChart {

    private int weight = Integer.MAX_VALUE;
    private boolean bestPathFound;
    private Object whereFrom;

    public int getWeight() {
        return weight;
    }

    public Object getWhereFrom() {
        return whereFrom;
    }

    public boolean isBestPathFound() {
        return bestPathFound;
    }

    public void bestPathHasBeenFound() {
        bestPathFound = true;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setWhereFrom(Object whereFrom) {
        this.whereFrom = whereFrom;
    }
}