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

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        SharedResource resource = new SharedResource();
        service.execute(new Worker("Alpha", resource));
        service.execute(new Worker("Beta", resource));
        service.execute(new Worker("Gamma", resource));
        service.shutdown();
        if (service.awaitTermination(60, TimeUnit.SECONDS)) {
            System.out.println("*** Executor did not shut down after 60s, forcing shutdown ***");
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
        private Semaphore semaphore = new Semaphore(2, true);

        public void message(String message) {
            try {
                printMessage(message + "Entered method message()");
                semaphore.acquire();
                printMessage(message + "Acquired semaphore, now waiting my time");
                Thread.sleep(2000);
                printMessage(message + "Done waiting, releasing semaphore");
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
