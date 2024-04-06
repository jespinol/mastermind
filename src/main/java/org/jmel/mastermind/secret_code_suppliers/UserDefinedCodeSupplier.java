package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;

import java.util.Optional;

public class UserDefinedCodeSupplier implements CodeSupplier {
    private final Code secretCode;

    public UserDefinedCodeSupplier(Code secretCode) {
        this.secretCode = secretCode;
    }

    @Override
    public Optional<Code> get() {
        return Optional.of(secretCode);
    }
}
