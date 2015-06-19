package uialgebra.lexer;

import uialgebra.Exceptions.MalFormedUIA_Exception;
import uialgebra.algebra.IUIATokenContainer;

/**
 * Created by Marc Janßen on 15.05.2015.
 */
public interface ILexer {
    public IUIATokenContainer parseTokens(IUIATokenContainer p_container) throws MalFormedUIA_Exception;
}
