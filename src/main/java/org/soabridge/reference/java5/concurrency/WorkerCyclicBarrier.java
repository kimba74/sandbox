package org.soabridge.reference.java5.concurrency;

import org.soabridge.reference.general.threads.LoggingRunnable;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class WorkerCyclicBarrier implements LoggingRunnable {

    private long patience;
    private CyclicBarrier barrier;

    public WorkerCyclicBarrier(long patience, CyclicBarrier barrier) {
        this.patience = patience;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            for(int i=1; i<=10; i++) {
                threadMessage(i + ". cycle, now waiting " + patience + "ms");
                Thread.sleep(patience);
            }
            threadMessage("Waiting for others to arrive");
            barrier.await();
            threadMessage("Done!");
        }
        catch (InterruptedException e) {
            threadMessage("Received Interrupt!");
        }
        catch (BrokenBarrierException e) {
            threadMessage("Barrier was broken!");
        }
    }
}
