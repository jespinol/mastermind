package org.jmel.mastermind;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Code {
    private final List<Integer> value;

    private Code(List<Integer> value) {
        this.value = Collections.unmodifiableList(value);
    }

    public static Code from(List<Integer> possibleCode, int codeLength, int numColors) {
        if (Objects.isNull(possibleCode))
            throw new IllegalArgumentException("Null secret code");
        if (possibleCode.size() != codeLength)
            throw new IllegalArgumentException("Invalid secret code length");
        if (possibleCode.stream().anyMatch(i -> i < 0 || i >= numColors))
            throw new IllegalArgumentException("Invalid secret code colors");

        return new Code(possibleCode);
    }

    public List<Integer> value() {
        return value;
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
