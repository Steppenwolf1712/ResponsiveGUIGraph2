package uialgebra.responsiveGUIGraph.graph;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Marc Janßen on 24.06.2015.
 */
public class KontextMenu extends JPopupMenu implements MouseListener {

    private JMenuItem btn_remove = new JMenuItem("Remove GUI");
    private final ResponsiveGUIGraph_Point m_point;
    private final ResponsiveGUIGraph m_graph;


    public KontextMenu(ResponsiveGUIGraph graph, ResponsiveGUIGraph_Point point) {
        super();
        m_graph = graph;
        m_point = point;

        init();
    }

    private  void init() {
        btn_remove.addMouseListener(this);
        add(btn_remove);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(btn_remove)) {
            m_graph.remove(m_point);
        }
        this.setVisible(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
