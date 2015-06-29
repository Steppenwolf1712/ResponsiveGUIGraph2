package uialgebra;


import nz.ac.auckland.alm.Area;
import nz.ac.auckland.alm.swing.ALMLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Marc Janßen on 08.06.2015.
 */
public class UIAPlaceHolder extends JButton implements MouseListener {

    private final String m_id;
    private JFrame m_parent = null;
    private ALMLayout m_layout = null;

    //private static final Dimension STARTING_DIMENSION = new Dimension(100, 100);

    public UIAPlaceHolder(JFrame parent, ALMLayout layout, String id) {
        super(id);
        this.m_id = id;
        this.m_parent = parent;
        this.m_layout = layout;

        init();
    }


    private void init() {
        //this.setSize(STARTING_DIMENSION);
        this.addMouseListener(this);
    }

    void setPlaceHolderContent(JComponent content) {
        Area temp = m_layout.areaOf(this);
        m_parent.remove(this);

        //m_layout.removeLayoutComponent(this);
        //m_layout.addLayoutComponent(content, new ALMLayout.LayoutParams(
        m_parent.add(content, new ALMLayout.LayoutParams(
                temp.getLeft(),
                temp.getTop(),
                temp.getRight(),
                temp.getBottom()
        ));
    }

    public void startEditMode() {
        if (m_parent==null)
            return;
        Container container = m_parent.getContentPane();
        ALMLayout layout = (ALMLayout)container.getLayout();

        // TODO: ALE nicht mehr startbar von der derzeit benutzten version des ALM
        //layout.edit(container);
    }

    public void changeComponent(MouseEvent e) {
        ComponentChooser chooser = new ComponentChooser(m_parent, m_id);

        chooser.setLocation(e.getLocationOnScreen());
        chooser.setVisible(true);

        JComponent temp = chooser.getChoosenComponent();

        if (temp != null) {
            setPlaceHolderContent(temp);
            m_parent.validate();
        } else {
            System.out.println("UIAPlaceHolder_ActionListener: The choosen element couldn't replace the current content " +
                    "of the area. The cause is either a bad definition at the ComponentChooser or a missing Area-pointer at this Listener.");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            //startEditMode();// TODO: Some mechanism to add constrains
        } else if (e.getButton() == MouseEvent.BUTTON3)
            changeComponent(e);
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
