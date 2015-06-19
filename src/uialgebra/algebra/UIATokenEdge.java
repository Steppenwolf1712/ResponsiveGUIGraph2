package uialgebra.algebra;

import java.util.ArrayList;

/**
 * Created by Marc Janßen on 14.05.2015.
 */
public class UIATokenEdge extends UIAToken {

    public static final String EDGE_ID_TOP = "TOP_EDGE";
    public static final String EDGE_ID_BOTTOM = "BOTTOM_EDGE";
    public static final String EDGE_ID_LEFT = "LEFT_EDGE";
    public static final String EDGE_ID_RIGHT = "RIGHT_EDGE";

    private ArrayList<UIATokenElement> m_RightUnderElements;
    private ArrayList<UIATokenElement> m_LeftTopElements;

    private final String id;
    private final UIATokenContainer m_parent;
    private final boolean m_vertical;
    private final boolean m_isLastEdge;

    public UIATokenEdge(UIATokenContainer parent, String name, boolean vertical) {
        id = name;
        m_parent = parent;
        m_vertical = vertical;

        m_isLastEdge = (this.id.equals(EDGE_ID_BOTTOM)||this.id.equals(EDGE_ID_TOP)||this.id.equals(EDGE_ID_LEFT)||this.id.equals(EDGE_ID_RIGHT));

        init();
    }

    boolean isValid() {
        return (isLastEdge()||(m_LeftTopElements.size()>0&&m_RightUnderElements.size()>0));
    }

    boolean isLastEdge() {
        return this.m_isLastEdge;
    }

    UIATokenElement[] getLeftsideElements() {
        UIATokenElement[] temp = new UIATokenElement[m_LeftTopElements.size()];
        return m_LeftTopElements.toArray(temp);
    }

    UIATokenElement[] getRightsideElements() {
        UIATokenElement[] temp = new UIATokenElement[m_RightUnderElements.size()];
        return m_RightUnderElements.toArray(temp);
    }

    private void init() {
        m_RightUnderElements = new ArrayList<UIATokenElement>();
        m_LeftTopElements = new ArrayList<UIATokenElement>();
    }

    public String getId() {
        return id;
    }

    public boolean isVertical() {
        return m_vertical;
    }

    void addLeft(UIATokenElement elem) {
        this.m_LeftTopElements.add(elem);
    }

    void addRight(UIATokenElement elem) {
        this.m_RightUnderElements.add(elem);
    }
}
