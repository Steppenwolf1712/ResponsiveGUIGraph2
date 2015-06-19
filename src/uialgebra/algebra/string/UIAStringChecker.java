package uialgebra.algebra.string;

import uialgebra.Exceptions.MalFormedUIA_Exception;
import uialgebra.lexer.Token;

import java.util.Stack;

/**
 * Created by Marc Janßen on 14.05.2015.
 */
public class UIAStringChecker {

    private static UIAStringChecker single = null;

    private UIAStringChecker() {
    }

    public static UIAStringChecker getInstance() {
        if (single==null)
            single = new UIAStringChecker();
        return single;
    }

    public boolean checkUIAString(IUIAString toCheck) throws MalFormedUIA_Exception {
        UIAString parameter = (UIAString)toCheck;

        if (parameter.isChecked())
            return true;

        String erg = parameter.getString();

        erg = startCheck(erg);

        // TODO: Here it is possible to add some checks, whether there are some impossible combinations of Tokens like "|/" or ")("

        parameter.setChecked(true);
        parameter.setNormalisedString(erg);

        return true;
    }

    private String startCheck(String toCheck) throws MalFormedUIA_Exception {
        //Every part of the Algebra between divided by stars have to be an sound algebra for itself
        //Each one will be checked separate
        String[] toCheck2 = toCheck.split("\\"+Token.LEXTOKEN_OP_CONCAT);

        String erg = initialCheck(toCheck2[0]);
        for (int i=1;i<toCheck2.length;i++)
            erg += Token.LEXTOKEN_OP_CONCAT+initialCheck(toCheck2[i]);

        return erg;
    }

    private String initialCheck(String s) throws MalFormedUIA_Exception {
        String erg = bracketTilingCheck(s);

        erg = checkTilingIDs(erg);

        return erg;
    }

    private String checkTilingIDs(String p_toCheck) throws MalFormedUIA_Exception {
        String erg = p_toCheck;

        erg = checkTilingIDs_Helper(erg, Token.LEXTOKEN_OP_HORI_TILING);
        erg = checkTilingIDs_Helper(erg, Token.LEXTOKEN_OP_VERT_TILING);

        return erg;
    }

    private String checkTilingIDs_Helper(String para, Character tiling) throws MalFormedUIA_Exception {
        String erg = "", splitter = tiling+"";
        splitter = (splitter.equals("\\")|| splitter.equals("|")? '\\'+splitter:splitter);

        String[] temp1 = para.split(splitter);

        if (temp1[0].isEmpty())
            throw new MalFormedUIA_Exception("An User-Interface-Algebra String must not start with a tiling operation!");
        erg = temp1[0];

        for (int i =1; i<temp1.length; i++) {
            if (temp1[i].startsWith("{")) {
                checkID(temp1[i]);
            } else {
                temp1[i] = createTilingID()+temp1[i];
            }
            erg += tiling+temp1[i];
        }
        return erg;
    }

    private int m_TilingIndex = 0;

    private String createTilingID() {
        return "{AutoGenIndex_"+ ++m_TilingIndex +"}";
    }

    private void checkID(String temp1) throws MalFormedUIA_Exception {
        for (int i = 1;i<temp1.length(); i++) {
            char test = temp1.charAt(i);
            switch (test) {
                case Token.LEXTOKEN_BR_CLOSE:
                case Token.LEXTOKEN_BR_OPEN:
                    case Token.LEXTOKEN_OP_CONCAT:
                case Token.LEXTOKEN_OP_HORI_TILING:
                case Token.LEXTOKEN_OP_VERT_TILING:
                case Token.LEXTOKEN_TILING_INDEX_OPEN:
                    throw new MalFormedUIA_Exception("The id of a tiling consists of some non allowed characters!");
                case Token.LEXTOKEN_TILING_INDEX_CLOSE:
                    return;
            }
        }

    }

    private String bracketTilingCheck(String p_toCheck) throws MalFormedUIA_Exception {
        String erg = p_toCheck;

        Stack<BracketPointer> bracketStack = new Stack<BracketPointer>();
        Character currentTiling = null;

        for (int i = 0; i<erg.length(); i++) {
            Character pointer = erg.charAt(i);
            switch (pointer) {
                case Token.LEXTOKEN_BR_OPEN:
                    bracketStack.push(new BracketPointer(i));
                    if (currentTiling != null)
                        currentTiling = switchTiling(currentTiling);
                    break;
                case Token.LEXTOKEN_BR_CLOSE:
                    if (bracketStack.empty())
                        throw new MalFormedUIA_Exception("There is a closing bracket without an matching open Bracket!");
                    BracketPointer temp = bracketStack.pop();
                    if (temp.isNotNecassary) {
                        erg = erg.substring(0,temp.m_position)+erg.substring(temp.m_position+1,i)+erg.substring(i+1,erg.length());
                        i--;
                        //throw new MalFormedUIA_Exception("The User-Interface-Algebra String consists some unnecessary Brackets");
                    }
                    //A Closing bracket should not be in front of all tiling tokens of the String. There has to be a Tiling before this bracket symbol
                    currentTiling = switchTiling(currentTiling);
                    break;
                case Token.LEXTOKEN_OP_HORI_TILING:
                case Token.LEXTOKEN_OP_VERT_TILING:
                    if (currentTiling==null) {
                        currentTiling = pointer;
                    } else {
                        if (!pointer.equals(currentTiling))
                            throw new MalFormedUIA_Exception("The operator " + Token.LEXTOKEN_OP_HORI_TILING + " and " + Token.LEXTOKEN_OP_VERT_TILING + " have to be the same on the same bracket-level\n" +
                                    "and they have to be switching between the levels!");
                    }
                    if (!bracketStack.empty())
                        bracketStack.peek().isNotNecassary = false;
                break;
            }
        }
        if (!bracketStack.empty())
            throw new MalFormedUIA_Exception("There is an unopened Bracket left!\n(The Star-Operator must not be in some Brackets)");

        return erg;
    }

    private char switchTiling(char p_cTiling) {
        if (p_cTiling==Token.LEXTOKEN_OP_HORI_TILING)
            return Token.LEXTOKEN_OP_VERT_TILING;
        else
            return Token.LEXTOKEN_OP_HORI_TILING;
    }

    public class BracketPointer {
        public boolean isNotNecassary;
        public int m_position;

        public BracketPointer(int p_position) {
            m_position = p_position;
        }
    }
}
