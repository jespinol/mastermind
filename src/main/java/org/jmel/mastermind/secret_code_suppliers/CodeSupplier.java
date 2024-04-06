package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;
import org.jmel.mastermind.custom_exceptions.ApiQuotaExceededException;
import org.jmel.mastermind.custom_exceptions.InvalidCodeException;

public interface CodeSupplier  {
    Code get() throws InvalidCodeException, ApiQuotaExceededException;
}
