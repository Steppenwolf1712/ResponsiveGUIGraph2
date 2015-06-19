package uialgebra;

import nz.ac.auckland.alm.swing.ALMLayout;
import uialgebra.Exceptions.MalFormedUIA_Exception;
import uialgebra.Exceptions.UIAStringNotSoundException;
import uialgebra.algebra.UIATokenElement;
import uialgebra.algebra.UIAlgebra;
import uialgebra.algebra.string.UIAStringChecker;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the UserInterfaceAlgebra Panel.
 * It contains a set of at least one GUI definition in UserInterfaceAlgebra with a set of Constrains belonging to it.
 * This panel implements the onresize() method, to switch to the GUi with the highest Value.
 *
 * Created by Marc Janßen on 13.05.2015.
 */
public class UIAPanel extends JPanel {

    private final String identifier;
    private UIAlgebra contentAlgebra = null;
    private Dimension optimalSize;

    private ALMLayout layout = null;
    
    private boolean isInitialized = false;
    private boolean containsAreas = false;

    public UIAPanel(String name, UIAlgebra uiaString) {
        super(true);

        identifier = name;
        contentAlgebra = uiaString;
        
        init();
    }

    private void init() {
        layout = new ALMLayout();
        setLayout(layout);

        try {
            if (isSound()) {
                createAreas();
                calcOptimalSze();

                isInitialized = true;
            }
        } catch (UIAStringNotSoundException e) {
            // TODO: Needs a serious implementation, like showing failureMSG with Dialog or some Kind of that
            e.printStackTrace();
        } catch (MalFormedUIA_Exception e) {
            // TODO: Needs a serious implementation, like showing failureMSG with Dialog or some Kind of that
            e.printStackTrace();
        }
    }

    private void createAreas() {
        UIATokenElement[] elems = contentAlgebra.getElements();
        containsAreas = true;
    }

    public String getID() {
        return identifier;
    }

    public boolean isSound() throws UIAStringNotSoundException, MalFormedUIA_Exception {
        return UIAStringChecker.getInstance().checkUIAString(contentAlgebra.getDefinition());
    }

    private void calcOptimalSze() {
        // TODO: waiting for an serious implementation
        if (!containsAreas) {
            optimalSize = new Dimension(-1,-1);
            return;
        }
        optimalSize = new Dimension(100,100);
    }

    public Dimension getOptimalSize() {
        return optimalSize;
    }
}
