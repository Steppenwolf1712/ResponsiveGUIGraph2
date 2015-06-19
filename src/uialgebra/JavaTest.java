package uialgebra;

import uialgebra.Exceptions.MalFormedUIA_Exception;
import uialgebra.algebra.UIATokenHE;
import uialgebra.algebra.UIAlgebra;
import uialgebra.editor.UIAEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc Janßen on 13.05.2015.
 */
public class JavaTest extends JFrame {

    private JButton mainButton;

    public JavaTest() {
        super();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        JPanel mainPanel = new JPanel();
        mainButton = new JButton("Start");
        mainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UIAEditor();
            }
        });
        mainPanel.add(mainButton);

        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Component c = (Component)e.getSource();

                double mult = c.getWidth()*c.getHeight();
                if (mult < 300*300) {
                    mainButton.setText("Das ist aber klein!");
                } else if (mult >650*650) {
                    mainButton.setText("Das ist aber Groß!");
                } else {
                    mainButton.setText("Das ist in Ordnung!");
                }
            }
        });

        this.getContentPane().add(mainPanel);


        this.setVisible(true);
    }

    public static void main(String[] args) {
        //JavaTest tester = new JavaTest();

        //tester.regexTest();

        //tester.HashMapTest();

        //tester.AlgebraTest();

        new UIAEditor();

        //algebraWindow();

        //pinWheelWindow();
    }

    private static void algebraWindow() {
        showalgebra("Z1/(((A|{testIndex}B)/C)|D)/E*E/(Z21|{testIndex}Z22)");
    }

    private static void pinWheelWindow() {
        showalgebra("A/(B|(E/{1}D))*(E|{2}C)/{1}D*A|{2}C");
    }

    private static void showalgebra(String uiaTestString) {
        String erg = "\nDas Ergebnis der Token-Analyse des UIAStrings \n\t"+uiaTestString+"\n\nHalfEdges= ";

        UIAlgebra testAlgebra = new UIAlgebra(uiaTestString);
        for (UIATokenHE he: testAlgebra.getHalfEdges()) {
            erg += he.toString() + "*\n";
        }

        try {
            erg += testAlgebra.getDefinition().getNormalisation();
        } catch (MalFormedUIA_Exception e) {
            e.printStackTrace();
        }

        System.out.println(erg);

        JFrame frame = new JFrame("Test UIAlgebra Interpretation");
        frame.setBounds(100, 100, 500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(testAlgebra.createALMLayout());

        frame.setVisible(true);
    }

    private void AlgebraTest() {
        String uiaTestString = "Z1/(((A|{testIndex}B)/C)|D)/E*E/(Z21|{testIndex}Z22)",
                erg = "\nDas Ergebnis der Token-Analyse des UIAStrings \n\t"+uiaTestString+"\n\nHalfEdges= ";

        UIAlgebra testAlgebra = new UIAlgebra(uiaTestString);
        for (UIATokenHE he: testAlgebra.getHalfEdges()) {
            erg += he.toString() + "*\n";
        }

        try {
            erg += testAlgebra.getDefinition().getNormalisation();
        } catch (MalFormedUIA_Exception e) {
            e.printStackTrace();
        }

        System.out.println(erg);
    }

    public void HashMapTest() {
        Map<HashTest, String> map = new HashMap<HashTest, String>();

        HashTest hash1 = new HashTest("Bob"), hash2 = new HashTest("Thorsten"), hash3 = new HashTest("Bob");

        map.put(hash1, "Test1");
        map.put(hash2, "Test2");
        map.put(hash3, "Test3");

        System.out.println("Grösse der Map: "+map.size()+"\ntoString: "+map.toString()+
                "\nWert von hash3: "+map.get(hash3)+"\nWert von hash1: "+map.get(hash1)+
                "\nContains hash1/hash3? : "+map.containsKey(hash1)+"/"+map.containsKey(hash3)+
                "\nhash1==hash3 : "+(hash1==hash3)+" und equals :"+(hash1.equals(hash3))+
                "Hashcodes: hash1: "+hash1.hashCode()+" und hash3: "+hash3.hashCode());
    }

    public void regexTest() {

        String test = "Hallo/was sind das denn /für String Teile/{blub}habe ich mich gefragt!";
        String[] test1 = test.split("\\/\\{\\S+\\}|\\\\\\{\\S+\\}");

        for (String s: test1)
            System.out.println(s);
        System.out.println();

        ArrayList<String> temp = new ArrayList<String>();
        for (String s: test1) {
            String[] temp2 = s.split("\\/|\\\\");
            for (String s2: temp2)
                temp.add(s2);
        }

        for (String s: temp)
            System.out.println(s);
        System.out.println();

        new JavaTest();
    }

    public class HashTest implements Comparable<HashTest> {
        private final String name;

        public HashTest(String name) {
            this.name = name;
        }

        public boolean equals(Object o) {
            if (o instanceof HashTest)
                return this.name.equals(((HashTest)o).name);
            return false;
        }

        @Override
        public int compareTo(HashTest o) {
            return this.name.compareTo(o.name);
        }

        public int hashCode() {
            return this.name.hashCode();
        }
    }
}
