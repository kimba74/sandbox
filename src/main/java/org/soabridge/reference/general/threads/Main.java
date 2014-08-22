package org.soabridge.reference.general.threads;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("-- Interrupted Thread -----------------------------");
        // Create example thread
        Thread t1 = new Thread(new WorkerInterrupted("Repeat Message until interrupted"), "Interrupted");
        // Start example thread
        t1.start();
        // Wait for 10 seconds or until example thread is finished
        System.out.println("*** Main Thread waiting ***");
        t1.join(10000);
        // If example thread is still alive interrupt it
        if (t1.isAlive()) {
            System.out.println("*** Main Thread waited long enough ***");
            t1.interrupt();
        }
        else {
            System.out.println("*** Main Thread joined with utility thread ***");
        }

        // Wait until thread is really finished
        t1.join();

        System.out.println("-- Synchronized Method ----------------------------");
        // Create example shared object
        SharedSynchronized counter = new SharedSynchronized();
        // Create example threads
        Thread t2 = new Thread(new WorkerSynchronized(counter), "Synchronized1");
        Thread t3 = new Thread(new WorkerSynchronized(counter), "Synchronized2");
        // Start example threads
        t2.start();
        t3.start();
        // Wait until both example threads are finished
        t2.join();
        t3.join();


        System.out.println("-- Guarded Block ----------------------------------");
        // Create example shared object
        SharedGuarded drop = new SharedGuarded();
        // Create example threads
        Thread t4 = new Thread(new WorkerGuarded(WorkerGuarded.Type.CONSUMER, drop), "Guarded1");
        Thread t5 = new Thread(new WorkerGuarded(WorkerGuarded.Type.PRODUCER, drop), "Guqrded2");
        // Start example threads
        t4.start();
        t5.start();
        // Wait until both example threads are finished
        t4.join();
        t5.join();

        System.out.println("-- Intrinsic Lock ---------------------------------");
        // Create example shared object
        SharedLocked locked = new SharedLocked();
        // Create example threads
        Thread t6 = new Thread(new WorkerLocked(locked), "Locked1");
        Thread t7 = new Thread(new WorkerLocked(locked), "Locked2");
        // Start example threads
        t6.start();
        t7.start();
        // Wait until both example threads are finished
        t6.join();
        t7.join();
    }

}
