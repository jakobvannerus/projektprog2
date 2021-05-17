//PROG2 VT2021, Inlämningsuppgift, del 2
//Grupp 042
//Ossian Däckfors osdc4143
//Jakob Vannerus java4663
//Sara Emnegard saem0275

public class Location extends javafx.scene.Node {

    private String name;
    private double x;
    private double y;
    private boolean changed = false;

    public Location(String name ,double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
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

    public void setName() {
        this.name = name;
    }

    @Override
    public javafx.scene.Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
