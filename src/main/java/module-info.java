module org.semesterbreak {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdom2;
    requires java.desktop;
    requires javafx.web;
    requires jdk.jsobject;

    opens org.semesterbreak to javafx.fxml;
    exports org.semesterbreak;
}