package uialgebra;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc Janßen on 14.05.2015.
 */
public class AdaptablePanel extends JPanel {

    private Map<String, UIAPanel> alternativeMap;

    public AdaptablePanel(UIAPanel mainAlternative) {
        alternativeMap = new HashMap<String, UIAPanel>();

        alternativeMap.put(mainAlternative.getID(), mainAlternative);

    }

    public void switchGUI(Dimension windowSize) {
        switchGUI(windowSize.getWidth(), windowSize.getHeight());
    }

    public void switchGUI(double width, double height) {

    }
}
