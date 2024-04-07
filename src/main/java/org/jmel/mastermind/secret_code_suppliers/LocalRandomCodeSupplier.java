package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;

import java.util.ArrayList;
import java.util.List;

public class LocalRandomCodeSupplier implements CodeSupplier {
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
            codeValue.add((int) (Math.random() * this.numColors)); // TODO: use better/simpler strategy here
        }

        return Code.from(codeValue, this.codeLength, this.numColors);
    }
}
