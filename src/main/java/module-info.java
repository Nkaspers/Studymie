module org.semesterbreak {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdom2;
    requires java.desktop;
    requires javafx.web;
    requires jdk.jsobject;

    opens org.semesterbreak.scenes.editor to javafx.fxml;
    opens org.semesterbreak.scenes.playmode to javafx.fxml;
    opens org.semesterbreak.scenes.welcome to javafx.fxml;
    exports org.semesterbreak;
}