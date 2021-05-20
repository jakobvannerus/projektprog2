//PROG2 VT2021, Inlämningsuppgift, del 2
//Grupp 042
//Ossian Däckfors osdc4143
//Jakob Vannerus java4663
//Sara Emnegard saem0275

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Location extends Canvas {

    private String name;
    private Circle circle;
    private Color color;
    private boolean clicked = true;
    private boolean changed = false;

    public Location(String name, Circle circle) {
        this.name = name;
        this.circle = circle;
    }

    public String getName() {
        return name;
    }

    public boolean getChanged() {
        return changed;
    }

    public void setName() {
        this.name = name;

    }

    public Circle getCircle(){
        return circle;
    }

    public boolean getClicked() {
        return clicked;
    }

    public void isClicked() {
        if (clicked) {
            circle.setFill(Color.RED);
        } else {
            circle.setFill(Color.BLUE);
        }
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

    /*class ColorChange implements EventHandler<MouseEvent> {
        public void change() {
            if (circle.getFill() == Color.BLUE) {
                circle.setFill(Color.RED);
            } else {
                circle.setFill(Color.BLUE);
            }
        }
        @Override
        public void handle(MouseEvent mouseEvent) {

        }
    }*/
}