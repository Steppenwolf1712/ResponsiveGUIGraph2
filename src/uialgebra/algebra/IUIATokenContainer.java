package uialgebra.algebra;

/**
 * Created by Marc Janßen on 15.05.2015.
 */
public interface IUIATokenContainer {
    UIATokenElement[] getElements();

    UIATokenEdge[] getEdges();

    UIATokenHE[] getHalfEdges();

    void addHalfEdge(IUIATokenHE halfEdge);

    public UIATokenEdge getEdge_Top();

    public UIATokenEdge getEdge_Bottom();

    public UIATokenEdge getEdge_Left();

    public UIATokenEdge getEdge_Right();
}
