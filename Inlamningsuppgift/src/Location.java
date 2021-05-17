//PROG2 VT2021, Inlämningsuppgift, del 2
//Grupp 042
//Ossian Däckfors osdc4143
//Jakob Vannerus java4663
//Sara Emnegard saem0275

public class Location extends javafx.scene.Node {

    private double x;
    private double y;
    private boolean changed = false;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean getChanged() {
        return changed;
    }

    @Override
    public javafx.scene.Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
