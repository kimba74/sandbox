package org.soabridge.reference.java5.concurrency;

import org.soabridge.reference.general.concurrency.LoggingRunnable;

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

    static final long patience = 60;
    static final TimeUnit patienceUnit = TimeUnit.SECONDS;

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
            // Lock acquisition stores for own and for other lock
            boolean myLock = false;
            boolean otherLock = false;
            try {
                // Try to acquire own lock and save decision in acquisition store variable
                myLock = lock.tryLock();
                // Try to acquire other lock and save decision in acquisition store variable
                otherLock = bower.lock.tryLock();
            }
            finally {
                // If either only one or no lock at all could be acquired release
                // lock that was acquired (if any)
                if (!(myLock && otherLock)) {
                    // Check if own lock was acquired and if so release it to let
                    // another the other thread acquire it.
                    if (myLock)
                        lock.unlock();
                    // Check if other lock was acquired and if so release it to let
                    // another the other thread acquire it.
                    if (otherLock)
                        bower.lock.unlock();
                }
            }
            // Return status of acquired locks
            return myLock && otherLock;
        }

        public void bow(Friend bower) {
            if(impendingBow(bower)) {
                try {
                    printMessage(bower.getName() + " has bowed to me.");
                    bower.bowBack(this);
                }
                // Always release locks within a 'finally' clause to ensure the locks are
                // released otherwise an exception could render a shared object locked forever.
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
