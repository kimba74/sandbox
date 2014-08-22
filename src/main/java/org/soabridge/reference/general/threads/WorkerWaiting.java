package org.soabridge.reference.general.threads;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class WorkerWaiting implements LoggingRunnable {

    private boolean run;
    private String message;

    @Override
    public void run() {
        run = true;
        try {
            while (run) {
                synchronized (this) {
                    while(message == null && run) {
                        threadMessage("Thread waiting for message");
                        this.wait();
                    }
                    if (run) {
                        threadMessage("Thread printing message");
                        for (int i = 0; i < 15; i++) {
                            threadMessage((i+1) + ". " + message);
                            Thread.sleep(1000);
                        }
                        message = null;
                    }
                }
            }
        }
        catch (InterruptedException e) {
            threadMessage("Thread was interrupted");
        }
    }

    public void print(String message) {
        synchronized (this) {
            threadMessage("Thread received message \"" + message + "\"");
            this.message = message;
            this.notify();
        }
    }

    public void terminate() {
        synchronized (this) {
            threadMessage("Thread will be terminated");
            this.run = false;
            this.notify();
        }
    }

}
