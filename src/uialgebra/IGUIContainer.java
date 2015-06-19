package uialgebra;

import javax.swing.*;

/**
 * Created by Marc Janßen on 13.05.2015.
 */
public interface IGUIContainer {

    /**
     * This method returns the UIAlgebraPanel, which contains in itself a set of GUI Definitions.
     *
     * @return
     */
    public JPanel getPanel();

    /**
     * This method gives information about the state of the GUI-definitions inside.
     * It returns false, if on or more definitions will produce an irregular GUI with overlaps or Gaps
     *
     * @return
     */
    public boolean isSound();


}
