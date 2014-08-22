package org.soabridge.reference.general.threads;

import java.util.Random;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class WorkerLocked implements LoggingRunnable {

    private SharedLocked locked;

    public WorkerLocked(SharedLocked locked) {
        this.locked = locked;
    }

    @Override
    public void run() {
        Random rand = new Random(System.currentTimeMillis());
        for(int i=1; i<=10; i++) {
            try {
                if (rand.nextInt(100) % 2 > 0) {
                    threadMessage(i + ". Increasing Counter1");
                    locked.increase1();
                }
                else {
                    threadMessage(i + ". Increasing Counter2");
                    locked.increase2();
                }
            }
            catch (InterruptedException e) {
                threadMessage("Received Interrupt!");
                return;
            }
        }
        threadMessage("Done!");
    }
}
