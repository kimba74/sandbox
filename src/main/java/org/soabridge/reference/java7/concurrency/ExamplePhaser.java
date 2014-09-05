package org.soabridge.reference.java7.concurrency;

import org.soabridge.reference.general.concurrency.LoggingRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExamplePhaser {
    static final long patience = 60;
    static final TimeUnit patienceUnit = TimeUnit.SECONDS;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-- Phaser Barrier ---------------------------------");
        Phaser phaser = new Phaser();
        ExecutorService service = Executors.newFixedThreadPool(3);
        service.execute(new Worker("Phaser1", 2000, phaser));
        service.execute(new Worker("Phaser2", 500, phaser));
        service.execute(new Worker("Phaser3", 1500, phaser));
        // Telling ExecutorService not to accept new tasks and shutdown after last task finishes
        service.shutdown();
        // Wait for a maximum of 60s for ExecutorService to finish execution
        if (!service.awaitTermination(patience, patienceUnit)) {
            System.out.println("*** ExecutorService didn't finish after a " + patience + " " + patienceUnit + " wait ***");
            // Forcing shutdown of ExecutorService
            service.shutdownNow();
        }
    }

    static class Worker implements LoggingRunnable {
        private long patience;
        private Phaser phaser;
        private String name;

        public Worker(String name, long patience, Phaser phaser) {
            this.name = name;
            this.patience = patience;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            // A Phaser is similar to a CyclicLock with the difference that the amount of waiting threads doesn't have
            // to be set during the construction of the Phaser, instead threads register themselves when they enter the
            // protected block.
            phaser.register();
            try {
                for (int i = 1; i < 10; i++) {
                    threadMessage(i + ". \"" + name + "\" Cycle");
                    Thread.sleep(patience);
                }
                threadMessage("\"" + name + "\" Waiting for others to arrive");
                phaser.arriveAndAwaitAdvance();
                threadMessage("\"" + name + "\" Done!");
            } catch (InterruptedException e) {
                threadMessage("\"" + name + "\" Received Interrupt!");
            }
        }
    }
}
