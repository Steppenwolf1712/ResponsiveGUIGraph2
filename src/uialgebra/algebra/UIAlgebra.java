package uialgebra.algebra;

import nz.ac.auckland.alm.swing.ALMLayout;
import uialgebra.Exceptions.MalFormedUIA_Exception;
import uialgebra.algebra.string.IUIAString;
import uialgebra.algebra.string.UIAString;
import uialgebra.algebra.string.UIAStringFactory;
import uialgebra.lexer.standard.UIALexer;

import javax.swing.*;

/**
 * Created by Marc Janßen on 14.05.2015.
 */
public class UIAlgebra {

    private IUIAString definition = null;

    private boolean m_isTokensCreated = false;
    private boolean m_isSound = false;

    private IUIATokenContainer m_tokenContainer;

    public UIAlgebra(String uiaString) {
        definition = new UIAString(uiaString);
        m_tokenContainer = new UIATokenContainer();
    }

    public UIATokenElement[] getElements() {
        try {
            if (readOut())
                return m_tokenContainer.getElements();
            else
                return new UIATokenElement[0];
        } catch (MalFormedUIA_Exception e) {
            System.err.println("UIAlgebra: The readout of the UIAString caused an Exception!\n" + e.getMessage());
            return new UIATokenElement[0];
        }
    }

    public UIATokenEdge[] getEdges() {
        try {
            if (readOut())
                return m_tokenContainer.getEdges();
            else
                return new UIATokenEdge[0];
        } catch (MalFormedUIA_Exception e) {
            System.err.println("UIAlgebra: The readout of the UIAString caused an Exception!\n" + e.getMessage());
            return new UIATokenEdge[0];
        }
    }

    public UIATokenHE[] getHalfEdges() {
        try {
            if (readOut())
                return m_tokenContainer.getHalfEdges();
            else
                return new UIATokenHE[0];
        } catch (MalFormedUIA_Exception e) {
            System.err.println("UIAlgebra{getHalfEdges()}: The readout of the UIAString caused an Exception!\n" + e.getMessage());
            return new UIATokenHE[0];
        }
    }

    public IUIAString getDefinition() {
        return definition;
    }

    private boolean readOut() throws MalFormedUIA_Exception {
        if (readOutTokens()) {
            if (checkSoundness()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSoundness() {
        if (m_isSound)
            return true;

        // TODO: Here is the place for checking the soundness of the Interface definition with the UIAStringChecker


        m_isSound = true;

        return m_isSound;
    }

    private boolean readOutTokens() throws MalFormedUIA_Exception {
        if (m_isTokensCreated)
            return true;

        UIALexer lex = new UIALexer(definition, new UIAStringFactory());

        this.m_tokenContainer = lex.parseTokens(m_tokenContainer);

        m_isTokensCreated = true;

        return m_isTokensCreated;
    }

    public ALMLayout createALMLayout() {
        try {
            if (readOut()) {
                ALMExtractor extractor = new ALMExtractor(new ALMLayout(), this.m_tokenContainer);
                return extractor.extractALM();
            }
        } catch (MalFormedUIA_Exception e) {
            System.err.println("UIAlgebra: The readout of the UIAString caused an Exception!\n" + e.getMessage());
        }
        return null;
    }

    public ALMLayout createALEditor(JFrame frame) {
        try {
            if (readOut()) {
                ALMLayout temp = new ALMLayout();
                frame.setLayout(temp);

                ALMExtractor extractor = new ALMExtractor(frame, temp, this.m_tokenContainer);
                return extractor.extractALM();
            }
        } catch (MalFormedUIA_Exception e) {
            System.err.println("UIAlgebra: The readout of the UIAString caused an Exception!\n" + e.getMessage());
        }
        return null;
    }
}
