package uialgebra.lexer.standard;

import uialgebra.algebra.string.IUIAString;

/**
 * Created by Marc Janßen on 15.05.2015.
 */
public class LexUnfinishedToken extends LexToken {

    protected LexUnfinishedToken(UIALexer uiaLexer, IUIAString content) {
        init(uiaLexer, content);
    }

    public static void createInstance(UIALexer lexer, IUIAString newContent) {
        if (!lexer.containsTokenFor(newContent))
            lexer.pushToken(new LexUnfinishedToken(lexer, newContent));
    }


    @Override
    void parseContent() {
        String sContent = content.getString();

        if (sContent.charAt(0) == LEXTOKEN_BR_OPEN) {
            int closingBR = getClosingBRNormal(sContent, 0);
            if (closingBR == sContent.length() - 1) {//trivial, remove the surrounding brackets and test again in another
                // instance
                createInstance(getLexer(), createIUAString(sContent.substring(1, sContent.length() - 1)));
            } else {
                //There is an attached algebra part, remove the string in brackets and go on like this:
                //(A|B)/C+ => A/ * B/ * A|B * /C [* C++]
                int endOfTilingIndex = getClosingBRTiling(sContent, closingBR + 2) + 1;
                String tail = sContent.substring(endOfTilingIndex);
                if (hasTiling(tail)) {
                    if (tail.charAt(0) == LEXTOKEN_BR_OPEN) {
                        //Case (A+)/(B+) or (A+)/(B+)/C++
                        rec_LexUnfinishedBracket(sContent.substring(closingBR + 1), endOfTilingIndex - (closingBR + 1));
                    } else {
                        //Case (A+)/B+
                        createInstance(getLexer(), createIUAString(tail));
                        int nextTiling = getNextTilingPosition(tail) + endOfTilingIndex;//+EndOfTilingIndex, because tails is missing that many letters in comparison to sContent
                        //System.out.println("LexUnfinishedToken: Finisher 1 RightToken: "+sContent.substring(closingBR + 1, nextTiling));
                        LexFinishedRightToken.createInstance(getLexer(), createIUAString(sContent.substring(closingBR + 1, nextTiling)));
                    }
                } else {
                    //Case (A+)/B {(A+)/(B) shouldn't be possible after normalisation of the UIAString}
                    //System.out.println("LexUnfinishedToken: Finisher 2 RightToken: "+sContent.substring(closingBR + 1));
                    LexFinishedRightToken.createInstance(getLexer(), createIUAString(sContent.substring(closingBR + 1)));
                }
                LexUnfinishedLeftBracketToken.createInstance(getLexer(), createIUAString(sContent.substring(0, endOfTilingIndex)));
            }
        } else {
            if (hasOnlyOneTiling(sContent)) {
                Character test = sContent.charAt(0);
                if (test.equals(LEXTOKEN_OP_HORI_TILING) || test.equals(LEXTOKEN_OP_VERT_TILING)) {
                    //Only a Half-Edge /{id}Element, it can be put into a finished Token
                    //System.out.println("LexUnfinishedToken: Finisher 3 RightToken: "+sContent);
                    LexFinishedRightToken.createInstance(getLexer(), createIUAString(sContent));
                } else {
                    if (sContent.charAt(sContent.length() - 1) == LEXTOKEN_TILING_INDEX_CLOSE) {
                        //Only a Half-Edge Element/{id}, it can be put into a finished Token
                        //System.out.println("LexUnfinishedToken: Finisher 4 LeftToken: "+sContent);
                        LexFinishedLeftToken.createInstance(getLexer(), createIUAString(sContent));
                    } else {
                        //Still the Case Element/{id}Element is left, so spilt it into two finishedTokens
                        int leftside = getNextTilingPosition(sContent);
                        int rightside = getClosingBRTiling(sContent, leftside + 1);

                        //System.out.println("LexUnfinishedToken: Finisher 5 RightToken: "+sContent.substring(leftside));
                        //System.out.println("LexUnfinishedToken: Finisher 6 LeftToken: "+sContent.substring(0, rightside + 1)+"\n\tsContent: "+sContent);
                        LexFinishedRightToken.createInstance(getLexer(), createIUAString(sContent.substring(leftside)));
                        LexFinishedLeftToken.createInstance(getLexer(), createIUAString(sContent.substring(0, rightside + 1)));
                    }
                }
            } else {
                Character test = sContent.charAt(0);
                if (test.equals(LEXTOKEN_OP_HORI_TILING) || test.equals(LEXTOKEN_OP_VERT_TILING)) {
                    int endOfTiling = getClosingBRTiling(sContent, 1) + 1;
                    if (sContent.charAt(endOfTiling) == LEXTOKEN_BR_OPEN) {
                        //Case /(A+)+
                        int endOfBrackets = getClosingBRNormal(sContent, endOfTiling)+1;
                        if (endOfBrackets == sContent.length()) {
                            //Case /(A+)
                            LexUnfinishedRightBracketToken.createInstance(getLexer(),createIUAString(sContent));
                        } else {
                            //Case /(A+)/B+
                            LexUnfinishedRightBracketToken.createInstance(getLexer(),createIUAString(sContent.substring(0,endOfBrackets)));
                            createInstance(getLexer(),createIUAString(sContent.substring(endOfTiling)));
                        }
                    } else {
                        //Case /A+ => A+ * /A
                        int nextTilingPosition = getNextTilingPosition(sContent.substring(1))+1;
                        LexFinishedRightToken.createInstance(getLexer(), createIUAString(sContent.substring(0, nextTilingPosition)));
                        createInstance(getLexer(), createIUAString(sContent.substring(endOfTiling)));
                    }
                } else {
                    //Case A/B+
                    int nextTiling = getNextTilingPosition(sContent);
                    int endOfTiling = getClosingBRTiling(sContent, nextTiling + 1) + 1;
                    LexFinishedLeftToken.createInstance(getLexer(), createIUAString(sContent.substring(0, endOfTiling)));

                    if (sContent.charAt(endOfTiling) == LEXTOKEN_BR_OPEN) {
                        //Case A/(B+)/C+
                        rec_LexUnfinishedBracket(sContent.substring(nextTiling), endOfTiling - nextTiling);
                    } else {
                        //Case A/B+
                        createInstance(getLexer(), createIUAString(sContent.substring(nextTiling)));
                    }

                }

            }
        }
    }

    private void rec_LexUnfinishedBracket(String substring, int bracketPosition) {
        int closingBR = getClosingBRNormal(substring, bracketPosition);
        if (closingBR == substring.length()-1) {
            //Case /(A+)
            LexUnfinishedRightBracketToken.createInstance(getLexer(), createIUAString(substring));
        } else {
            //Case /(A+)/C+
            int endOfSecondTiling = getClosingBRTiling(substring, closingBR+2)+1;
            LexUnfinishedRightBracketToken.createInstance(getLexer(), createIUAString(substring.substring(0, closingBR + 1)));
            LexUnfinishedLeftBracketToken.createInstance(getLexer(), createIUAString(substring.substring(bracketPosition, endOfSecondTiling)));

            //What comes after /(A+)/ ? The interpretation of /C+ is left
            String lastpart = substring.substring(closingBR+1);
            if (substring.charAt(endOfSecondTiling) == LEXTOKEN_BR_OPEN) {
                //Case /(C+)[/D+]
                rec_LexUnfinishedBracket(lastpart, endOfSecondTiling - (closingBR + 1));
            } else {
                //Case /C+
                createInstance(getLexer(), createIUAString(substring));
            }
        }
    }

    /**
     * This method is for special cases, where it is to check whether a string consist of only one Element
     *
     * tokens.indexOf(LEXTOKEN_OP_HORI_TILING)+tokens.indexOf(LEXTOKEN_OP_VERT_TILING)>=0
     *
     * @param tokens
     * @return
     */
    boolean hasTiling(String tokens) {
        return tokens.indexOf(LEXTOKEN_OP_HORI_TILING)+tokens.indexOf(LEXTOKEN_OP_VERT_TILING)>=0;
    }

    /**
     * This method gives information about the Number of tilings in a certain string.
     * If there is more than one tiling-token in the value of the parameter, it returns false. True otherwise
     *
     * In a normalised UIAString is no possible Single Element without any tiling beside it. A case of 0 tilings is to neglect.
     *
     * @param temp
     * @return
     */
    protected boolean hasOnlyOneTiling(String temp) {
        String[] splitted = temp.split(getSplitter(LEXTOKEN_OP_HORI_TILING+""));
        int countSplit = 0;

        if(splitted.length>2)
            return false;
        else {
            countSplit = splitted.length;
            for (String s : splitted)
                countSplit += s.split(getSplitter(LEXTOKEN_OP_VERT_TILING+"")).length -1;

            if (countSplit>2)
                return false;
            else
                return true;
        }
    }

    private String getSplitter(String para) {
        return (para.equals("\\")||para.equals("|")?"\\"+para: para);
    }
}
