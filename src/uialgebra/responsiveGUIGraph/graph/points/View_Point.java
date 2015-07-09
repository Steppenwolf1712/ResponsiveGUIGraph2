package uialgebra.responsiveGUIGraph.graph.points;

import uialgebra.responsiveGUIGraph.graph.ResponsiveGUIGraph;

import java.awt.*;

/**
 * Created by Marc Janßen on 09.07.2015.
 */
public class View_Point extends Abstract_Graph_Point {

    private Dimension m_dimension = null;
    private ResponsiveGUIGraph m_parent = null;

    public View_Point(ResponsiveGUIGraph parent, Dimension dim) {
        m_parent = parent;
        m_dimension = dim;

        this.color_Point = new Color(150,150,150);
        this.color_Point_Highlighted = new Color(180,180,180);
    }

    @Override
    public Dimension getDesiredSize() {
        return m_dimension;
    }

    @Override
    public Container showGUI(Point p) {
        Container erg = m_parent.showGUIAtSize(p);
        erg.setSize(m_dimension);
        return erg;
    }
}
