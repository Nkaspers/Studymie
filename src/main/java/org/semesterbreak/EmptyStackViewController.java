package org.semesterbreak;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
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
    private Button editButton;

    @FXML
    private ListView<?> stacksListView;

    @FXML
    void projectBtn(ActionEvent event) {

    }

    public void initialize(){
        try{
            expandButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/stack_icon.svg"));
        }

     catch (JDOMException | IOException e) {
         e.printStackTrace();
     }
        prepareSlide();
    }

    private void prepareSlide(){

        AtomicBoolean navClosed = new AtomicBoolean(true);
        TranslateTransition openNav=new TranslateTransition(new Duration(350), navList);
        openNav.setByX(300);
        TranslateTransition closeNav=new TranslateTransition(new Duration(350), navList);


        expandButton.setOnAction((ActionEvent evt)->{
            if(navClosed.get()){
                openNav.play();
                System.out.println("to Right");
                navClosed.set(false);
            }else{
                closeNav.setByX(-MenuVBox.getWidth());
                closeNav.play();
                System.out.println("to Left");
                navClosed.set(true);
            }
        });

    }
}

