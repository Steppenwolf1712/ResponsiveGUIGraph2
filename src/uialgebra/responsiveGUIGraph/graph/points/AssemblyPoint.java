package uialgebra.responsiveGUIGraph.graph.points;

import uialgebra.responsiveGUIGraph.graph.Vector2D;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Marc Janßen on 09.07.2015.
 */
public class AssemblyPoint extends Abstract_Graph_Point {

    private ArrayList<ResponsiveGUIGraph_Point> m_points = null;
    private Dimension m_startingDimension = null;

    public AssemblyPoint(ResponsiveGUIGraph_Point... points) {
        m_points = new ArrayList<ResponsiveGUIGraph_Point>();
        m_startingDimension = points[0].getDesiredSize();
        for (ResponsiveGUIGraph_Point point: points)
            m_points.add(point);

        this.color_Point = new Color(190, 0,0);
        this.color_Point_Highlighted = new Color(240, 0,0);
    }

    public void addPoint(ResponsiveGUIGraph_Point point) {
        m_points.add(point);
    }

    @Override
    public Dimension getDesiredSize() {
        return m_startingDimension;
//        Vector2D erg = new Vector2D();
//
//        for (ResponsiveGUIGraph_Point point: m_points) {
//            erg.add(new Vector2D(point.getDesiredSize()));
//        }
//        erg.mult(1.0/m_points.size());
//        return new Dimension((int)erg.getX(), (int)erg.getY());
    }

    @Override
    public Container showGUI(Point p) {

        return null;
    }

}
