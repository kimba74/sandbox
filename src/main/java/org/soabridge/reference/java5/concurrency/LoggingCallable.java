package org.soabridge.reference.java5.concurrency;

import java.util.concurrent.Callable;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public interface LoggingCallable<V> extends Callable<V> {

    default void callerMessage(String message) {
        String name = Thread.currentThread().getName();
        System.out.printf("[%s]: %s%n", name, message);
    }
}
