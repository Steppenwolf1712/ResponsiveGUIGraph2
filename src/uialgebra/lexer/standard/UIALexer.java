package uialgebra.lexer.standard;

import uialgebra.Exceptions.MalFormedUIA_Exception;
import uialgebra.algebra.IUIATokenContainer;
import uialgebra.algebra.string.IUIAString;
import uialgebra.algebra.string.IUIAStringFactory;
import uialgebra.lexer.ILexer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc Janßen on 15.05.2015.
 */
public class UIALexer implements ILexer {

    private Map<IUIAString, ILexToken> hashed_tokens = null;
    private IUIATokenContainer container = null;
    private IUIAStringFactory factory = null;

    private IUIAString content = null;
    private boolean hasTokens = false;

    public UIALexer(IUIAString content, IUIAStringFactory uiaFactory) throws MalFormedUIA_Exception {
        if (!content.isChecked())
            content.check();

        this.content = content;
        this.factory = uiaFactory;
    }

    IUIAString createUIAString(String content) {
        return factory.createUIAString(content);
    }

    public IUIATokenContainer parseTokens(IUIATokenContainer p_container) throws MalFormedUIA_Exception {
        if (hasTokens && container!=null && container.equals(p_container))
            return container;

        hashed_tokens = new HashMap<IUIAString, ILexToken>();

        IUIAString[] parts = prepareUIAString(content);

        //Starts the disintegration of the UIAString with help of a Interpreter-Pattern
        for (IUIAString s: parts)
            LexUnfinishedToken.createInstance(this, s);

        this.container = p_container;

        extractUIATokens();

        hasTokens = true;
        return container;
    }

    private void extractUIATokens() {
        for (ILexToken token: hashed_tokens.values())
            if (token instanceof LexFinishedToken)
                container.addHalfEdge((LexFinishedToken)token);
    }

    boolean containsTokenFor(IUIAString toCompare) {
        return hashed_tokens.containsKey(toCompare);
    }

    void pushToken(ILexToken token) {
        hashed_tokens.put(token.getUIAString(), token);
    }

    /**
     * This method tries to get the normalised UIA String out of the parameter and split it on the star-Operator.
     *
     * @param string
     * @return
     * @throws uialgebra.Exceptions.MalFormedUIA_Exception
     */
    private IUIAString[] prepareUIAString(IUIAString string) throws MalFormedUIA_Exception {
        String sContent = string.getNormalisation();
        String[] erg1 = sContent.split("\\*");
        IUIAString[] erg = new IUIAString[erg1.length];

        for (int i = 0;i<erg1.length; i++) {
            erg[i] = factory.createUIAString(erg1[i]);
        }

        return erg;
    }
}
