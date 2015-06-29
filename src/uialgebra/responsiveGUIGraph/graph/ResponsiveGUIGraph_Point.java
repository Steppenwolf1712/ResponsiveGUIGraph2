package uialgebra.responsiveGUIGraph.graph;

import com.sun.istack.internal.Nullable;
import nz.ac.auckland.alm.swing.ALMLayout;
import uialgebra.algebra.UIAlgebra;
import uialgebra.responsiveGUIGraph.IResponsivePart;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by Marc Janßen on 20.06.2015.
 */
class ResponsiveGUIGraph_Point implements IResponsivePart {

    private final ResponsiveGUIGraph m_parent_Graph;
    private UIAlgebra m_algebra;
    private JFrame m_view;

    private boolean m_selected = false;


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

        m_view.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        m_view.pack();
    }

    public void setSelected(boolean selected) {
        m_selected = selected;
    }

    public boolean isSelected() {
        return m_selected;
    }

    public Dimension getDesiredSize() {
        // TODO: Here it is to test, whether the preferredSize of the ContentPane satisfies my needs
        return m_view.getContentPane().getPreferredSize();
    }

    public void drawPoint(Point loc, Graphics2D g2) {
        Point p1 = new Point((int)loc.getX()-8, (int)loc.getY()),
                p2 = new Point((int)loc.getX(),(int)loc.getY()+8),
                p3 = new Point((int)loc.getX()+8,(int)loc.getY()),
                p4 = new Point((int)loc.getX(),(int)loc.getY()-8);
        GeneralPath p0 = new GeneralPath();
        p0.moveTo(p1.getX(), p1.getY());
        p0.lineTo(p2.getX(), p2.getY());
        p0.lineTo(p3.getX(), p3.getY());
        p0.lineTo(p4.getX(), p4.getY());
        p0.closePath();
        if (m_selected)
            g2.setColor(ResponsiveGUIGraph.COLOR_POINT_HIGHLIGHTED);
        else
            g2.setColor(ResponsiveGUIGraph.COLOR_POINT);
        g2.fill(p0);
        g2.setColor(ResponsiveGUIGraph.COLOR_GRAPH);
        g2.draw(p0);
    }

    public void showGUI(Point p) {
        this.m_view.setLocation((int)p.getX()-(this.m_view.getWidth()/2),(int)p.getY()-(this.m_view.getHeight()/2));
        this.m_view.setVisible(true);
    }

    @Override
    public double compareToSize(Dimension dim) {
        Vector2D toCompare = new Vector2D(dim);
        Vector2D ownSize = new Vector2D(getDesiredSize());
        return ownSize.sub(toCompare).getQuadLength();
    }

    @Override
    public ALMLayout getLayout() {
        return (ALMLayout)this.m_view.getContentPane().getLayout();
    }
}
