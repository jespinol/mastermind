package org.jmel.mastermind;

import java.util.List;
import java.util.Objects;

public class Code {
    private static final int DEFAULT_LENGTH = 4; // TODO this is repeated here and in Game
    private final List<Integer> value;

    public Code(List<Integer> code) {
        if (isValid(code)) {
            this.value = code;
        } else {
            throw new IllegalArgumentException("Invalid code");
        }
    }

    List<Integer> getValue() {
        return value;
    }

    private boolean isValid(List<Integer> code) {
        return Objects.nonNull(code) && code.size() == DEFAULT_LENGTH;
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
