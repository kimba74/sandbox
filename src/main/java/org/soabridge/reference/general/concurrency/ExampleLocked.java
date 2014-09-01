package org.soabridge.reference.general.concurrency;

import java.util.Random;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleLocked {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-- Intrinsic Lock ---------------------------------");
        // Create example shared object
        SharedObject locked = new SharedObject();
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

    static class SharedObject {
        private int counter1 = 0;
        private int counter2 = 0;

        private final Object lock1 = new Object();
        private final Object lock2 = new Object();

        public void increase1() throws InterruptedException {
            threadMessage("Thread trying to increase Counter1");
            synchronized (lock1) {
                counter1++;
                threadMessage("Thread increased Counter2");
                Thread.sleep(4000);
            }
        }

        public void increase2() throws InterruptedException {
            threadMessage("Thread trying to increase Counter2");
            synchronized (lock2) {
                counter2++;
                threadMessage("Thread increased Counter2");
                Thread.sleep(1000);
            }
        }

        private void threadMessage(String message) {
            String name = Thread.currentThread().getName();
            System.out.printf("[%s]: %s%n", name, message);
        }
    }

    static class Worker implements LoggingRunnable {
        private SharedObject locked;

        public Worker(SharedObject locked) {
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
