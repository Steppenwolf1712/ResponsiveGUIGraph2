package uialgebra.lexer.standard;

import uialgebra.algebra.Enum_UIATokenHEType;
import uialgebra.algebra.string.IUIAString;

/**
 * Created by Marc Janßen on 19.05.2015.
 */
public class LexFinishedRightToken extends LexFinishedToken {

    private LexFinishedRightToken(IUIAString newContent) {
        init(getLexer(), newContent);
    }

    static void createInstance(UIALexer lexer, IUIAString newContent) {
        lexer.pushToken(new LexFinishedRightToken(newContent));
    }


    @Override
    void parseContent() {
        String sContent = content.getString();

        System.out.println("LexFinishedRightToken: sContent = "+sContent);
        if (sContent.charAt(0) == LEXTOKEN_OP_HORI_TILING)
            this.m_type = Enum_UIATokenHEType.AreaUnder;
        else
            this.m_type = Enum_UIATokenHEType.AreaOnTheRightSideOf;

        int endOfTiling = getClosingBRTiling(sContent, 1);

        this.m_EdgeID = sContent.substring(2, endOfTiling);

        this.m_ElementID = sContent.substring(endOfTiling+1);
    }
}
