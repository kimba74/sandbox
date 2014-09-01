package org.soabridge.reference.java5.concurrency;

import org.soabridge.reference.general.concurrency.LoggingRunnable;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleCyclicBarrier {
    static final long patience = 60;
    static final TimeUnit patienceUnit = TimeUnit.SECONDS;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-- Cyclic Barrier -------------------------------");
        CyclicBarrier barrier = new CyclicBarrier(3);
        ExecutorService service = Executors.newFixedThreadPool(3);
        service.execute(new Worker("Cyclic1", 1000, barrier));
        service.execute(new Worker("Cyclic2", 2000, barrier));
        service.execute(new Worker("Cyclic3", 3000, barrier));
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
        private CyclicBarrier barrier;
        private String name;

        public Worker(String name, long patience, CyclicBarrier barrier) {
            this.name = name;
            this.patience = patience;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= 10; i++) {
                    threadMessage(i + ". \"" + name + "\" Cycle");
                    Thread.sleep(patience);
                }
                threadMessage("\"" + name + "\" Waiting for others to arrive");
                barrier.await();
                threadMessage("\"" + name + "\" Done!");
            } catch (InterruptedException e) {
                threadMessage("\"" + name + "\" Received Interrupt!");
            } catch (BrokenBarrierException e) {
                threadMessage("\"" + name + "\" Barrier was broken!");
            }
        }
    }
}
