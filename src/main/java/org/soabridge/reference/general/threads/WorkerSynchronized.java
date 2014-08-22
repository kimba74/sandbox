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
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (rand.nextInt(100) % 2 > 0) {
                    threadMessage("Decreasing counter");
                    counter.decrease();
                }
                else {
                    threadMessage("Increasing counter");
                    counter.increase();
                }
                // Either yield() or sleep() necessary to make thread less greedy
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
