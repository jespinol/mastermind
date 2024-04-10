package org.jmel.mastermind.core.secretcodesupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A code supplier that uses java.util.Random to generate a random code of a given length and specified colors.
 */
public class LocalRandomCodeSupplier implements CodeSupplier {
    private final int codeLength;
    private final int numColors;

    private LocalRandomCodeSupplier(int codeLength, int numColors) {
        this.codeLength = codeLength;
        this.numColors = numColors;
    }

    /**
     * Creates a new instance of LocalRandomCodeSupplier with the given code length and number of colors.
     *
     * @param codeLength the length of the code
     * @param numColors  the number of colors that can be used in the code
     * @return a new instance of LocalRandomCodeSupplier
     */
    public static LocalRandomCodeSupplier of(int codeLength, int numColors) {
        return new LocalRandomCodeSupplier(codeLength, numColors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> get() {
        List<Integer> codeValue = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < this.codeLength; i++) {
            codeValue.add(random.nextInt(0, numColors));
        }

        return codeValue;
    }
}
