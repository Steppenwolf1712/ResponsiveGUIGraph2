package uialgebra.algebra;

import uialgebra.lexer.Token;

/**
 * Created by Marc Janßen on 14.05.2015.
 */
public class UIATokenHE extends UIAToken implements IUIATokenHE {

    private final String m_edge;
    private final String m_element;
    private final Enum_UIATokenHEType m_type;
    private final String id;

    private final UIATokenContainer m_parent;

    public UIATokenHE(UIATokenContainer uiaTokenContainer, IUIATokenHE toCopy) {
        this.m_edge = toCopy.getEdgeID();
        this.m_element = toCopy.getElementID();
        this.m_type = toCopy.getType();

        //System.out.println("UIATokenHE: \n\tEdge: "+m_edge+"\n" +
        //        "\tElement: "+m_element+"\n" +
        //        "\tType:"+m_type.name());
        this.id = recreateString();

        m_parent = uiaTokenContainer;
    }

    @Override
    public String getElementID() {
        return this.m_element;
    }

    @Override
    public String getEdgeID() {
        return this.m_edge;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public Enum_UIATokenHEType getType() {
        return this.m_type;
    }


    private String recreateString() {
        String erg = this.m_element;
        switch (m_type) {
            case AreaOnTheLeftSideOf:
                erg+= Token.LEXTOKEN_OP_VERT_TILING+""+Token.LEXTOKEN_TILING_INDEX_OPEN+this.m_edge+Token.LEXTOKEN_TILING_INDEX_CLOSE;
                break;
            case AreaOnTopOf:
                erg+= Token.LEXTOKEN_OP_HORI_TILING+""+Token.LEXTOKEN_TILING_INDEX_OPEN+this.m_edge+Token.LEXTOKEN_TILING_INDEX_CLOSE;
                break;
            case AreaOnTheRightSideOf:
                erg = Token.LEXTOKEN_OP_VERT_TILING+""+Token.LEXTOKEN_TILING_INDEX_OPEN+this.m_edge+Token.LEXTOKEN_TILING_INDEX_CLOSE+erg;
                break;
            case AreaUnder:
                erg = Token.LEXTOKEN_OP_HORI_TILING+""+Token.LEXTOKEN_TILING_INDEX_OPEN+this.m_edge+Token.LEXTOKEN_TILING_INDEX_CLOSE+erg;
                break;
        }
        return erg;
    }

    public String toString() {
        return id;
    }

    public boolean equals(IUIATokenHE token) {
        return this.getID().equals(token.getID());
    }
}
