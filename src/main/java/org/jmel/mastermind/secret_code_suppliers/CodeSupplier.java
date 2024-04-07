package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;

import java.io.IOException;

public interface CodeSupplier {
    Code get() throws IOException;
}
