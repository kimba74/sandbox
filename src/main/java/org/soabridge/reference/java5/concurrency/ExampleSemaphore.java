package org.soabridge.reference.java5.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleSemaphore {
    static final long patience = 60;
    static final TimeUnit patienceUnit = TimeUnit.SECONDS;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-- Semaphore --------------------------------------");
        ExecutorService service = Executors.newFixedThreadPool(4);
        SharedResource resource = new SharedResource();
        service.execute(new Worker("Alpha", resource));
        service.execute(new Worker("Beta", resource));
        service.execute(new Worker("Gamma", resource));
        // Telling ExecutorService not to accept new tasks and shutdown after last task finishes
        service.shutdown();
        // Wait for a maximum of 60s for ExecutorService to finish execution
        if (!service.awaitTermination(patience, patienceUnit)) {
            System.out.println("*** ExecutorService didn't finish after a " + patience + " " + patienceUnit + " wait ***");
            // Forcing shutdown of ExecutorService
            service.shutdownNow();
        }
    }

    static class Worker implements Runnable {
        private SharedResource resource;
        private String name;

        Worker(String name, SharedResource resource) {
            this.name = name;
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i=0; i<10; i++)
                resource.message(name + " (Cycle " + i + ") ");
        }
    }

    static class SharedResource {
        // A Semaphore will allow the specified amount of concurrent threads to enter a protected block, any additional
        // threads will halt until at least one Semaphore is returned.
        private Semaphore semaphore = new Semaphore(2, true);

        public void message(String message) {
            try {
                printMessage(message + "Entered method message()");
                // Acquiring a Semaphore, if one is available. If no Semaphore is left wait until one is returned
                semaphore.acquire();
                printMessage(message + "Acquired semaphore, now waiting my time");
                Thread.sleep(1000);
                printMessage(message + "Done waiting, releasing semaphore");
                // Release Semaphore after completing the work thus allowing the next thread to enter the protected block
                semaphore.release();
            }
            catch (InterruptedException e) {
                printMessage(message + "Received Interrupt!");
            }
        }

        private void printMessage(String message) {
            String name = Thread.currentThread().getName();
            System.out.printf("[%s]: %s%n", name, message);
        }
    }
}
