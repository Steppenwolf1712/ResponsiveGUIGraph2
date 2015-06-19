package uialgebra.lexer.standard;

import uialgebra.algebra.Enum_UIATokenHEType;
import uialgebra.algebra.string.IUIAString;

/**
 * Created by Marc Janßen on 19.05.2015.
 */
public class LexFinishedLeftToken extends LexFinishedToken{


    private LexFinishedLeftToken(IUIAString newContent) {
        init(getLexer(), newContent);
    }

    static void createInstance(UIALexer lexer, IUIAString newContent) {
        lexer.pushToken(new LexFinishedLeftToken(newContent));
    }

    @Override
    void parseContent() {
        String sContent = content.getString();

        System.out.println("LexFinishedLeftToken: sContent = "+sContent);
        int startOfTiling = getNextTilingPosition(sContent);
        if (sContent.charAt(startOfTiling) == LEXTOKEN_OP_HORI_TILING)
            this.m_type = Enum_UIATokenHEType.AreaOnTopOf;
        else
            this.m_type = Enum_UIATokenHEType.AreaOnTheLeftSideOf;

        this.m_EdgeID = sContent.substring(startOfTiling+2, sContent.length()-1);

        this.m_ElementID = sContent.substring(0, startOfTiling);
    }

    @Override
    public String getID() {
        return this.content.getString();
    }
}
