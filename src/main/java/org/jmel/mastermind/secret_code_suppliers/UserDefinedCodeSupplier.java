package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;

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
