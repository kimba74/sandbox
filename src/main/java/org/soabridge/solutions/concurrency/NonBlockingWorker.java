package org.soabridge.solutions.concurrency;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class NonBlockingWorker {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Worker worker = new Worker();
        String[] messages = {"message 1","message 2","message 3","message 4","message 5","message 6","message 7"};
        service.execute(worker);
        for (String msg : messages)
            worker.submit(msg);
        // Sleep to let the worker do it's job
        TimeUnit.SECONDS.sleep(3);
        // Terminate worker
        worker.terminate();
        // Shutting down Thread Pool
        service.shutdown();
    }

    public static class Worker implements Runnable {
        // Worker Status
        private boolean terminated = false;
        // Message Queue
        private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

        public void submit(String message) {
            if (!terminated && message != null)
                queue.add(message);
        }

        public void terminate() {
            if (!terminated) {
                System.out.println("INFO> The Worker was terminated.");
                terminated = true;
            }
        }

        @Override
        public void run() {

            try {
                while (!terminated) {
                    if (!queue.isEmpty())
                        System.out.printf("Message> %s%n", queue.poll());
                    else
                        TimeUnit.MILLISECONDS.sleep(1000);
                }
            }
            catch (InterruptedException e) {
                System.out.println("INFO> The Worker's Thread was interrupted.");
            }
            finally {
                System.out.println("INFO> The Worker has finished.");
                terminated = true;
            }
        }

    }
}
