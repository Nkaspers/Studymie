package org.semesterbreak;
import javafx.scene.Group;
import javafx.scene.shape.SVGPath;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class Utilities {

    public static String defaultIconPath = "src/main/resources/org/semesterbreak/icons/";

    public static Group getIconGroup(String iconName, boolean defaultPath) throws JDOMException, IOException{
        return getIconGroup(defaultIconPath+iconName);
    }

    public static Group getIconGroup(String filepath) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(new File(filepath));
        Element root = doc.getRootElement();
        Group iconGroup = new Group();
        List<Element> pathList = root.getChildren();
        for(Element path:pathList){
            iconGroup.getChildren().add(createPath(path));
        }
        return iconGroup;
    }

    private static SVGPath createPath(Element pathElement) {
        SVGPath path = new SVGPath();
        StringBuilder attributesSb = new StringBuilder();
        for(Attribute attribute: pathElement.getAttributes()){
            if(attribute.getName().equals("d")) path.setContent(attribute.getValue());
            String string = "-fx-" + attribute.getName() + ":" + attribute.getValue() + ";";
            attributesSb.append(string);
        }
        path.setStyle(attributesSb.toString());
        return path;
    }

}
