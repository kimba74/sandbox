package org.soabridge.reference.java8.lambda;

import java.util.Objects;

/**
 * Missing documentation
 *
 * @since 1.0
 */
@FunctionalInterface
public interface SimpleMath {

    float evaluate(float num);

    default SimpleMath andThen(SimpleMath after) {
        Objects.requireNonNull(after);
        return n -> after.evaluate(evaluate(n));
    }

    default  SimpleMath compose(SimpleMath before) {
        Objects.requireNonNull(before);
        return n -> evaluate(before.evaluate(n));
    }
}
