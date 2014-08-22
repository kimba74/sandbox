package org.soabridge.reference.general.threads;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class SharedLocked {

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
