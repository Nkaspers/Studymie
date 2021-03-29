module org.semesterbreak {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.semesterbreak to javafx.fxml;
    exports org.semesterbreak;
}