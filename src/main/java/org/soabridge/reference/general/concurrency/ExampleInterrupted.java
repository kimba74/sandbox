package org.soabridge.reference.general.concurrency;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleInterrupted {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-- Interrupted Thread -----------------------------");
        // Create example thread
        Thread thread = new Thread(new Worker("Repeat Message until interrupted"), "Interrupted");
        // Start example thread
        thread.start();
        // Wait for 10 seconds or until example thread is finished
        System.out.println("*** Main Thread waiting ***");
        thread.join(10000);
        // If example thread is still alive interrupt it
        if (thread.isAlive()) {
            System.out.println("*** Main Thread waited long enough ***");
            thread.interrupt();
        }
        else {
            System.out.println("*** Main Thread joined with utility thread ***");
        }
        // Wait until thread is really finished
        thread.join();
    }

    static class Worker implements LoggingRunnable {
        private String message;

        public Worker(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    threadMessage((i + 1) + ". " + message);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    threadMessage("Thread got interrupted");
                    return;
                }
            }
        }
    }

}
