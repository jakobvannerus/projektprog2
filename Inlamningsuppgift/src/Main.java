//PROG2 VT2021, Inlämningsuppgift, del 2
//Grupp 042
//Ossian Däckfors osdc4143
//Jakob Vannerus java4663
//Sara Emnegard saem0275

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;

public class Main extends Application {

    private Button pathFinderButton = new Button("Find Path");
    private Button showConnectionButton = new Button("Show Connection");
    private Button newPlaceButton = new Button("New Place");
    private Button newConnectionButton = new Button("New Connection");
    private Button changeConnectionButton = new Button("Change Connection");

    public static void main(String[] args) {
        launch(args);
    }

    private Stage stage;
    private boolean changed = false;
    private BorderPane root = new BorderPane();
    Pane center = new Pane();

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        root.setCenter(center);

        Scene s = new Scene(root);
        stage.setTitle("PathFinder");
        stage.setScene(s);
        stage.show();

        VBox vbox = new VBox();
        root.setTop(vbox);
        MenuBar menu = new MenuBar();
        vbox.getChildren().add(menu);
        Menu fileMenu = new Menu("File");
        menu.getMenus().add(fileMenu);
        MenuItem newMapItem = new MenuItem("New Map");
        fileMenu.getItems().add(newMapItem);
        newMapItem.setOnAction(new NewMapHandler());
        MenuItem openItem = new MenuItem("Open");
        fileMenu.getItems().add(openItem);
        openItem.setOnAction(new OpenHandler());
        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().add(saveItem);
        MenuItem saveImageItem = new MenuItem("Save Image");
        fileMenu.getItems().add(saveImageItem);
        saveImageItem.setOnAction(new SaveImageHandler());
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitItem);

        FlowPane buttons = new FlowPane();
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().add(pathFinderButton);
        buttons.getChildren().add(showConnectionButton);
        buttons.getChildren().add(newPlaceButton);
        newPlaceButton.setOnAction(new NewPlaceHandler());
        buttons.getChildren().add(newConnectionButton);
        buttons.getChildren().add(changeConnectionButton);
        vbox.getChildren().add(buttons);
    }

    class NewPlaceHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent mouseEvent) {
            center.setOnMouseClicked(new ClickHandler());
            newPlaceButton.setDisable(true);
            center.setCursor(Cursor.CROSSHAIR);
        }
    }

    class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle (MouseEvent mouseEvent) {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            Location n = new Location(x, y);
            center.getChildren().add(n);
            center.setOnMouseClicked(null);
            newPlaceButton.setDisable(false);
            center.setCursor(Cursor.DEFAULT);
            changed = true;
        }
    }

    class NewMapHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Image europe = new Image("file:/Users/ossiandackfors/Documents/DSV SpelUtveckling/VT2021/Programering 2/projektprog2/Inlamningsuppgift/src/europa.gif");
            ImageView imageView = new ImageView(europe);
            center.getChildren().add(imageView);
            stage.sizeToScene();
        }
    }

    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {

        }
    }

    class SaveImageHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                WritableImage image = center.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", new File("capture.png"));
            } catch (IOException e) { 
                Alert a = new Alert(Alert.AlertType.ERROR, "IO-Error: " + e.getMessage());
                a.showAndWait();
            }
        }
    }

   private boolean changed() {
        if (changed) {
            return true;
        } else {
            for (Node n : center.getChildren()) {
                if (((Location)n).getChanged()) {
                    return true;
                }
            }
        } return false;
    }
}
