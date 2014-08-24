package org.soabridge.reference.java5.concurrency;

import org.soabridge.reference.general.threads.LoggingRunnable;

import java.util.concurrent.Phaser;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class WorkerPhaser implements LoggingRunnable {

    private long patience;
    private Phaser phaser;
    private String name;

    public WorkerPhaser(String name, long patience, Phaser phaser) {
        this.name = name;
        this.patience = patience;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        phaser.register();
        try {
            for(int i=1; i<10; i++) {
                threadMessage(i + ". \"" + name + "\" Cycle");
                Thread.sleep(patience);
            }
            threadMessage("\"" + name + "\" Waiting for others to arrive");
            phaser.arriveAndAwaitAdvance();
            threadMessage("\"" + name + "\" Done!");
        }
        catch (InterruptedException e) {
            threadMessage("\"" + name + "\" Received Interrupt!");
        }
    }
}
