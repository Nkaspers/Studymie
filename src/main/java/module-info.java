module org.semesterbreak {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdom2;
    requires java.desktop;
    requires javafx.web;
    requires jdk.jsobject;
    requires com.google.gson;

    opens org.semesterbreak.scenes.editor to javafx.fxml;
    opens org.semesterbreak.scenes.playmode to javafx.fxml;
    opens org.semesterbreak.scenes.welcome to javafx.fxml;
    opens org.semesterbreak to com.google.gson;
    exports org.semesterbreak;
}