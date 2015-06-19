package uialgebra.algebra;

import nz.ac.auckland.alm.*;
import nz.ac.auckland.alm.swing.ALMLayout;
import nz.ac.auckland.linsolve.Variable;
import uialgebra.UIAPlaceHolder;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc Janßen on 09.06.2015.
 */
public class ALMExtractor {

    private ALMLayout m_layout = null;
    private JFrame m_parentFrame = null;
    private IUIATokenContainer m_container = null;

    private Map<String, Variable> map_Tabs;
    private Map<String, Area> map_Area;

    public ALMExtractor(JFrame frame, ALMLayout layout, IUIATokenContainer container) {
        this.m_parentFrame = frame;
        this.m_layout = layout;
        this.m_container = container;

        init();
    }

    public ALMExtractor(ALMLayout layout, IUIATokenContainer container) {
        this(null, layout, container);
    }

    private void init() {
        map_Tabs = new HashMap<String, Variable>();
        map_Area = new HashMap<String, Area>();

        map_Tabs.put(m_container.getEdge_Bottom().getId(), m_layout.getBottom());
        map_Tabs.put(m_container.getEdge_Left().getId(), m_layout.getLeft());
        map_Tabs.put(m_container.getEdge_Right().getId(), m_layout.getRight());
        map_Tabs.put(m_container.getEdge_Top().getId(), m_layout.getTop());
    }

    public ALMLayout extractALM() {
        translateEdges();

        createAreas();

        return this.m_layout;
    }

    Area getArea(String id) {
        return this.map_Area.get(id);
    }

    private void createAreas() {
        for (UIATokenElement area: m_container.getElements()) {
            UIAPlaceHolder placeHolder = new UIAPlaceHolder(m_parentFrame, m_layout, area.getId());
            m_parentFrame.add(placeHolder, new ALMLayout.LayoutParams(
                    (XTab) map_Tabs.get(area.getLeft().getId()),
                    (YTab) map_Tabs.get(area.getTop().getId()),
                    (XTab) map_Tabs.get(area.getRight().getId()),
                    (YTab) map_Tabs.get(area.getBottom().getId())
            ));

            Area result_area = m_layout.areaOf(placeHolder);

            result_area.setAlignment(HorizontalAlignment.FILL, VerticalAlignment.FILL);

            map_Area.put(area.getId(),result_area);
            System.out.println(area.toString());
        }
    }

    private void translateEdges() {
        for (UIATokenEdge edge: m_container.getEdges()) {
            if (edge.isVertical())
                map_Tabs.put(edge.getId(), new XTab(edge.getId()));
            else
                map_Tabs.put(edge.getId(), new YTab(edge.getId()));
        }
    }
}
