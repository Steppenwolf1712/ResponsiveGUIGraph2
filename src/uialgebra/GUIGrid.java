package uialgebra;

import java.util.HashMap;
import java.util.Map;

/**
 * This class saves a grid of Window-Sizes and maps them to certain GUI-identifier.
 * The main purpose is, that this class can decide, which GUI is to choose at a certain size.
 *
 * Created by Marc Janßen on 15.05.2015.
 */
public class GUIGrid implements IGUIGrid {

    private Map<Double[], UIAPanel> panelMap = null;



    public GUIGrid(UIAPanel panel) {
        this(panel, panel.getOptimalSize().getWidth(), panel.getOptimalSize().getHeight());
        panelMap = new HashMap<Double[], UIAPanel>();

        Double[] temp = {panel.getSize().getWidth(),panel.getSize().getHeight()};
        panelMap.put(temp, panel);
    }

    public GUIGrid(UIAPanel panel, double desiredWidth, double desiredHeight) {

    }

    @Override
    public UIAPanel getAlternativeGUI(double targetWidth, double targetHeight) {
        return null;
    }

    @Override
    public void addUIAPanel(UIAPanel panel) {

    }

    @Override
    public void addUIAPanel(UIAPanel panel, double desiredWidth, double desiredHeight) {

    }
}
