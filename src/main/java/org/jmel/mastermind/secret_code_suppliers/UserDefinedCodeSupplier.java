package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;

import java.util.function.Supplier;

public class UserDefinedCodeSupplier implements Supplier<Code> {
    private final Code secretCode;

    public UserDefinedCodeSupplier(Code secretCode) {
        this.secretCode = secretCode;
    }

    @Override
    public Code get() {
        return secretCode;
    }
}
