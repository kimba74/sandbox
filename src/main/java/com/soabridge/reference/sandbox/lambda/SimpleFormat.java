package com.soabridge.reference.sandbox.lambda;

import java.util.Objects;

/**
 * Missing documentation
 *
 * @since 1.0
 */
@FunctionalInterface
public interface SimpleFormat {

    String format(String msg);

    default SimpleFormat andThen(SimpleFormat after) {
        Objects.requireNonNull(after);
        return r -> after.format(format(r));
    }

    default SimpleFormat compose(SimpleFormat before) {
        Objects.requireNonNull(before);
        return r -> format(before.format(r));
    }
}
