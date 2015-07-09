package uialgebra.responsiveGUIGraph;

import uialgebra.Exceptions.MalFormedUIA_Exception;
import uialgebra.algebra.UIATokenHE;
import uialgebra.algebra.UIAlgebra;
import uialgebra.responsiveGUIGraph.graph.ResponsiveGUIGraph;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Marc Janßen on 19.06.2015.
 */
public class ResponsiveGUIEditor extends JFrame implements ActionListener, MouseMotionListener, MouseListener, ChangeListener {

    private static final String S_BTN_TESTALGEBRA = "Test Algebra";
    private static final String S_BTN_ADDGUI = "Create and Add";

    private ResponsiveGUIGraph m_resp_Gui_Graph;

    private JTextArea txt_UIAlgebra;

    private JButton btn_testAlgebra;
    private JLabel l_gui_position;
    private JButton btn_addGUI;
    private JSlider sli_graphScale;
    private JButton btn_createResponsiveGUI;

    public ResponsiveGUIEditor() {
        super("ResponsiveGUI - Editor");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        init();

        pack();
        setResizable(false);
        setVisible(true);
    }

    private void init() {
        this.setLayout(null);//new GridLayout(2,1));

        m_resp_Gui_Graph = ResponsiveGUIGraph.getInstance();
        m_resp_Gui_Graph.addMouseMotionListener(this);
        m_resp_Gui_Graph.addMouseListener(this);
        m_resp_Gui_Graph.setLocation(0,0);
        add(m_resp_Gui_Graph);

        JPanel controlBorder = new JPanel(null);
        controlBorder.setBorder(BorderFactory.createLineBorder(Color.black));
        controlBorder.setBounds(5, m_resp_Gui_Graph.getHeight() + 5, m_resp_Gui_Graph.getWidth() - 5, 80);
        add(controlBorder);

        JPanel controlBoard = new JPanel();
        controlBoard.setBorder(new EmptyBorder(4,4,4,4));
        controlBoard.setBounds(3, 3, m_resp_Gui_Graph.getWidth()-10, 75);
        controlBoard.setLayout(new GridLayout(1,2,5,5));
        controlBorder.add(controlBoard);

        txt_UIAlgebra = new JTextArea("Z1/(((A|{testIndex}B)/C)|D)/E*E/(Z21|{testIndex}Z22)");
        txt_UIAlgebra.setBorder(BorderFactory.createLineBorder(Color.black));
        txt_UIAlgebra.setSize(650, 75);
        controlBoard.add(txt_UIAlgebra, 0);

        JPanel horiLay = new JPanel();
        horiLay.setLayout(new GridLayout(3,1,0,0));
        controlBoard.add(horiLay);


        JPanel graph_settings = new JPanel(new GridLayout(1,2));
        horiLay.add(graph_settings);
        l_gui_position = new JLabel("Point: 0/0");
        graph_settings.add(l_gui_position);
        sli_graphScale = new JSlider(JSlider.HORIZONTAL,
                1, 5, 5);
        sli_graphScale.addChangeListener(this);
        //sli_graphScale.setPaintTicks(true);//To less space for this
        graph_settings.add(sli_graphScale);

        JPanel uia_panel = new JPanel();
        uia_panel.setLayout(new GridLayout(1,2,0,0));
        horiLay.add(uia_panel);
        btn_testAlgebra = new JButton(S_BTN_TESTALGEBRA);
        btn_testAlgebra.addActionListener(this);
        uia_panel.add(btn_testAlgebra);

        btn_addGUI = new JButton(S_BTN_ADDGUI);
        btn_addGUI.addActionListener(this);
        uia_panel.add(btn_addGUI);

        btn_createResponsiveGUI = new JButton("create Responsive GUI");
        btn_createResponsiveGUI.addActionListener(this);
        horiLay.add(btn_createResponsiveGUI);

        this.getContentPane().setPreferredSize(new Dimension(m_resp_Gui_Graph.getWidth()-5, m_resp_Gui_Graph.getHeight() + 80));
    }

    /**
     * This var is only for storing the builded Window. Because it has perhaps some Constraints in it, which were lost,
     * if the window will be recreated out of the String. The stored Frame can be sent to the GUI_Point.
     */
    private JFrame m_lastCreated = null;
    private String m_uiaTestString = null;
    private UIAlgebra m_temp_algebra = null;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btn_addGUI)) {
            String temp = txt_UIAlgebra.getText().replaceAll("\\s","");

            if (temp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please define a User Interface first with the User Interface Algebra!");
                return;
            }

            if (m_lastCreated != null && !temp.equals(m_uiaTestString)) {
                this.m_resp_Gui_Graph.addUIAlgebra(m_temp_algebra, m_lastCreated);
                m_lastCreated = null;
            } else {
                UIAlgebra testAlgebra = new UIAlgebra(temp);
                this.m_resp_Gui_Graph.addUIAlgebra(testAlgebra);
            }

        } else if (e.getSource().equals(btn_testAlgebra)) {
            m_uiaTestString = txt_UIAlgebra.getText().replaceAll("\\s","");

            if (m_uiaTestString.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an String of User Interface Algebra first!");
                return;
            }

            String erg = "\nDas Ergebnis der Token-Analyse des UIAStrings \n\t"+m_uiaTestString+"\n\nHalfEdges= ";

            UIAlgebra testAlgebra = new UIAlgebra(m_uiaTestString);
            for (UIATokenHE he: testAlgebra.getHalfEdges()) {
                erg += he.toString() + "*\n";
            }

            try {
                erg += testAlgebra.getDefinition().getNormalisation();
            } catch (MalFormedUIA_Exception ex) {
                JOptionPane.showMessageDialog(this, "There was a problem with the definition of the User-Interface-Algebra String!\n"+ex.getMessage());
                ex.printStackTrace();
            }

            System.out.println(erg);

            JFrame frame = new JFrame(ResponsiveGUIGraph.S_UIALGEBRA_FRAME_NAME);
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

            //frame.getContentPane().setLayout(testAlgebra.createALMLayout());
            testAlgebra.createALEditor(frame);
            frame.pack();//frame.setBounds(100, 100, 500, 500);

            frame.setVisible(true);
            m_lastCreated = frame;
            m_temp_algebra = testAlgebra;
        } else if (e.getSource().equals(btn_createResponsiveGUI)) {
            if (!m_resp_Gui_Graph.createResponsiveGUI())
                JOptionPane.showMessageDialog(this,"There were no GUIs added to the ResponsiveGUI-Graph yet!");
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point posi = m_resp_Gui_Graph.getPositionInGraph(e.getX(), e.getY());

        if (posi.getX()==0 || posi.getY()==0) {
            printPosition(new Point(0, 0));
            m_resp_Gui_Graph.deselectGUI();
        } else {
            printPosition(posi);
            m_resp_Gui_Graph.selectGUI(posi);
        }
    }

    private void printPosition(Point dim) {
        String erg = "Point: ";
        erg += (dim.getX()+"/"+dim.getY());
        l_gui_position.setText(erg);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(sli_graphScale)) {
            m_resp_Gui_Graph.setScale(sli_graphScale.getValue());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = m_resp_Gui_Graph.getPositionInGraph(e.getX(), e.getY());
        if (p.getX() <= 0 || p.getY()<=0)
            return;
        if (e.getButton() == MouseEvent.BUTTON1) {
            m_resp_Gui_Graph.showSelectedGUI(e.getLocationOnScreen());
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            m_resp_Gui_Graph.showKontextMenu(e);
        }

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
