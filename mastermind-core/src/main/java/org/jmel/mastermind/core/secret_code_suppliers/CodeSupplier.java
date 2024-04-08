package org.jmel.mastermind.core.secret_code_suppliers;

import org.jmel.mastermind.core.Code;

import java.io.IOException;

public interface CodeSupplier {
    Code get() throws IOException;
}
