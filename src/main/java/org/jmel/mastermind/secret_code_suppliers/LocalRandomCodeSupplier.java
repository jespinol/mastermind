package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class LocalRandomCodeSupplier implements Supplier<Code> {
    private final int codeLength;
    private final int numColors;

    public LocalRandomCodeSupplier(int codeLength, int numColors) {
        this.codeLength = codeLength;
        this.numColors = numColors;
    }

    @Override
    public Code get() {
        List<Integer> codeValue = new ArrayList<>();
        for (int i = 0; i < this.codeLength; i++) {
            codeValue.add((int) (Math.random() * this.numColors));
        }
        return Code.from(codeValue, codeLength, numColors);
    }
}
