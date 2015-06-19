package uialgebra.lexer.standard;

import uialgebra.algebra.string.IUIAString;

import java.util.Stack;

/**
 * Created by Marc Janßen on 15.05.2015.
 */
public abstract class LexToken implements ILexToken {

    IUIAString content;
    private UIALexer lexer = null;

    //abstract void createInstanz(IUIAString newContent);
    abstract void parseContent();

    public void init(UIALexer lexer, IUIAString content) {
        this.lexer = lexer;
        this.content = content;
        parseContent();
    }

    IUIAString createIUAString(String content) {
        return lexer.createUIAString(content);
    }

    UIALexer getLexer() {
        return lexer;
    }

    @Override
    public IUIAString getUIAString() {
        return content;
    }

    public boolean equals(Object token) {
        return this.content.getString().equals(token.toString());
    }

    public int hashCode() {
        return this.content.getString().hashCode();
    }

    /**
     * This Method returns the position of the Character in the algebra string of the matching closing, normal bracket.
     * Starting_bracket shall be the position of the opening bracket part of the bracket we are searching for.
     *
     * @param algebra
     * @param starting_bracket
     * @return
     */
    protected int getClosingBRNormal(String algebra, int starting_bracket) {
        return getClosingBracket(algebra, LEXTOKEN_BR_OPEN, LEXTOKEN_BR_CLOSE, starting_bracket);
    }


    /**
     * This Method returns the position of the Character in the algebra string of the matching closing, curly bracket.
     * Starting_bracket shall be the position of the opening bracket part of the bracket we are searching for.
     *
     * @param algebra
     * @param starting_bracket
     * @return
     */
    protected int getClosingBRTiling(String algebra, int starting_bracket) {
        return getClosingBracket(algebra, LEXTOKEN_TILING_INDEX_OPEN, LEXTOKEN_TILING_INDEX_CLOSE, starting_bracket);
    }

    private int getClosingBracket(String algebra, Character openBR, Character closeBR, int starting_bracket) {
        int pointer = starting_bracket;
        Stack<Boolean> openBrackets = new Stack<Boolean>();
        Character temp;

        for (int i = starting_bracket+1; i<algebra.length()+1; i++) {
            if (i == algebra.length())
                return -1;
            temp = algebra.charAt(i);
            if (temp.equals(openBR)) {
                openBrackets.push(true);
            }
            if (temp.equals(closeBR)) {
                if (openBrackets.empty())
                    return i;
                else
                    openBrackets.pop();
            }
        }

        return pointer;
    }

    /**
     * This method returns the position of the next vertically or horizontally tiling sign, if there is any.
     * Otherwise it returns -1.
     *
     * @param sContent
     * @return
     */
    protected int getNextTilingPosition(String sContent) {
        int erg = sContent.indexOf(LEXTOKEN_OP_HORI_TILING);
        if (erg<0)
            erg = sContent.indexOf(LEXTOKEN_OP_VERT_TILING);
        else {
            int temp = sContent.indexOf(LEXTOKEN_OP_VERT_TILING);
            erg = (erg<temp || temp<0?erg:temp);
        }
        return erg;
    }
}
