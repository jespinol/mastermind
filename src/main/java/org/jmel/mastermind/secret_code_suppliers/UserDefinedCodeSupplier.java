package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;

import java.util.List;
import java.util.function.Supplier;

public class UserDefinedCodeSupplier implements Supplier<Code> {
    private final Code secretCode;

    public UserDefinedCodeSupplier(List<Integer> inputCode) {
        this.secretCode = new Code(inputCode);
    }

    @Override
    public Code get() {
        return secretCode;
    }
}