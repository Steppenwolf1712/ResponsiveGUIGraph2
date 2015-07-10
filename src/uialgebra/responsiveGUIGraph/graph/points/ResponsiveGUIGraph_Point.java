package uialgebra.responsiveGUIGraph.graph.points;

import com.sun.istack.internal.Nullable;
import nz.ac.auckland.alm.swing.ALMLayout;
import uialgebra.algebra.UIAlgebra;
import uialgebra.responsiveGUIGraph.IResponsivePart;
import uialgebra.responsiveGUIGraph.graph.ResponsiveGUIGraph;
import uialgebra.responsiveGUIGraph.graph.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by Marc Janßen on 20.06.2015.
 */
public class ResponsiveGUIGraph_Point extends Abstract_Graph_Point implements IResponsivePart {

    private final ResponsiveGUIGraph m_parent_Graph;
    private UIAlgebra m_algebra;
    private JFrame m_view;

    public ResponsiveGUIGraph_Point(ResponsiveGUIGraph parent, UIAlgebra algebra) {
        this(parent, algebra, null);
    }

    public ResponsiveGUIGraph_Point(ResponsiveGUIGraph parent, UIAlgebra algebra,@Nullable JFrame frame) {
        m_parent_Graph = parent;
        m_algebra = algebra;

        if (frame == null) {
            m_view = new JFrame(ResponsiveGUIGraph.S_UIALGEBRA_FRAME_NAME);
            algebra.createALEditor(m_view);
        } else
            m_view = frame;

        this.color_Point = new Color(0, 0, 180, 180);
        this.color_Point_Highlighted = new Color(150, 130, 0, 180);

        m_view.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        m_view.pack();
    }

    public Dimension getDesiredSize() {
        // TODO: Here it is to test, whether the preferredSize of the ContentPane satisfies my needs
        m_view.pack();
        return m_view.getContentPane().getPreferredSize();
    }

    String getStringDefinition() {
        return this.m_algebra.getDefinition().toString();
    }


    public Container showGUI(Point p) {
        this.m_view.setLocation((int)p.getX()-(this.m_view.getWidth()/2),(int)p.getY()-(this.m_view.getHeight()/2));
        this.m_view.setVisible(true);
        this.m_view.pack();
        return m_view;
    }

    @Override
    public ALMLayout getLayout(JFrame frame) {
        return this.m_algebra.createALEditor(frame);
    }
}
