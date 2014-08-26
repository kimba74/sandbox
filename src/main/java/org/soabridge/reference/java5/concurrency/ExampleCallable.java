package org.soabridge.reference.java5.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleCallable {
    static final long patience = 60;
    static final TimeUnit patienceUnit = TimeUnit.SECONDS;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("-- Callable Example -------------------------------");
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future1 = service.submit(new Worker(new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20}));
        try {
            System.out.printf("*** Summation result: %d ***%n", future1.get(patience, patienceUnit));
        }
        catch (TimeoutException e) {
            System.out.println("*** Summation did not finish withing " + patience + " " + patienceUnit + ", terminating ***");
            future1.cancel(true);
        }
        // Telling ExecutorService not to accept new tasks and shutdown after last task finishes
        service.shutdown();
        // Wait for a maximum of 60s for ExecutorService to finish execution
        if (!service.awaitTermination(patience, patienceUnit)) {
            System.out.println("*** ExecutorService didn't finish after a " + patience + " " + patienceUnit + " wait ***");
            // Forcing shutdown of ExecutorService
            service.shutdownNow();
        }
    }

    static class Worker implements LoggingCallable<Integer> {
        private int[] items;

        public Worker(int[] items) {
            this.items = items;
        }

        @Override
        public Integer call() throws Exception {
            int sum = 0;
            for (int num : items) {
                callerMessage("adding " + num + " to " + sum);
                sum = sum + num;
                Thread.sleep(500);
            }
            return sum;
        }
    }
}
