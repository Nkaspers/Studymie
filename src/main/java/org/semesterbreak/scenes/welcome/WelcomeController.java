package org.semesterbreak.scenes.welcome;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jdom2.JDOMException;
import org.semesterbreak.App;
import org.semesterbreak.Utilities;
import org.semesterbreak.scenes.editor.EditorController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class WelcomeController {
    @FXML
    private Button newProjectButton;
    @FXML
    private Button openProjectButton;

    private Parent editorView;

    private FXMLLoader loader;

    public void initialize() {
        initializeIcons();
        try {
            loader = App.fxmlLoader("editorview");
            editorView = loader.load();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void newProjectAction() {
        Stage mainStage = (Stage) newProjectButton.getScene().getWindow();
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Paths.get(System.getProperty("user.home")).toFile());
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter(".stdmi","*.stdmi"));
        File file = chooser.showSaveDialog(mainStage);

        if(file == null) return;
        if(!file.getPath().endsWith(".stdmi")) file = new File(file.getPath() + ".stdmi");

        try {
            Files.createFile(Paths.get(file.getPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There was a problem with your file");
            return;
        }

        mainStage.getScene().setRoot(editorView);
        ((EditorController)loader.getController()).initializeData(file.getPath());
    }

    public void openProjectAction() {
        Stage mainStage = (Stage) newProjectButton.getScene().getWindow();
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(Paths.get(System.getProperty("user.home")).toFile());
        chooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter(".stdmi","*.stdmi"));
        File file = chooser.showOpenDialog(mainStage);
        if(file != null) {
            mainStage.getScene().setRoot(editorView);
            ((EditorController) loader.getController()).initializeData(file.getPath());
        }
    }

    private void initializeIcons() {
        try {
            Group stackIcon = Utilities.getIconGroup("add_stack_white.svg", true);
            var stackAnchorpane = ((AnchorPane) newProjectButton.getGraphic());
            Label stackGraphic = (Label) stackAnchorpane.getChildren().get(0);
            stackIcon.setScaleX(1.5);
            stackIcon.setScaleY(1.5);
            stackGraphic.setGraphic(stackIcon);

            Group openIcon = Utilities.getIconGroup("open.svg", true);
            var openAnchorpane = ((AnchorPane) openProjectButton.getGraphic());
            Label openGraphic = (Label) openAnchorpane.getChildren().get(0);
            openGraphic.setScaleX(1.5);
            openGraphic.setScaleY(1.5);
            openGraphic.setGraphic(openIcon);
        }catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }
}
