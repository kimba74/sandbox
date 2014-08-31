package org.soabridge.reference.java5.concurrency;

import org.soabridge.reference.general.threads.LoggingRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleLock {

    static long patience = 60;
    static TimeUnit patienceUnit = TimeUnit.SECONDS;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Friend alfred = new Friend("Alfred");
        Friend gaston = new Friend("Gaston");
        service.execute(new Worker(alfred, gaston));
        service.execute(new Worker(gaston, alfred));
        // Telling ExecutorService not to accept new tasks and shutdown after last task finishes
        service.shutdown();
        // Wait for a maximum of 60s for ExecutorService to finish execution
        if (!service.awaitTermination(patience, patienceUnit)) {
            System.out.println("*** ExecutorService didn't finish after a " + patience + " " + patienceUnit + " wait ***");
            // Forcing shutdown of ExecutorService
            service.shutdownNow();
        }
    }

    static class Friend {
        private String name;
        private ReentrantLock lock = new ReentrantLock();

        Friend(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        private boolean impendingBow(Friend bower) {
            boolean myLock = false;
            boolean otherLock = false;
            try {
                myLock = lock.tryLock();
                otherLock = bower.lock.tryLock();
            }
            finally {
                if (!(myLock && otherLock)) {
                    if (myLock)
                        lock.unlock();
                    if (otherLock)
                        bower.lock.unlock();
                }
            }
            return myLock && otherLock;
        }

        public void bow(Friend bower) {
            if(impendingBow(bower)) {
                try {
                    printMessage(bower.getName() + " has bowed to me.");
                    bower.bowBack(this);
                }
                finally {
                    lock.unlock();
                    bower.lock.unlock();
                }
            }
            else {
                printMessage(bower.getName() + " started to bow to me but saw, that I was already bowing to him.");
            }
        }

        public void bowBack(Friend bower) {
            printMessage(bower.getName() + " has bowed back to me!");
        }

        private void printMessage(String message) {
            String thread = Thread.currentThread().getName();
            System.out.printf("[%s] %s: %s%n", thread, this.getName(), message);
        }
    }

    static class Worker implements LoggingRunnable {
        private Friend bower;
        private Friend bowee;

        Worker(Friend bower, Friend bowee) {
            this.bower = bower;
            this.bowee = bowee;
        }

        @Override
        public void run() {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            try {
                for (int i=0; i<10; i++) {
                    TimeUnit.SECONDS.sleep(random.nextInt(5));
                    bower.bow(bowee);
                }
            }
            catch (InterruptedException e) {
                threadMessage("Received Interrupt!");
            }
        }
    }
}
