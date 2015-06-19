package uialgebra.algebra.string;

import uialgebra.Exceptions.MalFormedUIA_Exception;

/**
 * Created by Marc Janßen on 15.05.2015.
 */
public interface IUIAString {
    boolean isChecked();

    String getString();

    String toString();

    boolean check() throws MalFormedUIA_Exception;

    String getNormalisation() throws MalFormedUIA_Exception;

    boolean equals(Object o);
}
