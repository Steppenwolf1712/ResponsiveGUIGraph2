package uialgebra.algebra;

import java.util.HashSet;

/**
 * Created by Marc Janßen on 14.05.2015.
 */
public class UIATokenElement extends UIAToken {

    private UIATokenEdge top = null;
    private UIATokenEdge bottom = null;
    private UIATokenEdge right = null;
    private UIATokenEdge left = null;

    private final String id;
    private final UIATokenContainer m_parent;

    public UIATokenElement(UIATokenContainer parent, String name) {
        this.id = name;
        this.m_parent = parent;

        init();
    }

    private void init() {
        top = m_parent.getEdgeTop();
        bottom = m_parent.getEdgeBottom();
        left = m_parent.getEdgeLeft();
        right = m_parent.getEdgeRight();
    }

    public boolean equals(Object o) {
        if (o instanceof UIATokenElement) {
            UIATokenElement temp = (UIATokenElement)o;
            return this.getId().equals(temp.getId());
        }
        return false;
    }

    void setLeft(UIATokenEdge left) {
        this.left = left;
    }

    void setTop(UIATokenEdge top) {
        this.top = top;
    }

    void setBottom(UIATokenEdge bottom) {
        this.bottom = bottom;
    }

    void setRight(UIATokenEdge right) {
        this.right = right;
    }

    public UIATokenEdge getTop() {
        return top;
    }

    public UIATokenEdge getBottom() {
        return bottom;
    }

    public UIATokenEdge getRight() {
        return right;
    }

    public UIATokenEdge getLeft() {
        return left;
    }

    public String getId() {
        return id;
    }

    public String toString() {
        String erg = "Element: "+id+"\n";
        erg += "\tLeft: "+this.left.getId()+"\n";
        erg += "\tTop: "+this.top.getId()+"\n";
        erg += "\tRight: "+this.right.getId()+"\n";
        erg += "\tBottom: "+this.bottom.getId()+"\n";
        return erg;
    }

    HashSet<UIATokenElement> getAllLeft() {
        HashSet<UIATokenElement> erg = new HashSet<UIATokenElement>();
        erg.add(this);
        if (!getLeft().isLastEdge())
            for (UIATokenElement elem: getLeft().getLeftsideElements())
                erg.addAll(elem.getAllLeft());
        return erg;
    }

    HashSet<UIATokenElement> getAllRight() {
        HashSet<UIATokenElement> erg = new HashSet<UIATokenElement>();
        erg.add(this);
        if (!getRight().isLastEdge())
            for (UIATokenElement elem: getRight().getRightsideElements())
                erg.addAll(elem.getAllRight());
        return erg;
    }

    HashSet<UIATokenElement> getAllTop() {
        HashSet<UIATokenElement> erg = new HashSet<UIATokenElement>();
        erg.add(this);
        if (!getTop().isLastEdge())
            for (UIATokenElement elem: getLeft().getLeftsideElements())
                erg.addAll(elem.getAllTop());
        return erg;
    }

    HashSet<UIATokenElement> getAllBottom() {
        HashSet<UIATokenElement> erg = new HashSet<UIATokenElement>();
        erg.add(this);
        if (!getBottom().isLastEdge())
            for (UIATokenElement elem: getBottom().getRightsideElements())
                erg.addAll(elem.getAllBottom());
        return erg;
    }

    /**
     * Returns all elements, whose starting Edge is visible from this area  over traverse away from this element.
     * This set consist of the Element itself and shall consist all Elements of an GUI except the case, that
     * an overlap can be found.
     *
     * @return
     */
    public UIATokenElement[] getAreasVisibleFrom() {
        HashSet<UIATokenElement> temp = new HashSet<UIATokenElement>();

        temp.add(this);

        temp.addAll(getAllBottom());
        temp.addAll(getAllLeft());
        temp.addAll(getAllRight());
        temp.addAll(getAllTop());

        UIATokenElement[] erg = new UIATokenElement[temp.size()];
        return temp.toArray(erg);
    }
}
