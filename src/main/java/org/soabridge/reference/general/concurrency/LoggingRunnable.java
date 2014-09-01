package org.soabridge.reference.general.concurrency;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public interface LoggingRunnable extends Runnable {

    default void threadMessage(String message) {
        String name = Thread.currentThread().getName();
        System.out.printf("[%s]: %s%n", name, message);
    }

}
