package org.soabridge.reference.general.threads;

import java.util.Random;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleLocked {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-- Intrinsic Lock ---------------------------------");
        // Create example shared object
        SharedLocked locked = new SharedLocked();
        // Create example threads
        Thread thread1 = new Thread(new Worker(locked), "Locked1");
        Thread thread2 = new Thread(new Worker(locked), "Locked2");
        // Start example threads
        thread1.start();
        thread2.start();
        // Wait until both example threads are finished
        thread1.join();
        thread2.join();
    }

    static class Worker implements LoggingRunnable {
        private SharedLocked locked;

        public Worker(SharedLocked locked) {
            this.locked = locked;
        }

        @Override
        public void run() {
            Random rand = new Random(System.currentTimeMillis());
            for (int i = 1; i <= 10; i++) {
                try {
                    if (rand.nextInt(100) % 2 > 0) {
                        threadMessage(i + ". Increasing Counter1");
                        locked.increase1();
                    } else {
                        threadMessage(i + ". Increasing Counter2");
                        locked.increase2();
                    }
                } catch (InterruptedException e) {
                    threadMessage("Received Interrupt!");
                    return;
                }
            }
            threadMessage("Done!");
        }
    }
}
