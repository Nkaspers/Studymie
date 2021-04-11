package org.semesterbreak.scenes.welcome;

import javafx.scene.Group;
import javafx.scene.control.Button;
import org.jdom2.JDOMException;
import org.semesterbreak.Utilities;

import java.io.IOException;

public class WelcomeController {

    public Button addStackButton;
    public Button openProjectButton;

    public void initialize() throws JDOMException, IOException {
        Group stackIcon = Utilities.getIconGroup("add_stack_white.svg", true);
        Group openIcon = Utilities.getIconGroup("open.svg", true);
        stackIcon.setScaleX(1.5);
        stackIcon.setScaleY(1.5);
        addStackButton.setGraphic(stackIcon);
        openProjectButton.setGraphic(openIcon);
    }
}
