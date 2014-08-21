package org.soabridge.reference.general.threads;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class WorkerInterrupted implements Runnable {

    private String name = Thread.currentThread().getName();
    private String message;

    public WorkerInterrupted(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        for(int i=0; i<100; i++) {
            try {
                threadMessage((i+1) + ". " + message);
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                threadMessage("Thread got interrupted");
                return;
            }
        }
    }

    private void threadMessage(String message) {
        String name = Thread.currentThread().getName();
        System.out.printf("[%s]: %s%n", name, message);
    }
}
