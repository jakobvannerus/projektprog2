//PROG2 VT2021, Inlämningsuppgift, del 2
//Grupp 042
//Ossian Däckfors osdc4143
//Jakob Vannerus java4663
//Sara Emnegard saem0275

public class PathChart<T> {

    private int weight = Integer.MAX_VALUE;
    private boolean bestPathFound;
    private T whereFrom;

    public int getWeight() {
        return weight;
    }

    public T getWhereFrom() {
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

    public void setWhereFrom(T whereFrom) {
        this.whereFrom = whereFrom;
    }
}