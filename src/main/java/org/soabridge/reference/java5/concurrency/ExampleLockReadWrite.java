package org.soabridge.reference.java5.concurrency;

import org.soabridge.reference.general.concurrency.LoggingRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleLockReadWrite {

    static final long patience = 60;
    static final TimeUnit patienceUnit = TimeUnit.SECONDS;

    public static void main(String[] args) throws InterruptedException {
        SharedObject object = new SharedObject();
        ExecutorService service = Executors.newFixedThreadPool(3);
        service.execute(new Worker(Worker.Type.WRITER, object));
        service.execute(new Worker(Worker.Type.READER, object));
        service.execute(new Worker(Worker.Type.READER, object));
        // Telling ExecutorService not to accept new tasks and shutdown after last task finishes
        service.shutdown();
        // Wait for a maximum of 60s for ExecutorService to finish execution
        if (!service.awaitTermination(patience, patienceUnit)) {
            System.out.println("*** ExecutorService didn't finish after a " + patience + " " + patienceUnit + " wait ***");
            // Forcing shutdown of ExecutorService
            service.shutdownNow();
        }
    }

    static class SharedObject {
        private String message;
        // The ReentrantReadWrite lock offers two sub-locks, one for read operations the other for write operations.
        // The Read-only lock may be held by as many concurrent threads as available. It will not lock unless a
        // Write-Lock is acquired by another thread.
        private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        public void writeMessage(String message) throws InterruptedException {
            lock.writeLock().lock();
            try {
                TimeUnit.SECONDS.sleep(5);
                this.message = message;
            }
            finally {
                lock.writeLock().unlock();
            }
        }

        public String readMessage() throws InterruptedException {
            // Define boolean to hold lock decision for later evaluation
            boolean myLock = false;
            // Try to acquire lock. This method will not pause if the lock is not available but will return 'false'.
            // In case the lock is available, the method will acquire it and then return 'true'
            myLock = lock.readLock().tryLock();
            // It is good practice to put the block following the acquisition of a lock in a try/finally block and
            // put the returning of the lock into the 'final' clause, that way it can be assured that the lock will
            // be returned even if the logic being guarded by a lock throws an exception.
            try {
                if (myLock) {
                    TimeUnit.SECONDS.sleep(1);
                    return this.message;
                }
                else {
                    TimeUnit.SECONDS.sleep(1);
                    return "Could not get lock";
                }
            }
            finally {
                // Check if the lock was successfully acquired before trying to return it. An exception will be
                // thrown when trying to return a lock that is not owned by the current thread.
                if (myLock) lock.readLock().unlock();
            }
        }
    }

    static class Worker implements LoggingRunnable {
        private SharedObject shared;
        private Type type;

        public enum Type {
            READER,
            WRITER
        }

        Worker(Type type, SharedObject shared) {
            this.type = type;
            this.shared = shared;
        }

        @Override
        public void run() {
            try {
                switch(type) {
                    case READER:
                        read();
                        break;
                    case WRITER:
                        write();
                        break;
                }
            }
            catch (InterruptedException e) {
                threadMessage("Received Interrupt!");
            }
        }

        private void read() throws InterruptedException {
            for (int i=1; i<=10; i++) {
                threadMessage(i + ". Read message: " + shared.readMessage());
                TimeUnit.SECONDS.sleep(1);
            }
        }

        private void write() throws InterruptedException {
            String[] message = {"OneTwo was a racing horse", "TwoOne was one, too",
                                "OneTwo won one race", "TwoOne won one, too."};
            for (String msg :  message) {
                threadMessage("Writing message \"" + msg + "\"");
                shared.writeMessage(msg);
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }
}
