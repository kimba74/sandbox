package org.soabridge.reference.java5.concurrency;

import org.soabridge.reference.general.threads.LoggingRunnable;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class WorkerExecutor implements LoggingRunnable {

    private String name;

    public WorkerExecutor(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for (int i=1; i<=10; i++) {
            threadMessage(i + ". \"" + name + "\" Cycle print message");
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                threadMessage("Received Interrupt!");
                return;
            }
        }
    }
}
