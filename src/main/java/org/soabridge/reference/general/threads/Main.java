package org.soabridge.reference.general.threads;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        WorkerInterrupted workerInterrupted = new WorkerInterrupted("Hello World");
        Thread t2 = new Thread(workerInterrupted, "Interrupter");
        t2.start();
        System.out.println("*** Main Thread waiting ***");
        t2.join(10000);
        if (t2.isAlive()) {
            System.out.println("*** Main Thread waited long enough ***");
            t2.interrupt();
        }
        else {
            System.out.println("*** Main Thread joined with utility thread ***");
        }

        System.out.println("---------------------------------------------");

        SharedSynchronized counter = new SharedSynchronized();
        Thread t3 = new Thread(new WorkerSynchronized(counter), "Worker1");
        Thread t4 = new Thread(new WorkerSynchronized(counter), "Worker2");

        t3.start();
        t4.start();

        try {
            Thread.sleep(30000);
        }
        catch (InterruptedException e) { }

        if(t3.isAlive()) {
            System.out.printf("--- Interrupting Thread %s ---%n", t3.getName());
            t3.interrupt();
        }

        if(t4.isAlive()) {
            System.out.printf("--- Interrupting Thread %s ---%n", t4.getName());
            t4.interrupt();
        }

        System.out.println("---------------------------------------------");

        SharedGuarded drop = new SharedGuarded();
        Thread t5 = new Thread(new WorkerGuarded(WorkerGuarded.Type.CONSUMER, drop), "Worker1");
        Thread t6 = new Thread(new WorkerGuarded(WorkerGuarded.Type.PRODUCER, drop), "Worker2");

        t5.start();
        t6.start();
    }

}
