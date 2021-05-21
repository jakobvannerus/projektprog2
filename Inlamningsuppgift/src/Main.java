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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;
import javafx.stage.WindowEvent;

public class Main extends Application {

    private Button pathFinderButton = new Button("Find Path");
    private Button showConnectionButton = new Button("Show Connection");
    private Button newPlaceButton = new Button("New Place");
    private Button newConnectionButton = new Button("New Connection");
    private Button changeConnectionButton = new Button("Change Connection");
    private ListGraph<Location> listGraph = new ListGraph<>();
    private HashSet<Line> lines = new HashSet<>();
    private Stage stage;
    private boolean changed = false;
    private BorderPane root = new BorderPane();
    Pane center = new Pane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        root.setCenter(center);

        Scene scene = new Scene(root);
        stage.setTitle("PathFinder");
        stage.setScene(scene);
        stage.setOnCloseRequest(new ExitHandler());
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
        exitItem.setOnAction(new ExitItemHandler());

        FlowPane buttons = new FlowPane();
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().add(pathFinderButton);
        buttons.getChildren().add(showConnectionButton);
        buttons.getChildren().add(newPlaceButton);
        newPlaceButton.setOnAction(new NewPlaceHandler());
        buttons.getChildren().add(newConnectionButton);
        newConnectionButton.setOnAction(new NewConnectionHandler());
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
            center.setOnMouseClicked(null);
            newPlaceButton.setDisable(false);
            center.setCursor(Cursor.DEFAULT);
            changed = true;
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Name");
            dialog.setContentText("Name of place: ");
            Optional<String> answer = dialog.showAndWait();
            if (answer.isPresent()) {
                Location l = new Location(answer.get(), new Circle(x, y, 7.0f, Color.BLUE));
                center.getChildren().add(l.getCircle());
                listGraph.add(l);
                l.getCircle().setOnMouseClicked(new ColorHandler());
            }
        }
    }

    class ColorHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Circle c = (Circle)mouseEvent.getSource();
            Location l = getByCircle(c);
            int counter = 0;
            for (Location locations : listGraph.getNodes()){
                if (locations.getCircle().getFill() == Color.RED)
                    counter++;
            }
            if (l.getCircle().getFill() == Color.BLUE  &&  counter < 2) {
                l.getCircle().setFill(Color.RED);
            } else {
                l.getCircle().setFill(Color.BLUE);
            }
        }
    }

    class NewConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Location l1 = null;
            Location l2 = null;
            for (Location l : listGraph.getNodes()) {
                if (l.getCircle().getFill() == Color.RED) {
                    if (l1 != null) {
                        l2 = l;
                    } else {
                        l1 = l;
                    }
                }
            } if (l1 == null || l2 == null) {
                Alert a = new Alert(Alert.AlertType.ERROR,("Two places must be selected!"));
                a.showAndWait();
            } try {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Connection");
                dialog.setHeaderText("Connection from " + l1.getName() + " to " + l2.getName());
                dialog.setContentText("Name: \n \n Time: ");
                Optional<String> nameAnswer = dialog.showAndWait();
                Optional<Integer> timeAnswer = Optional.of(Integer.parseInt(dialog.showAndWait().get()));
                if (nameAnswer.isPresent() && timeAnswer.isPresent()) {
                    listGraph.connect(l1, l2, nameAnswer.get(), timeAnswer.get());
                    Line line = new Line(l1.getCircle().getCenterX(), l1.getCircle().getCenterY(), l2.getCircle().getCenterX(), l2.getCircle().getCenterY());
                    line.setStroke(Color.BLACK);
                    lines.add(line);
                    System.err.println(line.getEndX());
                    center.getChildren().add(line);

                }
            } catch (IllegalArgumentException i) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Incorrect datatype - " + i.getMessage());
                a.showAndWait();
            }
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

    class ExitItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    class ExitHandler implements EventHandler<WindowEvent> {
        @Override
        public void handle (WindowEvent windowEvent) {
            if (changed()) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Unsaved changes, exit anyway?");
                Optional<ButtonType> answer = a.showAndWait();
                if (answer.isPresent() && answer.get() == ButtonType.CANCEL) {
                    windowEvent.consume();
                }
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

    private Location getByCircle(Circle circle){
        for (Location l : listGraph.getNodes()){
            if (l.getCircle() == circle)
                return l;
        }
        return null;
    }
}