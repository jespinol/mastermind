package org.jmel.mastermind.core;

import java.util.List;
import java.util.Objects;

/**
 * Represents a Mastermind code which could be a secret code or a guess.
 * The value of a code is represented by a list of integers that has a certain length and each integer represents one of several allowed color. These constraints are defined by the game configuration and will match the constraints of the secret code.
 */
public class Code {
    private final List<Integer> value;

    private Code(List<Integer> value) {
        this.value = List.copyOf(value);
    }

    /**
     * Creates a Code object from a list of integers.
     *
     * @param possibleCode list of integers representing a code
     * @param codeLength   the length of the code
     * @param numColors    the number of colors that can be used in the code
     * @return a Code object
     * @throws IllegalArgumentException if the code is null, has an invalid length, or contains invalid colors
     */
    public static Code from(List<Integer> possibleCode, int codeLength, int numColors) {
        if (Objects.isNull(possibleCode)) throw new IllegalArgumentException("Null secret code");
        if (possibleCode.size() != codeLength) throw new IllegalArgumentException("Invalid secret code length");
        if (possibleCode.stream().anyMatch(i -> i < 0 || i >= numColors))
            throw new IllegalArgumentException("Invalid secret code colors");

        return new Code(possibleCode);
    }

    /**
     * Returns the value of the code.
     *
     * @return an unmodifiable list of integers representing the code
     */
    public List<Integer> value() {
        return List.copyOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Code code = (Code) o;
        return value.equals(code.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
