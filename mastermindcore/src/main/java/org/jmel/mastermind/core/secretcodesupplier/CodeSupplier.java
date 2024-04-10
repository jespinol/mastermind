package org.jmel.mastermind.core.secretcodesupplier;

import java.io.IOException;
import java.util.List;

/**
 * An interface to specify code suppliers to provide a secret code.
 */
public interface CodeSupplier {
    /**
     * Gets a code from a supplier implementation.
     *
     * @return a Code object
     * @throws IOException if the code cannot be retrieved in certain implementations
     */
    List<Integer> get() throws IOException;
}
