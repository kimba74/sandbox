package org.soabridge.solutions.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class StatusWorker {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        Worker worker = new Worker();

        pool.execute(worker);
        TimeUnit.MILLISECONDS.sleep(1500);

        worker.setMessage("Message 1");
        TimeUnit.MILLISECONDS.sleep(1500);

        worker.setMessage("Message 2");
        TimeUnit.MILLISECONDS.sleep(1500);

        worker.setMessage("Message 3");
        TimeUnit.MILLISECONDS.sleep(1500);

        worker.terminate();

        pool.shutdown();
    }


    public static class Worker implements Runnable {

        public enum Status {
            RUNNABLE,    // Status after the Worker was created
            RUNNING,     // Status when the Worker has started executing
            TERMINATING, // Status after the Worker has been scheduled to terminate
            SHUTDOWN     // Status after the Worker actually finished
        }

        // The initial status of all workers is RUNNABLE
        private Status status = Status.RUNNABLE;
        private String message = "<NO MESSAGE>";

        public Status getStatus() {
            return status;
        }

        public void setMessage(String message) {
            if (message != null && (status == Status.RUNNABLE || status == Status.RUNNING))
                this.message = message;
        }

        public void terminate() {
            // Worker can only be terminated if it is running
            if (status == Status.RUNNING) {
                status = Status.TERMINATING;
                System.out.println("INFO> Worker was scheduled for termination!");
            }
        }

        @Override
        public void run() {
            // Worker can only be run if it is runnable
            if (status == Status.RUNNABLE)
                status = Status.RUNNING;
            try {
                while (status == Status.RUNNING) {
                    System.out.printf("MESSAGE> %s%n", message);
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            }
            catch (InterruptedException e) {
                System.out.println("INFO> Worker Thread was interrupted");
            }
            finally {
                // Worker will be set to SHUTDOWN right before it exits the run() method
                status = Status.SHUTDOWN;
                System.out.println("INFO> Worker shut down!");
            }
        }

    }
}
