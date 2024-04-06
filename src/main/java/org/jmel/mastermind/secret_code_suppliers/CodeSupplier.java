package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;

import java.util.Optional;

public interface CodeSupplier {
    Optional<Code> get();
}
