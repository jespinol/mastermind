package org.jmel.mastermind.core.secretcodesupplier;

import java.io.IOException;
import java.util.List;

/**
 * An interface to specify code suppliers to provide a secret code.
 */
public interface CodeSupplier {
    /**
     * Returns a code represented by a {@code List<Integers>} from a supplier implementation.
     *
     * @return a {@code List<Integers>} representing a code
     * @throws IOException if the code cannot be retrieved in certain implementations
     */
    List<Integer> get() throws IOException;
}
