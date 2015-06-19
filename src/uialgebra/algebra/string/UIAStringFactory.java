package uialgebra.algebra.string;

/**
 * Created by Marc Janßen on 15.05.2015.
 */
public class UIAStringFactory implements IUIAStringFactory {

    @Override
    public IUIAString createUIAString(String content) {
        return new UIAString(content);
    }
}
