package org.soabridge.reference.general.threads;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("-- Interrupted Thread -----------------------------");
        // Create example thread
        Thread t2 = new Thread(new WorkerInterrupted("Repeat Message until interrupted"), "Worker");
        // Start example thread
        t2.start();
        // Wait for 10 seconds or until example thread is finished
        System.out.println("*** Main Thread waiting ***");
        t2.join(10000);
        // If example thread is still alive interrupt it
        if (t2.isAlive()) {
            System.out.println("*** Main Thread waited long enough ***");
            t2.interrupt();
        }
        else {
            System.out.println("*** Main Thread joined with utility thread ***");
        }

        // Wait until thread is really finished
        t2.join();

        System.out.println("-- Synchronized Method ----------------------------");
        // Create example shared object
        SharedSynchronized counter = new SharedSynchronized();
        // Create example threads
        Thread t3 = new Thread(new WorkerSynchronized(counter), "Worker1");
        Thread t4 = new Thread(new WorkerSynchronized(counter), "Worker2");
        // Start example threads
        t3.start();
        t4.start();
        // Wait until both example threads are finished
        t3.join();
        t4.join();


        System.out.println("-- Guarded Block ----------------------------------");
        // Create example shared object
        SharedGuarded drop = new SharedGuarded();
        // Create example threads
        Thread t5 = new Thread(new WorkerGuarded(WorkerGuarded.Type.CONSUMER, drop), "Worker1");
        Thread t6 = new Thread(new WorkerGuarded(WorkerGuarded.Type.PRODUCER, drop), "Worker2");
        // Start example threads
        t5.start();
        t6.start();
        // Wait until both example threads are finished
    }

}
