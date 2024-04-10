package org.jmel.mastermind.core.secretcodesupplier;

import java.util.List;

/**
 * A code supplier that uses predefined code as a secret code.
 */
public class UserDefinedCodeSupplier implements CodeSupplier {
    private final List<Integer> secretCode;

    private UserDefinedCodeSupplier(List<Integer> secretCode) {
        this.secretCode = List.copyOf(secretCode);
    }

    /**
     * Creates a new instance of UserDefinedCodeSupplier with the given secret code.
     *
     * @param secretCode the secret code
     * @return a new instance of UserDefinedCodeSupplier
     */
    public static UserDefinedCodeSupplier of(List<Integer> secretCode) {
        return new UserDefinedCodeSupplier(secretCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> get() {
        return secretCode;
    }
}
