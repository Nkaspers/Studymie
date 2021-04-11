package org.semesterbreak.scenes.welcome;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdom2.JDOMException;
import org.semesterbreak.App;
import org.semesterbreak.Utilities;
import org.semesterbreak.scenes.editor.EditorController;

import java.io.IOException;

public class WelcomeController {
    @FXML
    private Button newProjectButton;
    @FXML
    private Button openProjectButton;

    public void initialize() {
        initializeIcons();
    }

    public void newProjectAction() {
        Stage mainStage = (Stage) newProjectButton.getScene().getWindow();
        try {
            FXMLLoader loader = App.fxmlLoader("editorview");
            mainStage.getScene().setRoot(loader.load());
            ((EditorController)loader.getController()).initializeData(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openProjectAction() {
        Stage mainStage = (Stage) newProjectButton.getScene().getWindow();
        try {
            FXMLLoader loader = App.fxmlLoader("editorview");
            mainStage.getScene().setRoot(loader.load());
            ((EditorController)loader.getController()).initializeData("/home/ben/Schreibtisch/untitled.stdmi");
        } catch (IOException e) {
            e.printStackTrace();
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
