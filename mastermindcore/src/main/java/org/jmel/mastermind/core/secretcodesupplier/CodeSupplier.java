package org.jmel.mastermind.core.secretcodesupplier;

import org.jmel.mastermind.core.Code;

import java.io.IOException;

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
    Code get() throws IOException;
}
