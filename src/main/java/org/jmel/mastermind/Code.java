package org.jmel.mastermind;

import java.util.List;
import java.util.Objects;

public class Code {
    private final List<Integer> value;
    public Code(List<Integer> code) {
        this.value = code; // TODO add validation
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
