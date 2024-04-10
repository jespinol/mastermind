package org.jmel.mastermind.core.secretcodesupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A code supplier that uses java.util.Random to generate a random code of a given length where each code element is one
 * of the allowed colors.
 */
public class LocalRandomCodeSupplier implements CodeSupplier {
    private final int codeLength;
    private final int numColors;

    private LocalRandomCodeSupplier(int codeLength, int numColors) {
        this.codeLength = codeLength;
        this.numColors = numColors;
    }

    public static LocalRandomCodeSupplier of(int codeLength, int numColors) {
        return new LocalRandomCodeSupplier(codeLength, numColors);
    }

    @Override
    public List<Integer> get() {
        List<Integer> codeValue = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < this.codeLength; i++) {
            codeValue.add(random.nextInt(0, numColors));
        }

        return List.copyOf(codeValue);
    }
}
