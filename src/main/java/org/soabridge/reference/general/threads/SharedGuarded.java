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
        threadMessage(deposit_cntr + ". Thread entered deposit procedure [" + message + "]");
        while (!empty) {
            wait();
        }
        this.message = message;
        this.empty = false;
        notifyAll();
        threadMessage(deposit_cntr + ". Thread commenced deposit procedure");
    }

    public synchronized String pickup() throws InterruptedException {
        pickup_cntr++;
        threadMessage(pickup_cntr + ". Thread entered pickup procedure");
        while (empty) {
            wait();
        }
        empty = true;
        notifyAll();
        threadMessage(pickup_cntr + ". Thread commenced pickup procedure [" + message + "]");
        return message;
    }

    private void threadMessage(String message) {
        String name = Thread.currentThread().getName();
        System.out.printf("[%s]: %s%n", name, message);
    }
}
