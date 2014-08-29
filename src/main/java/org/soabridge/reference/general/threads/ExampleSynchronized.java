package org.soabridge.reference.general.threads;

import java.util.Random;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleSynchronized {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-- Synchronized Method ----------------------------");
        // Create example shared object
        SharedSynchronized counter = new SharedSynchronized();
        // Create example threads
        Thread thread1 = new Thread(new Worker(counter), "Synchronized1");
        Thread thread2 = new Thread(new Worker(counter), "Synchronized2");
        // Start example threads
        thread1.start();
        thread2.start();
        // Wait until both example threads are finished
        thread1.join();
        thread2.join();
    }

    static class Worker implements LoggingRunnable {
        private SharedSynchronized counter;

        public Worker(SharedSynchronized counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            Random rand = new Random(System.currentTimeMillis());
            for (int i = 1; i <= 10; i++) {
                try {
                    if (rand.nextInt(100) % 2 > 0) {
                        threadMessage(i + ". Decreasing counter");
                        counter.decrease();
                    } else {
                        threadMessage(i + ". Increasing counter");
                        counter.increase();
                    }
                    // Either yield() or sleep() necessary to prevent thread from being greedy.
                    Thread.yield();
                } catch (InterruptedException e) {
                    threadMessage("Received interrupt!");
                    return;
                }
            }
            threadMessage("Done!");
        }
    }

}
