package uialgebra.editor;

import uialgebra.Exceptions.MalFormedUIA_Exception;
import uialgebra.algebra.UIATokenHE;
import uialgebra.algebra.UIAlgebra;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Marc Janßen on 03.06.2015.
 */
public class UIAEditor extends JFrame implements ActionListener{

    private final String S_BTN_NAME_CREATE = "BTN_CREATE";
    private final String S_BTN_NAME_LOAD = "BTN_LOAD";
    private JTextArea txta_create;

    public UIAEditor() {
        super("User Interface Algebra - Editor", null);
        init();
    }

    private void init() {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLayout(null);

        this.setBounds(150, 200, 320, 340);

        JLabel info_text = new JLabel("<html><h3 align=\"center\">Welcome to the<br>UserInterfaceAlgebra<br>Editor</h3><br>Here you can start a new definition of" +
                " a graphical user interface.</html>");
        info_text.setBounds(10, 10, 286, 120);
        this.add(info_text);

        txta_create = new JTextArea("Z1/(((A|{testIndex}B)/C)|D)/E*E/(Z21|{testIndex}Z22)");
        txta_create.setName(S_BTN_NAME_CREATE);
        txta_create.setBounds(20, 160, 280, 80);

        this.add(txta_create);

        JButton btn_load = new JButton("create GUI");
        btn_load.setName(S_BTN_NAME_LOAD);
        btn_load.setBounds(60, 250, 200, 30);
        btn_load.addActionListener(this);

        this.add(btn_load);

        this.setResizable(false);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton caller = null;
        if (e.getSource() instanceof JButton)
            caller = (JButton) e.getSource();
        else
            return;

        if (caller.getName().equals(S_BTN_NAME_LOAD)) {
            String uiaTestString = txta_create.getText().replaceAll("\\s","");

            String erg = "\nDas Ergebnis der Token-Analyse des UIAStrings \n\t"+uiaTestString+"\n\nHalfEdges= ";

            UIAlgebra testAlgebra = new UIAlgebra(uiaTestString);
            for (UIATokenHE he: testAlgebra.getHalfEdges()) {
                erg += he.toString() + "*\n";
            }

            try {
                erg += testAlgebra.getDefinition().getNormalisation();
            } catch (MalFormedUIA_Exception ex) {
                System.out.println("There was a problem with the definition of the User-Interface-Algebra String!");
                ex.printStackTrace();
            }

            System.out.println(erg);

            JFrame frame = new JFrame("Test UIAlgebra Interpretation");
            frame.setBounds(100, 100, 500, 500);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            //frame.getContentPane().setLayout(testAlgebra.createALMLayout());
            testAlgebra.createALEditor(frame);

            frame.setVisible(true);


        }
    }
}
