module org.semesterbreak {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdom2;

    opens org.semesterbreak to javafx.fxml;
    exports org.semesterbreak;
}