package uialgebra.responsiveGUIGraph.graph;

import uialgebra.algebra.UIAlgebra;
import uialgebra.responsiveGUIGraph.ResponsiveGUIFrame;
import uialgebra.responsiveGUIGraph.graph.delauny.DelaunyTriangle;
import uialgebra.responsiveGUIGraph.graph.delauny.DelaunyTriangle_Factory;
import uialgebra.responsiveGUIGraph.graph.delauny.LineDrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * Created by Marc Janßen on 19.06.2015.
 */
public class ResponsiveGUIGraph extends JPanel {

    public static final String S_UIALGEBRA_FRAME_NAME = "UIAlgebra - View";
    private ArrayList<ResponsiveGUIGraph_Point> m_points = null;
    private ResponsiveGUIGraph_Point m_point_selected = null;

    public static final Dimension DIM_SIZE = new Dimension(750, 550);
    public static final Color COLOR_GRAPH = new Color(0, 0, 0, 180);
    public final Color COLOR_GRAPH_BACK = new Color(this.getBackground().getRed()+8, this.getBackground().getGreen()+8, this.getBackground().getBlue()+8, this.getBackground().getAlpha()-20);
    public static final Color COLOR_POINT = new Color(0, 0, 180, 180);
    public static final Color COLOR_LINE = new Color(0, 0, 255, 255);
    public static final Color COLOR_POINT_HIGHLIGHTED = new Color(150, 130, 0, 180);
    public static final Stroke GRAPH_STROKE = new BasicStroke(3f);
    public static final Stroke DELAUNY_STROKE = new BasicStroke(0.8f);
    public static int MAX_SCALE = 5;
    private int m_xScale = 5;
    private int m_yScale = 5;
    static final int I_BORDER_GAP_X = 30;
    static final int I_BORDER_GAP_Y = 20;

    private LineDrawer m_LineDrawer = null;

    private DelaunyTriangle m_Delauny = null;

    private static ResponsiveGUIGraph single = null;

    private ResponsiveGUIGraph() {
        init();
    }

    private void init() {
        m_points = new ArrayList<ResponsiveGUIGraph_Point>();

        this.setBounds(0, 0, DIM_SIZE.width, DIM_SIZE.height);
    }

    public static ResponsiveGUIGraph getInstance() {
        if (single==null)
            single = new ResponsiveGUIGraph();
        return single;
    }

    private ResponsiveGUIGraph_Point[] getPoints() {
        ResponsiveGUIGraph_Point[] erg = new ResponsiveGUIGraph_Point[m_points.size()];
        return m_points.toArray(erg);
    }

    /**
     * This Method adds a specific Point to the ResponsiveGUIGraph, regarding the UIAlgebra-Parameter.
     *
     * @param toAdd
     */
    public void addUIAlgebra(UIAlgebra toAdd) {
        ResponsiveGUIGraph_Point temp = new ResponsiveGUIGraph_Point(this, toAdd);
        this.m_points.add(temp);

        if (m_points.size() == 3 && m_Delauny==null)
            calcDelauny(new Vector2D(m_points.get(0).getDesiredSize()),
                    new Vector2D(m_points.get(1).getDesiredSize()),
                    new Vector2D(m_points.get(2).getDesiredSize()));
        else
        if (m_points.size() > 3 && !(m_Delauny==null))
            addDelauny(new Vector2D(temp.getDesiredSize()));

        this.repaint();
    }

    /**
     * This Method adds a specific Point to the ResponsiveGUIGraph, regarding the UIAlgebra-Parameter.
     * If there was already an ALMLayout created out of the UIAlgebra, it can be stored with the ResponsiveGUIGraph
     * into the Point-Object to save the collected Constraints collected so far.
     *
     * @param toAdd
     * @param alm_Container
     */
    public void addUIAlgebra(UIAlgebra toAdd, JFrame alm_Container) {
        ResponsiveGUIGraph_Point temp = new ResponsiveGUIGraph_Point(this, toAdd, alm_Container);
        this.m_points.add(temp);

        if (m_points.size() == 3 && m_Delauny==null)
            calcDelauny(new Vector2D(m_points.get(0).getDesiredSize()),
                    new Vector2D(m_points.get(1).getDesiredSize()),
                    new Vector2D(m_points.get(2).getDesiredSize()));
        else
            if (m_points.size() > 3 && !(m_Delauny==null))
                addDelauny(new Vector2D(temp.getDesiredSize()));

        this.repaint();
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Vector2D relPos = new Vector2D(I_BORDER_GAP_X, getHeight()-I_BORDER_GAP_Y);
        Vector2D scale = new Vector2D(m_xScale,m_yScale);
        Vector2D max_Size = new Vector2D(getWidth(), getHeight()).sub(new Vector2D(2*I_BORDER_GAP_X, 2*I_BORDER_GAP_Y)).mult(scale);

        if (m_LineDrawer==null)
            m_LineDrawer = new LineDrawer(g2, relPos, scale, max_Size);
        else
            m_LineDrawer.refreshGraphicsObject(g2);

        GeneralPath content_Graph = new GeneralPath();
        content_Graph.moveTo(I_BORDER_GAP_X, I_BORDER_GAP_Y);
        content_Graph.lineTo(I_BORDER_GAP_X, getHeight() - I_BORDER_GAP_Y);
        content_Graph.lineTo(getWidth() - I_BORDER_GAP_X, getHeight() - I_BORDER_GAP_Y);
        content_Graph.lineTo(getWidth() - I_BORDER_GAP_X, I_BORDER_GAP_Y);
        content_Graph.closePath();

        g2.setColor(COLOR_GRAPH_BACK);
        System.out.println(COLOR_GRAPH_BACK);
        g2.draw(content_Graph);
        g2.fill(content_Graph);
        g2.setColor(COLOR_GRAPH);

        m_LineDrawer.drawLine(max_Size.mult(new Vector2D(0.0,1.0)), new Vector2D());
        m_LineDrawer.drawLine(new Vector2D(), max_Size.mult(new Vector2D(1.0,0.0)));

        //Hatchmarks y-axes
        boolean length_switch = true;
        for (int i = 0; i<14; i++) {
            int x0;
            int y0 = getHeight()-I_BORDER_GAP_Y -(50*i);
            if (length_switch) {
                x0 = I_BORDER_GAP_X - 10;
                String temp = ""+(100*i*m_yScale/2);
                g2.drawString(temp, x0+9-(temp.length()*7), y0-2);
            } else
                x0 = I_BORDER_GAP_X-5;
            length_switch = !length_switch;

            int x1 = I_BORDER_GAP_X;
            int y1 = getHeight()-I_BORDER_GAP_Y -(50*i);
            g2.drawLine(x0, y0, x1, y1);
        }
        //Hatchmarks x-axes
        length_switch = true;
        for (int i = 0; i<14; i++) {
            int x0 = I_BORDER_GAP_X+(50*i);

            int y0;
            if (length_switch) {
                y0 = getHeight() - I_BORDER_GAP_Y + 10;
                String temp = ""+(100*i*m_xScale/2);
                g2.drawString(temp, x0+1, y0+3);
            } else
                y0 = getHeight()-I_BORDER_GAP_Y +5;
            length_switch = !length_switch;

            int x1 = I_BORDER_GAP_X+(50*i);
            int y1 = getHeight()-I_BORDER_GAP_Y;
            g2.drawLine(x0, y0, x1, y1);
        }

        Stroke oldStroke = g2.getStroke();
        g2.setColor(COLOR_GRAPH);
        g2.setStroke(GRAPH_STROKE);

        g2.setColor(COLOR_POINT);

        //relocate all points
        for (ResponsiveGUIGraph_Point point: m_points) {
            Vector2D temp_loc = new Vector2D(point.getDesiredSize());
            Vector2D loc = temp_loc.divide(scale).mult(new Vector2D(1.0, -1.0)).add(relPos);//getWidth()/m_xScale+ I_BORDER_GAP_X, this.getHeight()-(temp_loc.getHeight()/m_yScale+I_BORDER_GAP_Y));
            point.drawPoint(loc, g2);
        }

        Color temp = g2.getColor();
        g2.setStroke(DELAUNY_STROKE);
        g2.setColor(COLOR_LINE);

        if (m_points.size()==2) {
            drawLineBetween(new Vector2D(m_points.get(0).getDesiredSize()),
                    new Vector2D(m_points.get(1).getDesiredSize()));
        } else {
            if (m_points.size()>2) {
                if (m_Delauny == null)
                    calcDelauny(new Vector2D(m_points.get(0).getDesiredSize()),
                            new Vector2D(m_points.get(1).getDesiredSize()),
                            new Vector2D(m_points.get(2).getDesiredSize()));
                drawDelauny();
            }
        }
        g2.setColor(temp);
    }

    private void drawLineBetween(Point positionInGraph, Point positionInGraph1) {
        Vector2D point0, point1;
        if (positionInGraph.x<=positionInGraph1.x) {
            point0 = new Vector2D(positionInGraph);
            point1 = new Vector2D(positionInGraph1);
        } else {
            point0 = new Vector2D(positionInGraph1);
            point1 = new Vector2D(positionInGraph);
        }

        Vector2D middle = point0.add(point1);
        middle = middle.mult(0.5);

        double step;
        try {
            step = point1.sub(point0).getSlope();//point1.x - point0.x) / (point1.y - point0.y);
            step = -1 / step;
        } catch (ArithmeticException ex) {
            Vector2D left = new Vector2D(0,middle.getY()), right = new Vector2D(getWidth()-2*I_BORDER_GAP_X, middle.getY());

            m_LineDrawer.drawLine(left, right);
            //g2.drawLine(I_BORDER_GAP_X, getHeight()-I_BORDER_GAP_Y-middle.y, getWidth()-I_BORDER_GAP_X, getHeight()-I_BORDER_GAP_Y-middle.y);
            return;
        }
        Vector2D p1, p2;
        if (step == 0) {
            p1 = new Vector2D(middle.getX(), 0);
            p2 = new Vector2D(middle.getX(), getHeight()-2*I_BORDER_GAP_Y);
        } else {

            double b = middle.getY()-step*middle.getX();
            int maxH = getHeight()-2*I_BORDER_GAP_Y;

            int maxW = getWidth()-2*I_BORDER_GAP_X;
            double b2 = b+step*maxW;

            double x1 = -b/step;
            double x2 = (maxH-b)/step;

            p2 = new Vector2D(maxW, b2);

            if (b<=maxH) {
                if (b>=0) {
                    p1 = new Vector2D(0, b);
                    if (b2<=maxH) {
                        if (b2<0)
                            p2 = new Vector2D(x1, 0);
                    } else
                        p2 = new Vector2D(x2, maxH);
                } else {
                    p1 = new Vector2D(x1, 0);

                    if (b2>maxH)
                        p2 = new Vector2D(x2, maxH);

                }
            } else {
                p1 = new Vector2D(x2, maxH);

                if (b2<0)
                    p2 = new Vector2D(x1, 0);
            }

        }
        m_LineDrawer.drawLine(p1, p2);
        //VectorHelper.drawLineOnGraph(g2, p1, p2);
    }


    public boolean remove(ResponsiveGUIGraph_Point point) {
        boolean erg = m_points.remove(point);

        if (erg) {
            repaint();
            if (m_point_selected.equals(point)) {
                m_point_selected = null;
            }
            if (m_points.size()>=3)
                calcDelauny(new Vector2D(m_points.get(0).getDesiredSize()),
                        new Vector2D(m_points.get(1).getDesiredSize()),
                        new Vector2D(m_points.get(2).getDesiredSize()));
            else
                this.m_Delauny = null;
        }
        return erg;
    }

    public void setScale(int new_scale) {
        if (new_scale>MAX_SCALE || new_scale<0)
            return;
        this.m_xScale = new_scale;
        this.m_yScale = new_scale;
        Vector2D scale = new Vector2D(this.m_xScale, this.m_yScale);
        m_LineDrawer.setScale(scale);
        m_LineDrawer.setMaxValues(new Vector2D(DIM_SIZE).mult(scale));
        repaint();
    }


    public Dimension getPreferredSize() {
        return this.DIM_SIZE;
    }

    public Point getPositionInGraph(int x, int y) {
        Point erg = new Point(calcX(x), calcY(y));
        return erg;
    }

    private int calcX(int x) {
        int erg = (x-I_BORDER_GAP_X)*m_xScale;
        if (erg<0)
            return 0;
        return erg;
    }

    private int calcY(int y) {
        int erg = ((getHeight()-y)-I_BORDER_GAP_Y)*m_yScale;
        if (erg<0)
            return 0;
        return erg;
    }

    public void selectGUI(Point posi) {
        if (m_points.size() == 0)
            return;
        if (m_points.size() == 1) {
            m_point_selected = m_points.get(0);
        } else {
            if (m_point_selected!= null)
                m_point_selected.setSelected(false);
            double current = Double.MAX_VALUE, test;
            for (ResponsiveGUIGraph_Point point : m_points) {
                test = posi.distanceSq(point.getDesiredSize().getWidth(), point.getDesiredSize().getHeight());
                //System.out.println("Distance of "+point.getDesiredSize()+"\n\tto Mouse is : "+test+"\n\twith mouse : "+posi.toString());
                if (test < current) {
                    m_point_selected = point;
                    current = test;
                }
            }
        }
        m_point_selected.setSelected(true);
        repaint();
    }

    public void deselectGUI() {
        if (m_point_selected==null)
            return;
        m_point_selected.setSelected(false);
        m_point_selected = null;
        repaint();
    }

    public void showSelectedGui(Point p) {
        if (m_point_selected==null)
            return;
        m_point_selected.showGUI(p);
    }

    private void calcDelauny(Vector2D point1, Vector2D point2, Vector2D point3) {
        m_Delauny = DelaunyTriangle_Factory.createDelaunyTriangle(point1, point2, point3);
    }

    private void addDelauny(Vector2D point) {
        if (this.m_Delauny != null)
            this.m_Delauny.addPoint(point);

        System.out.println("Die Punkte der Algebra: ");
        for (int i = 0; i<m_points.size(); i++) {
            System.out.println("\tPunkte : "+m_points.get(i).getDesiredSize());
        }
    }

    private void drawDelauny() {
        //this.m_Delauny.drawDelauny(this.m_LineDrawer);//For testing the DelaunyTriangulation
        this.m_Delauny.drawVoronoiDiagram(this.m_LineDrawer);
        //m_LineDrawer.changeColor(new Color(255,0,0,255));
        //this.m_Delauny.drawDelauny(this.m_LineDrawer);//For testing the DelaunyTriangulation
    }

    public void showKontextMenu(MouseEvent event) {
        //Point locationOnScreen = event.getLocationOnScreen();
        System.out.println("Hello");
        KontextMenu menu = new KontextMenu(this, m_point_selected);
        menu.show(event.getComponent(), event.getX(), event.getY());
        //menu.setLocation(locationOnScreen.x, locationOnScreen.y);
        //menu.setVisible(true);
    }

    public boolean createResponsiveGUI() {
        if (m_points !=null && m_points.size() >=1) {
            new ResponsiveGUIFrame(this.getPoints());
            return true;
        } else
            return false;

    }
}