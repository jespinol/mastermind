package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        Random random = new Random();
        for (int i = 0; i < this.codeLength; i++) {
            codeValue.add(random.nextInt(0, numColors));
        }

        return Code.from(codeValue, this.codeLength, this.numColors);
    }
}
