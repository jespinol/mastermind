package org.jmel.mastermind.core.secret_code_suppliers;

import org.jmel.mastermind.core.Code;

/**
 * A code supplier that uses predefined code as a secret code.
 */
public class UserDefinedCodeSupplier implements CodeSupplier {
    private final Code secretCode;

    public UserDefinedCodeSupplier(Code secretCode) {
        this.secretCode = secretCode;
    }

    @Override
    public Code get() {
        return secretCode;
    }
}
