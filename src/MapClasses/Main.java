package MapClasses;

import java.util.ArrayList;

import MapClasses.Graphics.ControlPanel;
import MapClasses.Graphics.MapWindow;
import MapClasses.Graphics.ViewCanvas;

public class Main {

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Stack");
        ViewCanvas viewCanvas = new ViewCanvas(800, 1000, new Navigation());
        MapWindow map = new MapWindow("Boogle Maps", viewCanvas, new ControlPanel(viewCanvas));
        // define start node
        // use hashmap to store nodes and nodes to store paths
        // node class
        // path class
    }
}
