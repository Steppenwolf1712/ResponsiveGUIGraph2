package uialgebra.lexer.standard;

import uialgebra.algebra.string.IUIAString;

/**
 * Created by Marc Janßen on 21.05.2015.
 */
public class LexUnfinishedRightBracketToken extends LexAbstractBracketToken {

    private LexUnfinishedRightBracketToken(UIALexer uiaLexer, IUIAString content) {
        super(uiaLexer, content);
    }

    public static void createInstance(UIALexer lexer, IUIAString newContent) {
        if (!lexer.containsTokenFor(newContent))
           lexer.pushToken(new LexUnfinishedRightBracketToken(lexer, newContent));
    }

    /**
     * This Method just make sure, that the right createInstance-Method at the extended abstract class will be called
     *
     * @param lexer
     * @param tiledString
     */
    @Override
    protected void createNewBracketInstance(UIALexer lexer, IUIAString tiledString) {
        createInstance(lexer, tiledString);
    }


    void parseContent() {
        //It is only one Case allowed here /(A+)
        //The only thing this class shall do is get rid of the outer Brackets from A and derive the Tokens from that
        String sContent = content.getString();
        int startOfBrackets = getClosingBRTiling(sContent, 1)+1;
        String tiling = sContent.substring(0, startOfBrackets);
        String tokens = sContent.substring(startOfBrackets+1, sContent.length()-1);

        //Just parse the content of the Brackets as usual with an unfinished Token
        // (It is not possible, that the content of the Brackets only consist of one Element, because of normalisation)
        LexUnfinishedToken.createInstance(getLexer(), createIUAString(tokens));

        //Filter for those LexUnfinishedRightBracket-instances who have the same tiling on their Bracket-Depth
        int tilingPosition = 0;
        if (tokens.charAt(0) == LEXTOKEN_BR_OPEN)
            tilingPosition = getClosingBRNormal(tokens, 0)+1;
        else
            tilingPosition = getNextTilingPosition(tokens);

        char tilingType = tokens.charAt(tilingPosition);
        if (tilingType == tiling.charAt(0)) {
            //Only the first token has to be added with an additional tiling
            int endOfElement = tilingPosition;
            if (tokens.charAt(0) == LEXTOKEN_BR_OPEN) {
                createNewBracketInstance(getLexer(),createTiledString(tokens.substring(0, endOfElement), tiling));
            } else {
                //System.out.println("LexUnfinishedRightBracket: LexFinishedRight1 : "+createTiledString(tokens, tiling));
                createTiledToken(tokens.substring(0, endOfElement), tiling);
            }
        } else {
            rec_derive_Tokens(tokens, tiling);
        }
    }

    @Override
    void createTiledToken(String tokens, String tilingToAdd) {
        //System.out.println("LexUnfinishedRightBracket: LexFinishedRight2 : "+createTiledString(tokens, tilingToAdd));
        LexFinishedRightToken.createInstance(getLexer(), createTiledString(tokens, tilingToAdd));
    }

    @Override
    IUIAString createTiledString(String tokens, String tilingToAdd) {
        return createIUAString(tilingToAdd+tokens);
    }
}
