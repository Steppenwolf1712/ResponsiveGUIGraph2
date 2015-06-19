package uialgebra;

/**
 * Created by Marc Janßen on 15.05.2015.
 */
public interface IGUIGrid {

    public UIAPanel getAlternativeGUI(double targetWidth, double targetHeight);

    public void addUIAPanel(UIAPanel panel);
    public void addUIAPanel(UIAPanel panel, double desiredWidth, double desiredHeight);

}
