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

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        double height = Screen.getPrimary().getBounds().getHeight() * 0.8;
        double width = Screen.getPrimary().getBounds().getWidth() * 0.7;
        //for this to have effect, remove borderpane prefWidth,height attributes
        //scene = new Scene(loadFXML("editorview"), width, height);

        scene = new Scene(loadFXML("welcomeview"), width, height);

        //scene = new Scene(loadFXML("emptystackview"), width, height);

        stage.setScene(scene);
        stage.setTitle("Microbiology.stdmi*");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}