package org.jmel.mastermind.core.secretcodesupplier;

import java.io.IOException;
import java.util.List;

/**
 * Represents a supplier of a secret code.
 */
public interface CodeSupplier {
    /**
     * Returns a code represented by a {@code List<Integers>}.
     *
     * @return a {@code List<Integers>} representing a code
     * @throws IOException if the code cannot be retrieved in certain implementations
     */
    List<Integer> get() throws IOException;
}
