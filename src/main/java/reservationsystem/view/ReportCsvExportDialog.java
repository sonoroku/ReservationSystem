package reservationsystem.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Path;

public class ReportCsvExportDialog {

    public Path chooseDestination(Window owner, String initialFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Report as CSV");
        fileChooser.setInitialFileName(initialFileName);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files", "*.csv")
        );

        File selectedFile = fileChooser.showSaveDialog(owner);
        return selectedFile == null ? null : selectedFile.toPath();
    }

    public boolean confirmOverwrite(Window owner, Path destination) {
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "The file already exists. Replace it?\n" + destination,
                ButtonType.YES,
                ButtonType.NO
        );
        confirmation.setTitle("Confirm Report Export");
        confirmation.setHeaderText("Replace existing CSV file?");
        if (owner != null) {
            confirmation.initOwner(owner);
        }
        return confirmation.showAndWait().orElse(ButtonType.NO)
                == ButtonType.YES;
    }
}
