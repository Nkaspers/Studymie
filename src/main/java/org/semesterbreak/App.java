package org.semesterbreak;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private Scene editorScene;
    private Scene welcomeScene;
    private Scene playScene;

    @Override
    public void start(Stage stage) throws IOException {
        double height = Screen.getPrimary().getBounds().getHeight() * 0.8;
        double width = Screen.getPrimary().getBounds().getWidth() * 0.7;
        //for this to have effect, remove borderpane prefWidth,height attributes

        welcomeScene = new Scene(loadFXML("welcomeview"), width, height);
        editorScene = new Scene(loadFXML("editorview"), width, height);
        playScene = new Scene(loadFXML("playmodeview"), width, height);

        stage.setScene(welcomeScene);
        stage.setTitle("Untitled.stdmi");
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}