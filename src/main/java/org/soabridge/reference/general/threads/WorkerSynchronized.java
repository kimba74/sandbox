package org.soabridge.reference.general.threads;

import java.util.Random;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class WorkerSynchronized implements LoggingRunnable {

    private SharedSynchronized counter;

    public WorkerSynchronized(SharedSynchronized counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        Random rand = new Random(System.currentTimeMillis());
        for (int i=1; i<=10; i++) {
            try {
                if (rand.nextInt(100) % 2 > 0) {
                    threadMessage(i + ". Decreasing counter");
                    counter.decrease();
                }
                else {
                    threadMessage(i + ". Increasing counter");
                    counter.increase();
                }
                // Either yield() or sleep() necessary to prevent thread from being greedy.
                Thread.yield();
            }
            catch (InterruptedException e) {
                threadMessage("Received interrupt!");
                return;
            }
        }
        threadMessage("Done!");
    }

}
