package uialgebra.responsiveGUIGraph.graph.delauny;

import uialgebra.responsiveGUIGraph.graph.ResponsiveGUIGraph;
import uialgebra.responsiveGUIGraph.graph.Vector2D;

import java.awt.*;

/**
 * Created by Marc Janßen on 25.06.2015.
 */
public class DelaunyTriangle_Factory {

    public static DelaunyTriangle createDelaunyTriangle(Point a, Point b, Point c) {


        Vector2D middle = new Vector2D(a).add(b).add(c);
        middle = middle.mult(1.0/3);

        double minDistance = new Vector2D(ResponsiveGUIGraph.DIM_SIZE).getLength()*2*ResponsiveGUIGraph.MAX_SCALE;

        Vector2D[] new_vertices = new Vector2D[3];
        Vector2D[] orig_vertices = {new Vector2D(a),new Vector2D(b),new Vector2D(c)};
        Vector2D direc;
        for (int i = 0; i<3; i++) {
            direc = orig_vertices[i].sub(middle).getNormalisation();
            new_vertices[i] = middle.add(direc.mult(minDistance));
        }

        DelaunyTriangle erg = new DelaunyTriangle(new_vertices[0], new_vertices[1], new_vertices[2]);

        erg.addPoint(orig_vertices[0]);
        erg.addPoint(orig_vertices[1]);
        erg.addPoint(orig_vertices[2]);

        return erg;
    }
}
