package org.semesterbreak;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.ListView;
        import javafx.scene.layout.AnchorPane;
        import javafx.util.Duration;
        import org.jdom2.JDOMException;
        import javafx.animation.TranslateTransition;

        import java.io.IOException;

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

        TranslateTransition openNav=new TranslateTransition(new Duration(350), navList);
        openNav.setToX(0);
        TranslateTransition closeNav=new TranslateTransition(new Duration(350), navList);
        expandButton.setOnAction((ActionEvent evt)->{
            if(navList.getTranslateX()!=0){
                openNav.play();
                //System.out.println("to Right");
            }else{
                closeNav.setToX(-(navList.getWidth()));
                closeNav.play();
                //System.out.println("to Left");
            }
        });

    }
}

