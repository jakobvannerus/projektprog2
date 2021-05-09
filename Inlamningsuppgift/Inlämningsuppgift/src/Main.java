import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;

public class Main extends Application {

    private Button pathFinderButton = new Button("Find Path");
    private Button showConnectionButton = new Button("Show Connection");
    private Button newPlaceButton = new Button("New Place");
    private Button newConnectionButton = new Button("New Connection");
    private Button changeConnectionButton = new Button("Change Connection");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        Pane center = new Pane();
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

        FlowPane buttons = new FlowPane();
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().add(pathFinderButton);
        buttons.getChildren().add(showConnectionButton);
        buttons.getChildren().add(newPlaceButton);
        buttons.getChildren().add(newConnectionButton);
        buttons.getChildren().add(changeConnectionButton);
        vbox.getChildren().add(buttons);

        Image europe = new Image("/lägg/in/fil/här.jpg");
        ImageView imageView = new ImageView(europe);
        root.getChildren().add(imageView);

    }
}