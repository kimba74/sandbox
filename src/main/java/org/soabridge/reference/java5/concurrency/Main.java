package org.soabridge.reference.java5.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int patience = 60;
        TimeUnit patienceUnit = TimeUnit.SECONDS;

        System.out.println("-- Single Thread Executor -------------------------");
        ExecutorService service1 = Executors.newSingleThreadExecutor();
        service1.execute(new WorkerExecutor("Single1"));
        service1.execute(new WorkerExecutor("Single2"));
        service1.execute(new WorkerExecutor("Single3"));
        // Telling ExecutorService not to accept new tasks and shutdown after last task finishes
        service1.shutdown();
        // Wait for a maximum of 60s for ExecutorService to finish execution
        if (!service1.awaitTermination(patience, patienceUnit)) {
            System.out.println("*** ExecutorService didn't finish after a " + patience + " " + patienceUnit + " wait ***");
            // Forcing shutdown of ExecutorService
            service1.shutdownNow();
        }

        System.out.println("-- Cached Thread Pool -----------------------------");
        ExecutorService service2 = Executors.newCachedThreadPool();
        service2.execute(new WorkerExecutor("Cached1"));
        service2.execute(new WorkerExecutor("Cached2"));
        service2.execute(new WorkerExecutor("Cached3"));
        // Telling ExecutorService not to accept new tasks and shutdown after last task finishes
        service2.shutdown();
        // Wait for a maximum of 60s for ExecutorService to finish execution
        if (!service2.awaitTermination(patience, patienceUnit)) {
            System.out.println("*** ExecutorService didn't finish after a " + patience + " " + patienceUnit + " wait ***");
            // Forcing shutdown of ExecutorService
            service2.shutdownNow();
        }

        System.out.println("-- Cached Thread Pool -----------------------------");
        ExecutorService service3 = Executors.newFixedThreadPool(2);
        service3.execute(new WorkerExecutor("Fixed1"));
        service3.execute(new WorkerExecutor("Fixed2"));
        service3.execute(new WorkerExecutor("Fixed3"));
        // Telling ExecutorService not to accept new tasks and shutdown after last task finishes
        service3.shutdown();
        // Wait for a maximum of 60s for ExecutorService to finish execution
        if (!service3.awaitTermination(patience, patienceUnit)) {
            System.out.println("*** ExecutorService didn't finish after a " + patience + " " + patienceUnit + " wait ***");
            // Forcing shutdown of ExecutorService
            service3.shutdownNow();
        }
    }

}
