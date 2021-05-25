import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class Dialog extends Alert {

    private TextField nameField = new TextField();
    private TextField timeField = new TextField();

    public Dialog() {
        super(AlertType.INFORMATION);
        this.nameField = nameField;
        this.timeField = timeField;
        GridPane grid = new GridPane();
        grid.addRow(0, new Label("Name: "), nameField);
        grid.addRow(1, new Label("Time: "), timeField);
        getDialogPane().setContent(grid);
    }

    public String getNameField() {
        return nameField.getText();
    }

    public int getTimeField() {
        return Integer.parseInt(timeField.getText());
    }


}
