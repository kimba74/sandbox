package org.soabridge.reference.general.threads;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class SharedGuarded {

    private boolean empty = true;
    private String message;
    private int deposit_cntr = 0;
    private int pickup_cntr = 0;

    public synchronized void deposit(String message) throws InterruptedException {
        deposit_cntr++;
        threadMessage("Thread entered " + deposit_cntr + ". deposit procedure [" + message + "]");
        while (!empty) {
            wait();
        }
        this.message = message;
        this.empty = false;
        notifyAll();
        threadMessage("Thread commenced " + deposit_cntr + ". deposit procedure");
    }

    public synchronized String pickup() throws InterruptedException {
        pickup_cntr++;
        threadMessage("Thread entered " + pickup_cntr + ". pickup procedure");
        while (empty) {
            wait();
        }
        empty = true;
        notifyAll();
        threadMessage("Thread commenced " + pickup_cntr + ". pickup procedure [" + message + "]");
        return message;
    }

    private void threadMessage(String message) {
        String name = Thread.currentThread().getName();
        System.out.printf("[%s]: %s%n", name, message);
    }
}
