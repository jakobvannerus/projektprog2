//PROG2 VT2021, Inlämningsuppgift, del 2
//Grupp 042
//Ossian Däckfors osdc4143
//Jakob Vannerus java4663
//Sara Emnegard saem0275

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class Location extends Circle {

    private final String name;

    private boolean clicked = true;
    private boolean saved;

    public Location(String name, double x, double y) {
        super(x, y, 7, Color.BLUE);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isSaved() {
        return saved;
    }

    public void save(){
        saved = true;
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}