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
        ExecutorService threadpool = Executors.newSingleThreadExecutor();
        Worker worker = new Worker();

        threadpool.execute(worker);
        TimeUnit.MILLISECONDS.sleep(1500);

        worker.setMessage("Message 1");
        TimeUnit.MILLISECONDS.sleep(1500);

        worker.setMessage("Message 2");
        TimeUnit.MILLISECONDS.sleep(1500);

        worker.setMessage("Message 3");
        TimeUnit.MILLISECONDS.sleep(1500);

        worker.terminate();

        threadpool.shutdown();
    }


    public static class Worker implements Runnable {

        public enum Status {
            RUNNABLE,
            RUNNING,
            TERMINATING,
            SHUTDOWN
        }

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
            if (status == Status.RUNNING) {
                status = Status.TERMINATING;
                System.out.println("INFO> Worker was scheduled for termination!");
            }
        }

        @Override
        public void run() {
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
                status = Status.SHUTDOWN;
                System.out.println("INFO> Worker shut down!");
            }
        }

    }
}
