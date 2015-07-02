package uialgebra.responsiveGUIGraph;

import nz.ac.auckland.alm.swing.ALMLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Marc Janßen on 30.06.2015.
 */
public interface IResponsivePart {

    public double compareToSize(Dimension dim);

    public Dimension getDesiredSize();

    public ALMLayout getLayout(JFrame frame);
}
