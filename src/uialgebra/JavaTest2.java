package uialgebra;

/**
 * Created by Marc Janßen on 26.05.2015.
 */
public class JavaTest2 {

    public static void show() {
        System.out.println("Das ist main Klasse");
    }

    public static void main(String[] args) {
        JavaTest2.show();

        JavaTest4 test = new JavaTest4();
        test.blub();
    }
}
