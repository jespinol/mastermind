package org.jmel.mastermind.cli;

/**
 * Represents the preference for a secret code supplier.
 * <p></p>
 * * Available preferences are:
 * <ul>
 *     <li>{@link #RANDOM_ORG_API}</li>
 *     <li>{@link #LOCAL_RANDOM}</li>
 *     <li>{@link #USER_DEFINED}</li>
 * </ul>
 */
public enum CodeSupplierPreference {
    /**
     * See {@link org.jmel.mastermind.core.secretcodesupplier.ApiCodeSupplier}
     */
    RANDOM_ORG_API,
    /**
     * See {@link org.jmel.mastermind.core.secretcodesupplier.LocalRandomCodeSupplier}
     */
    LOCAL_RANDOM,
    /**
     * See {@link org.jmel.mastermind.core.secretcodesupplier.UserDefinedCodeSupplier}
     */
    USER_DEFINED
}
