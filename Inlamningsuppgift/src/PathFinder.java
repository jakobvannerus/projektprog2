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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;
import javafx.stage.WindowEvent;

public class PathFinder extends Application {

    private Button findPathButton = new Button("Find Path");
    private Button showConnectionButton = new Button("Show Connection");
    private Button newPlaceButton = new Button("New Place");
    private Button newConnectionButton = new Button("New Connection");
    private Button changeConnectionButton = new Button("Change Connection");
    private ListGraph<Location> listGraph = new ListGraph<>();
    private HashSet<Line> lines = new HashSet<>();
    private Stage stage;
    private boolean changed;
    private BorderPane root = new BorderPane();
    private Pane center;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        center = new Pane();
        this.stage = stage;
        root.setCenter(center);
        center.setId("outputArea");

        VBox vbox = new VBox();
        root.setTop(vbox);
        MenuBar menu = new MenuBar();
        vbox.getChildren().add(menu);
        menu.setId("menu");
        Menu fileMenu = new Menu("File");
        menu.getMenus().add(fileMenu);
        fileMenu.setId("menuFile");
        MenuItem newMapItem = new MenuItem("New Map");
        fileMenu.getItems().add(newMapItem);
        newMapItem.setId("menuNewMap");
        newMapItem.setOnAction(new NewMapHandler());
        MenuItem openItem = new MenuItem("Open");
        fileMenu.getItems().add(openItem);
        openItem.setId("menuOpenFile");
        openItem.setOnAction(new OpenHandler());
        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().add(saveItem);
        saveItem.setId("menuSaveFile");
        saveItem.setOnAction(new SaveHandler());
        MenuItem saveImageItem = new MenuItem("Save Image");
        fileMenu.getItems().add(saveImageItem);
        saveImageItem.setId("menuSaveImage");
        saveImageItem.setOnAction(new SaveImageHandler());
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitItem);
        exitItem.setId("menuExit");
        exitItem.setOnAction(new ExitItemHandler());

        FlowPane buttons = new FlowPane();
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().add(findPathButton);
        findPathButton.setId("btnFindPath");
        findPathButton.setOnAction(new FindPathHandler());
        buttons.getChildren().add(showConnectionButton);
        showConnectionButton.setId("btnShowConnection");
        showConnectionButton.setOnAction(new ShowConnectionHandler());
        buttons.getChildren().add(newPlaceButton);
        newPlaceButton.setId("btnNewPlace");
        newPlaceButton.setOnAction(new NewPlaceHandler());
        buttons.getChildren().add(newConnectionButton);
        newConnectionButton.setId("btnNewConnection");
        newConnectionButton.setOnAction(new NewConnectionHandler());
        buttons.getChildren().add(changeConnectionButton);
        changeConnectionButton.setId("btnChangeConnection");
        changeConnectionButton.setOnAction(new ChangeConnectionHandler());
        vbox.getChildren().add(buttons);

        Scene scene = new Scene(root);
        stage.setTitle("PathFinder");
        stage.setScene(scene);
        stage.setOnCloseRequest(new ExitHandler());
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
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
                Location l = new Location(answer.get(), x, y);
                l.setId(l.getName());
                center.getChildren().add(l);
                listGraph.add(l);
                l.setId(l.getName());
                l.setOnMouseClicked(new ColorHandler());
            }
        }
    }

    class ColorHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Location l = (Location)mouseEvent.getSource();
            int counter = 0;
            for (Location locations : listGraph.getNodes()){
                if (locations.getFill() == Color.RED)
                    counter++;
            }
            if (l.getFill() == Color.BLUE  &&  counter < 2) {
                l.setFill(Color.RED);
            } else {
                l.setFill(Color.BLUE);
            }
        }
    }

    class FindPathHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Location l1 = null;
            Location l2 = null;
            for (Location l : listGraph.getNodes()) {
                if (l.getFill() == Color.RED) {
                    if (l1 != null) {
                        l2 = l;
                    } else {
                        l1 = l;
                    }
                }
            } if (l1 == null || l2 == null) {
                Alert placeAlert = new Alert(Alert.AlertType.ERROR, "Two places must be selected!");
                placeAlert.showAndWait();
            } try {
                List<Edge<Location>> pathList = listGraph.getPath(l1, l2);
                if (pathList == null || pathList.isEmpty()) {
                    Alert noPathsAlert = new Alert(Alert.AlertType.ERROR, "No paths exist!");
                    noPathsAlert.showAndWait();
                } else {
                    int totalTime = 0;
                    TextArea textArea = new TextArea();
                    Dialog dialog = new Dialog(textArea);
                    dialog.setHeaderText("The path from " + l1.getName() + " to " + l2.getName());
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Edge<Location> e : pathList) {
                        totalTime += e.getWeight();
                        stringBuilder.append("to ").append(e.getDestination().getName()).append(" by ").append(e.getName()).append(" takes ").append(e.getWeight()).append("\n");
                    }
                    stringBuilder.append("Total ").append(totalTime);
                    textArea.setText(stringBuilder.toString());
                    dialog.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class NewConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Location l1 = null;
            Location l2 = null;
            for (Location l : listGraph.getNodes()) {
                if (l.getFill() == Color.RED) {
                    if (l1 != null) {
                        l2 = l;
                    } else {
                        l1 = l;
                    }
                }
            }
            if (l1 == null || l2 == null) {
                Alert placeAlert = new Alert(Alert.AlertType.ERROR, "Two places must be selected!");
                placeAlert.showAndWait();
            } else {
                if (listGraph.getEdgeBetween(l1, l2) != null) {
                    Alert connectionAlert = new Alert(Alert.AlertType.ERROR, "Connection already exists!");
                    connectionAlert.showAndWait();
                } else {
                    Dialog dialog = new Dialog();
                    dialog.setHeaderText("Connection from " + l1.getName() + " to " + l2.getName());
                    dialog.showAndWait();
                    String nameAnswer = "";
                    int timeAnswer = 0;
                    nameAnswer = dialog.getNameField();
                    timeAnswer = dialog.getTimeField();
                    if (!nameAnswer.isEmpty()) {
                        listGraph.connect(l1, l2, nameAnswer, timeAnswer);
                        Line line = new Line(l1.getCenterX(), l1.getCenterY(), l2.getCenterX(), l2.getCenterY());
                        line.setStroke(Color.BLACK);
                        lines.add(line);
                        center.getChildren().add(line);
                        line.setDisable(true);
                    }
                }
            }
        }
    }

    class NewMapHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            listGraph = new ListGraph<>();
            Image europe = new Image("file:europa.gif");
            ImageView imageView = new ImageView(europe);
            center.getChildren().add(imageView);
            stage.sizeToScene();
            listGraph.getNodes().clear();
        }
    }

    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (!saved()) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Unsaved changes, exit anyway?");
                Optional<ButtonType> answer = a.showAndWait();
                if (answer.isPresent() && (answer.get() == ButtonType.CANCEL)) {
                    return;
                }
            }
                try {
                    listGraph = new ListGraph<>();
                    FileReader fileReader = new FileReader("europa.graph");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line = bufferedReader.readLine();

                    Image europe = new Image(line);
                    ImageView imageView = new ImageView(europe);
                    center.getChildren().add(imageView);
                    stage.sizeToScene();
                    line = bufferedReader.readLine();
                    if (line != null) {
                        String[] split = line.split(";");
                        int counter = 0;
                        while (counter < split.length) {
                            Location l = new Location(split[counter++], Double.parseDouble(split[counter++]), Double.parseDouble(split[counter++]));
                            l.save();
                            listGraph.add(l);
                            l.setId(l.getName());
                            center.getChildren().add(l);
                            l.setOnMouseClicked(new ColorHandler());
                        }
                        line = bufferedReader.readLine();
                        while (line != null) {
                            split = line.split(";");
                            Location l1 = null;
                            Location l2 = null;
                            for (Location l : listGraph.getNodes()) {
                                if (l.getName().equals(split[0])) {
                                    l1 = l;
                                    break;
                                }
                            }
                            for (Location l : listGraph.getNodes()) {
                                if (l.getName().equals(split[1])) {
                                    l2 = l;
                                    break;
                                }
                            }
                            if (listGraph.getEdgeBetween(l1, l2) == null) {
                                listGraph.connect(l1, l2, split[2], Integer.parseInt(split[3]));
                                Line mapLine = new Line(l1.getCenterX(), l1.getCenterY(), l2.getCenterX(), l2.getCenterY());
                                mapLine.setStroke(Color.BLACK);
                                lines.add(mapLine);
                                center.getChildren().add(mapLine);
                                mapLine.setDisable(true);
                            }
                            line = bufferedReader.readLine();
                        }

                        fileReader.close();
                        bufferedReader.close();
                    }
                } catch (FileNotFoundException e) {
                    System.err.print("FILE ERROR: " + e.getMessage());
                } catch (IOException e) {
                    System.err.print("IO ERROR: " + e.getMessage());
                }

        }
    }

    class ShowConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Location l1 = null;
            Location l2 = null;
            for (Location l : listGraph.getNodes()) {
                if (l.getFill() == Color.RED) {
                    if (l1 != null) {
                        l2 = l;
                    } else {
                        l1 = l;
                    }
                }
            }
            if (l1 == null || l2 == null) {
                Alert placeAlert = new Alert(Alert.AlertType.ERROR, "Two places must be selected!");
                placeAlert.showAndWait();
            }
            if (!listGraph.directConnectionExists(l1, l2)) {
                Alert illegalStateAlert = new Alert(Alert.AlertType.ERROR, "Connection does not exist");
                illegalStateAlert.showAndWait();

            } else {
                if (l1 != null && l2 != null) {
                    TextField nameField = new TextField();
                    TextField timeField = new TextField();
                    Dialog dialog = new Dialog(nameField, timeField);
                    dialog.setHeaderText("Connection from " + l1.getName() + " to " + l2.getName());
                    nameField.setEditable(false);
                    timeField.setEditable(false);
                    nameField.setText(listGraph.getEdgeBetween(l1, l2).getName());
                    timeField.setText(listGraph.getEdgeBetween(l1, l2).getWeight() + "");
                    dialog.showAndWait();
                    }
                }
            }
        }

    class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            saveFile("europa.graph");
        }
    }

    class ChangeConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Location l1 = null;
            Location l2 = null;
            for (Location l : listGraph.getNodes()) {
                if (l.getFill() == Color.RED) {
                    if (l1 != null) {
                        l2 = l;
                    } else {
                        l1 = l;
                    }
                }
            }
            if (l1 == null || l2 == null) {
                Alert placeAlert = new Alert(Alert.AlertType.ERROR, "Two places must be selected!");
                placeAlert.showAndWait();

            } try {
                if (l1 != null && l2 != null) {

                    int timeAnswer = 0;
                    TextField nameField = new TextField();
                    TextField timeField = new TextField();
                    Dialog dialog = new Dialog(nameField, timeField);
                    dialog.setHeaderText("Connection from " + l1.getName() + " to " + l2.getName());
                    nameField.setEditable(false);
                    nameField.setText(listGraph.getEdgeBetween(l1, l2).getName());
                    dialog.showAndWait();
                    timeAnswer = dialog.getTimeField();
                    listGraph.setConnectionWeight(l1, l2, timeAnswer);
                }
            }catch (NumberFormatException e){
                Alert incorrectFormatAlert = new Alert(Alert.AlertType.ERROR, "Incorrect datatype");
                incorrectFormatAlert.showAndWait();
            }
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
            if (!saved()) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Unsaved changes, exit anyway?");
                Optional<ButtonType> answer = a.showAndWait();
                if (answer.isPresent() && answer.get() == ButtonType.CANCEL) {
                    windowEvent.consume();
                }
            }
        }
    }

    private boolean saved() {
        for (Location l : listGraph.getNodes()) {
            if (!l.isSaved())
                return false;
        }
        return true;
    }

    private Location getByCircle(Circle circle) {
        for (Location l : listGraph.getNodes()){
            if (l == circle)
                return l;
        }
        return null;
    }

    private static boolean isEqual()
    {
        try {
            if (Files.size(Path.of("control.graph")) != Files.size(Path.of("europa.graph"))) {
                return false;
            }

            byte[] first = Files.readAllBytes(Path.of("control.graph"));
            byte[] second = Files.readAllBytes(Path.of("europa.graph"));
            return Arrays.equals(first, second);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveFile(String file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("file:europa.gif");
            int counter = 0;
            for (Location l : listGraph.getNodes()) {
                printWriter.print(l.getName() + ";" + l.getCenterX() + ";" + l.getCenterY());
                counter++;
                l.save();
                if (counter < listGraph.getNodes().size()) {
                    printWriter.print(";");
                }
            }
            for (Location l : listGraph.getNodes()) {
                for (Edge<Location> e : listGraph.getEdgesFrom(l)) {
                    printWriter.print("\n" + l.getName() + ";" + e.getDestination().getName() + ";" + e.getName() + ";" + e.getWeight());
                }
            }
            printWriter.close();
            fileWriter.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("IO Error " + e.getMessage());
        }
    }
}