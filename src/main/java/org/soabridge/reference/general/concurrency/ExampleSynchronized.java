package org.soabridge.reference.general.concurrency;

import java.util.Random;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleSynchronized {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-- Synchronized Method ----------------------------");
        // Create example shared object
        SharedObject counter = new SharedObject();
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

    static class SharedObject {
        private int counter = 0;
        private Random rand = new Random(System.currentTimeMillis());

        public synchronized void decrease() throws InterruptedException {
            counter--;
            int wait = rand.nextInt(3000);
            threadMessage("Decreasing value to " + counter + " now waiting for " + wait + "ms");
            // InterruptedException needs to be re-thrown so Runnable can determine if thread was interrupted.
            // Once sleep() throws Interrupted Exception, "interrupted" flag of thread is cleared and Runnable
            // will never know it was interrupted.
            Thread.sleep(wait);
        }

        public synchronized void increase() throws InterruptedException {
            counter++;
            int wait = rand.nextInt(3000);
            threadMessage("Increasing value to " + counter+ " now waiting for " + wait + "ms");
            // InterruptedException needs to be re-thrown so Runnable can determine if thread was interrupted.
            // Once sleep() throws Interrupted Exception, "interrupted" flag of thread is cleared and Runnable
            // will never know it was interrupted.
            Thread.sleep(wait);
        }

        private void threadMessage(String message) {
            String name = Thread.currentThread().getName();
            System.out.printf("[%s]: %s%n", name, message);
        }
    }

    static class Worker implements LoggingRunnable {
        private SharedObject counter;

        public Worker(SharedObject counter) {
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
