package uialgebra.lexer.standard;

import uialgebra.algebra.string.IUIAString;

import java.util.Stack;

/**
 * Created by Marc Janßen on 21.05.2015.
 */
class LexUnfinishedLeftBracketToken extends LexAbstractBracketToken {

    private LexUnfinishedLeftBracketToken(UIALexer uiaLexer, IUIAString content) {
        super(uiaLexer, content);
    }

    public static void createInstance(UIALexer lexer, IUIAString newContent) {
        if (!lexer.containsTokenFor(newContent))
            lexer.pushToken(new LexUnfinishedLeftBracketToken(lexer, newContent));
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
        //It is only one Case allowed here (A+)/
        //The only thing this class shall do is get rid of the outer Brackets from A and derive the Tokens from that
        String sContent = content.getString();
        int closingBR = getClosingBRNormal(sContent, 0);
        String tiling = sContent.substring(closingBR+1);

        String tokens = sContent.substring(1, closingBR);

        //Just parse the content of the Brackets as usual with an unfinished Token
        // (It is not possible, that the content of the Brackets only consist of one Element, because of normalisation)
        LexUnfinishedToken.createInstance(getLexer(), createIUAString(tokens));

        //Filter for those LexUnfinishedLeftBracket-instances who have the same tiling on their Bracket-Depth
        int tilingPosition = 0;
        if (tokens.charAt(0) == LEXTOKEN_BR_OPEN)
            tilingPosition = getClosingBRNormal(tokens, 0)+1;
        else
            tilingPosition = getNextTilingPosition(tokens);

        char tilingType = tokens.charAt(tilingPosition);
        if (tilingType == tiling.charAt(0)) {
            //Only the last token has to be added with an additional tiling
            int startOfElement;
            if (tokens.charAt(tokens.length()-1) == LEXTOKEN_BR_CLOSE) {
                startOfElement = getOpeningBRNormal(tokens, tokens.length() - 1);
                createNewBracketInstance(getLexer(),createTiledString(tokens.substring(startOfElement), tiling));
            } else {
                startOfElement = tokens.lastIndexOf(LEXTOKEN_TILING_INDEX_CLOSE) + 1;
                //System.out.println("LexUnfinishedLeftBracket: LexFinishedLeft1 : "+createTiledString(tokens.substring(startOfElement), tiling));
                LexFinishedLeftToken.createInstance(getLexer(), createTiledString(tokens.substring(startOfElement), tiling));
            }
        } else {
            rec_derive_Tokens(tokens, tiling);
        }
    }

    /**
     * This Method returns the position of the Character in the algebra string of the matching opening, normal bracket.
     * Closing_bracket shall be the position of the closing bracket part of the bracket we are searching for.
     *
     * @param algebra
     * @param closing_bracket
     * @return
     */
    protected int getOpeningBRNormal(String algebra, int closing_bracket) {
        return getOpeningBracket(algebra, LEXTOKEN_BR_OPEN, LEXTOKEN_BR_CLOSE, closing_bracket);
    }

    private int getOpeningBracket(String algebra, Character openBR, Character closeBR, int starting_bracket) {
        int pointer = starting_bracket;
        Stack<Boolean> openBrackets = new Stack<Boolean>();
        Character temp;

        for (int i = starting_bracket-1; i>=-1; i--) {
            if (i == -1)
                return -1;
            temp = algebra.charAt(i);
            if (temp.equals(closeBR)) {
                openBrackets.push(true);
            }
            if (temp.equals(openBR)) {
                if (openBrackets.empty())
                    return i;
                else
                    openBrackets.pop();
            }
        }

        return pointer;
    }

    void createTiledToken(String tokens, String tilingToAdd) {
        //System.out.println("LexUnfinishedLeftBracket: LexFinishedLeft2 : "+createTiledString(tokens, tilingToAdd));
        LexFinishedLeftToken.createInstance(getLexer(), createTiledString(tokens, tilingToAdd));
    }

    @Override
    IUIAString createTiledString(String tokens, String tilingToAdd) {
        return createIUAString(tokens+tilingToAdd);
    }


}
