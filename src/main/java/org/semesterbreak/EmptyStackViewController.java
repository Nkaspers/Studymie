package org.semesterbreak;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.geometry.Pos;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.control.ListView;
        import javafx.scene.layout.AnchorPane;
        import javafx.scene.layout.VBox;
        import javafx.util.Duration;
        import org.jdom2.JDOMException;
        import javafx.animation.TranslateTransition;

        import java.io.IOException;
        import java.util.concurrent.atomic.AtomicBoolean;

public class EmptyStackViewController {

    @FXML
    private Button expandButton;

    @FXML
    private AnchorPane navList;

    @FXML
    private Button projectButton;

    @FXML
    private Button exportButton;

    @FXML
    private VBox MenuVBox;

    @FXML
    private Label textLabel;

    @FXML
    private Button editButton;

    @FXML
    private ListView<String> stacksListView;

    @FXML
    void projectBtn(ActionEvent event) {

    }

    public void initialize(){
        try{
            expandButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/stack_icon.svg"));
            exportButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/export.svg"));
            editButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/edit.svg"));
            textLabel.setAlignment(Pos.CENTER);
        }

     catch (JDOMException | IOException e) {
         e.printStackTrace();
     }
        prepareSlide();

        //stacksListView.setItems(FlashcardManager.getStackList());
        stacksListView.getItems().addAll("Biology", "LudS", "Lineare Algebra");
    }

    private void prepareSlide(){

        AtomicBoolean navClosed = new AtomicBoolean(true);
        TranslateTransition openNav=new TranslateTransition(new Duration(350), navList);
        openNav.setByX(300);
        TranslateTransition closeNav=new TranslateTransition(new Duration(350), navList);


        expandButton.setOnAction((ActionEvent evt)->{
            if(navClosed.get()){
                //to right transition
                openNav.play();
                navClosed.set(false);
            }else{
                //to left transition
                closeNav.setByX(-MenuVBox.getWidth());
                closeNav.play();
                navClosed.set(true);
            }
        });

    }
}

