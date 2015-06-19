package uialgebra.lexer.standard;

import uialgebra.algebra.Enum_UIATokenHEType;
import uialgebra.algebra.IUIATokenHE;

/**
 * Created by Marc Janßen on 15.05.2015.
 */
public abstract class LexFinishedToken extends LexToken implements IUIATokenHE {

    protected String m_ElementID;
    protected String m_EdgeID;

    protected Enum_UIATokenHEType m_type;

    @Override
    public String getElementID() {
        return this.m_ElementID;
    }

    @Override
    public String getEdgeID() {
        return this.m_EdgeID;
    }

    public String getID() {
        return this.toString();
    }

    public String toString() {
        return this.content.getString();
    }

    @Override
    public Enum_UIATokenHEType getType() {
        return this.m_type;
    }

    public int hashCode() {
        return this.getID().hashCode();
    }
}
