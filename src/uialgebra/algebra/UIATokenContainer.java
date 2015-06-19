package uialgebra.algebra;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc Janßen on 14.05.2015.
 */
public class UIATokenContainer implements IUIATokenContainer {

    private Map<String, UIATokenElement> m_ElementMap;
    private Map<String, UIATokenHE> m_HalfEdgeMap;
    private Map<String, UIATokenEdge> m_EdgeMap;

    private UIATokenEdge m_Edge_Top;
    private UIATokenEdge m_Edge_Bottom;
    private UIATokenEdge m_Edge_Left;
    private UIATokenEdge m_Edge_Right;

    public UIATokenContainer() {
        init();
    }

    public UIATokenEdge getEdge_Top() {
        return m_Edge_Top;
    }

    public UIATokenEdge getEdge_Bottom() {
        return m_Edge_Bottom;
    }

    public UIATokenEdge getEdge_Left() {
        return m_Edge_Left;
    }

    public UIATokenEdge getEdge_Right() {
        return m_Edge_Right;
    }

    @Override
    public boolean checkSoundness() {
        boolean erg = false;

        System.out.println("\nSoundness-Check: ");
        UIATokenElement[] array = getElements();
        int sizeToCompare = array.length, temp=0;
        for (UIATokenElement elem: array) {
            temp = elem.getAreasVisibleFrom().length;

            System.out.println("\tUIATokenContainer: Count of Visible-Token from Element("+elem.getId()+") = "+temp+"\n\t\tshould be "+sizeToCompare);

            if (temp<sizeToCompare)
                return false;
            else
                erg = true;
        }

        return erg;
    }

    private void init() {
        m_ElementMap = new HashMap<String, UIATokenElement>();
        m_EdgeMap = new HashMap<String, UIATokenEdge>();
        m_HalfEdgeMap = new HashMap<String, UIATokenHE>();

        m_Edge_Top = new UIATokenEdge(this, UIATokenEdge.EDGE_ID_TOP, false);
        m_Edge_Bottom = new UIATokenEdge(this, UIATokenEdge.EDGE_ID_BOTTOM, false);
        m_Edge_Left = new UIATokenEdge(this, UIATokenEdge.EDGE_ID_LEFT, true);
        m_Edge_Right = new UIATokenEdge(this, UIATokenEdge.EDGE_ID_RIGHT, true);
    }

    UIATokenEdge getEdgeTop() {
        return m_Edge_Top;
    }

    UIATokenEdge getEdgeBottom() {
        return m_Edge_Bottom;
    }

    UIATokenEdge getEdgeLeft() {
        return m_Edge_Left;
    }

    UIATokenEdge getEdgeRight() {
        return m_Edge_Right;
    }

    @Override
    public UIATokenElement[] getElements() {
        int index = 0;
        UIATokenElement[] erg = new UIATokenElement[m_ElementMap.size()];
        for (UIATokenElement elem: m_ElementMap.values())
            erg[index++] = elem;

        return erg;
    }

    @Override
    public UIATokenEdge[] getEdges() {
        int index = 0;
        UIATokenEdge[] erg = new UIATokenEdge[m_EdgeMap.size()];
        for (UIATokenEdge elem: m_EdgeMap.values())
            erg[index++] = elem;

        return erg;
    }

    @Override
    public UIATokenHE[] getHalfEdges() {
        int index = 0;
        UIATokenHE[] erg = new UIATokenHE[m_HalfEdgeMap.size()];
        for (UIATokenHE elem: m_HalfEdgeMap.values())
            erg[index++] = elem;

        return erg;
    }

    @Override
    public void addHalfEdge(IUIATokenHE halfEdge) {
        //Check whether the HE is already known before adding it
        if (!m_HalfEdgeMap.containsKey(halfEdge.toString()))
            addHalfEdge(new UIATokenHE(this, halfEdge));

        //create new Edges and Elements out of Half-Edges:
        String sElement = halfEdge.getElementID();
        UIATokenElement elem;
        if (m_ElementMap.containsKey(sElement))
            elem = m_ElementMap.get(sElement);
        else {
            elem = new UIATokenElement(this, sElement);
            addElement(elem);
        }

        addEdgeToElement(elem, halfEdge);
    }

    private void addEdgeToElement(UIATokenElement elem, IUIATokenHE halfEdge) {
        UIATokenEdge edge = m_EdgeMap.get(halfEdge.getEdgeID());
        switch (halfEdge.getType()) {
            case AreaOnTheLeftSideOf:
                if (edge == null) {
                    edge = new UIATokenEdge(this, halfEdge.getEdgeID(), true);
                    addEdge(edge);
                }
                edge.addLeft(elem);
                elem.setRight(edge);
                break;
            case AreaOnTheRightSideOf:
                if (edge == null) {
                    edge = new UIATokenEdge(this, halfEdge.getEdgeID(), true);
                    addEdge(edge);
                }
                edge.addRight(elem);
                elem.setLeft(edge);
                break;
            case AreaOnTopOf:
                if (edge == null) {
                    edge = new UIATokenEdge(this, halfEdge.getEdgeID(), false);
                    addEdge(edge);
                }
                edge.addLeft(elem);
                elem.setBottom(edge);
                break;
            case AreaUnder:
                if (edge == null) {
                    edge = new UIATokenEdge(this, halfEdge.getEdgeID(), false);
                    addEdge(edge);
                }
                edge.addRight(elem);
                elem.setTop(edge);
                break;
        }
    }

    private void addEdge(UIATokenEdge edge) {
        m_EdgeMap.put(edge.getId(), edge);
    }

    private void addElement(UIATokenElement element) {
        m_ElementMap.put(element.getId(), element);
    }

    private void addHalfEdge(UIATokenHE halfEdge) {
        m_HalfEdgeMap.put(halfEdge.getID(), halfEdge);
    }

}
