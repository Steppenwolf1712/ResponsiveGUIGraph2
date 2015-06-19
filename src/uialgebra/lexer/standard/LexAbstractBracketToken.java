package uialgebra.lexer.standard;

import uialgebra.algebra.string.IUIAString;

/**
 * Created by Marc Janßen on 21.05.2015.
 */
abstract class LexAbstractBracketToken extends LexUnfinishedToken {

    protected LexAbstractBracketToken(UIALexer uiaLexer, IUIAString content) {
        super(uiaLexer, content);
    }

    abstract void createTiledToken(String tokens, String tilingToAdd);
    protected abstract void createNewBracketInstance(UIALexer lexer, IUIAString tiledString);
    abstract IUIAString createTiledString(String tokens, String tilingToAdd);

    void rec_derive_Tokens(String tokens, String tilingToAdd) {
        if (tokens.charAt(0) == LEXTOKEN_BR_OPEN) {
            int closingBR = getClosingBRNormal(tokens, 0);

            createNewBracketInstance(getLexer(), createTiledString(tokens.substring(0,closingBR+1), tilingToAdd));

            if (closingBR == tokens.length()-1) {
                //End of tokens, so thats the only Token left so far
                return;
            }
            //There are more than one area/element, so subtract this element and go on with the rest recursive
            int endOfTiling = getClosingBRTiling(tokens, (closingBR+2))+1;
            rec_derive_Tokens(tokens.substring(endOfTiling), tilingToAdd);

        } else {
            //the first Element has to be an area/element but is it the last?
            if (hasTiling(tokens)) {
                int endOfElement = getNextTilingPosition(tokens);
                createTiledToken(tokens.substring(0, endOfElement), tilingToAdd);

                int startOfNextElement = getClosingBRTiling(tokens, endOfElement+1)+1;
                rec_derive_Tokens(tokens.substring(startOfNextElement), tilingToAdd);
            } else {
                //Just 1 area/element left, create a FinishedToken
                createTiledToken(tokens, tilingToAdd);
            }

        }
    }

}
