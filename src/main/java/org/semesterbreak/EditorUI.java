package org.semesterbreak;

import javafx.scene.Group;
import javafx.scene.shape.SVGPath;

public class EditorUI {
    public static Group projectButtonIcon = new Group(
            createPath("m 4,0.5 h 96 c 1.939,0 3.5,1.561 3.5,3.5 v 14.7547 c 0,1.939 -1.561,3.5 -3.5,3.5 H 4 c -1.939,0 -3.5,-1.561 -3.5,-3.5 V 4 C 0.5,2.061 2.061,0.5 4,0.5 Z"));
    public static Group undoButtonIcon = new Group(
            createPath("M20.5341 16.9914C20.5341 16.9914 20.6272 11.8897 16.7643 8.52405C12.9013 5.1584 7.36035 5.38154 7.36035 5.38154"),
            createPath("M8.48261 0.383606L0.818359 5.81647L8.67422 11.2066"));
    public static Group redoButtonIcon = new Group(
            createPath("M0.465917 16.9914C0.465917 16.9914 0.372827 11.8897 4.23574 8.52405C8.09866 5.1584 13.6396 5.38154 13.6396 5.38154"),
            createPath("M12.5174 0.383606L20.1816 5.81647L12.3258 11.2066"));
    public static Group exportButtonIcon = new Group(
            createPath("M27 9.90566L13.6089 19.8113L13.6089 0L27 9.90566Z"),
            createPath("M12.3887 5.54658C12.3887 5.54658 7.91025 4.74566 3.97266 8.2027C2.75486 9.51405 0.0464888 11.6893 0.000266075 18.5678C4.82798 13.8892 8.12351 14.2175 12.3236 14.2648L12.3887 5.54658Z"));
    public static Group addStackButtonIcon = new Group(
            createPath("M14.04 17.3849L27.1962 21.8981L14.04 28.8378L0.883789 21.8981L14.04 17.3849Z"),
            createPath("M14.04 12.2923L27.1962 17.3494L14.04 24.2891L0.883789 17.3494L14.04 12.2923Z"),
            createPath("M14.04 7.74707L27.1962 13.0955L14.04 20.0352L0.883789 13.0955L14.04 7.74707Z"),
            createPath("M24.6543 1.10257V9.22882"),
            createPath("M21.2842 5.18707H28.1623"));
    public static Group PlayButtonIcon = new Group(createPath("M1.5 2.73833V20.2617L15.2139 11.5L1.5 2.73833Z"));
    public static Group addFlashCardButtonIcon = new Group(
            createPath("M 0.5,0.5 H 34.2598 V 20.2547 H 0.5 Z"),
            createPath("M16.9248 7V13.8996"),
            createPath("M13 10.468H21.0087"));

    private static SVGPath createPath(String d) {
        SVGPath path = new SVGPath();
        path.getStyleClass().add("svg");
        path.setContent(d);
        return path;
    }
}
