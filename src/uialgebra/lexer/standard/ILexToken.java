package uialgebra.lexer.standard;

import uialgebra.algebra.string.IUIAString;
import uialgebra.lexer.Token;

/**
 * Created by Marc Janßen on 15.05.2015.
 */
public interface ILexToken extends Token {

    public IUIAString getUIAString();
}
