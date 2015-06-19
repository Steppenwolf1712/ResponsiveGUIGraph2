package uialgebra.algebra.string;

import uialgebra.Exceptions.MalFormedUIA_Exception;

/**
 * Created by Marc Janßen on 14.05.2015.
 */
public class UIAString implements IUIAString {

    private final String contentUIAString;
    private String normalised = null;

    private boolean isChecked = false;

    public UIAString(String uiaString) {
        contentUIAString = uiaString;
    }

    void setChecked(boolean checked) {
        isChecked = checked;
    }

    void setNormalisedString(String p_normalised) {
        this.normalised = p_normalised;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    public boolean equals(Object o) {
        return this.contentUIAString.equals(o.toString());
    }

    @Override
    public String getString() {
        return this.toString();
    }

    @Override
    public String toString() {
        return contentUIAString;
    }

    @Override
    public boolean check() throws MalFormedUIA_Exception {
        return UIAStringChecker.getInstance().checkUIAString(this);
    }

    @Override
    public String getNormalisation() throws MalFormedUIA_Exception {
        if (check())
            return normalised;
        return null;
    }

    public int hashCode() {
        return this.getString().hashCode();
    }
}
