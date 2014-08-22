package org.soabridge.reference.general.threads;

import java.util.Random;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class SharedCounter {

    private int counter = 0;
    private Random rand = new Random(System.currentTimeMillis());

    public synchronized void decrease() throws InterruptedException {
        counter--;
        int wait = rand.nextInt(5000);
        threadMessage("Decreasing value to " + counter + " now waiting for " + wait + "ms");
        // InterruptedException needs to be re-thrown so Runnable can determine if thread was interrupted.
        // Once sleep() throws Interrupted Exception, "interrupted" flag of thread is cleared and Runnable
        // will never know it was interrupted.
        Thread.sleep(wait);
    }

    public synchronized void increase() throws InterruptedException {
        counter++;
        int wait = rand.nextInt(5000);
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
