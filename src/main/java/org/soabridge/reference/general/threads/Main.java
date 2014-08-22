package org.soabridge.reference.general.threads;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        WorkerWaiting workerWaiting = new WorkerWaiting();
        Thread t1 = new Thread(workerWaiting, "Waiter");
        t1.start();

        Thread.sleep(1000);

        System.out.println("*** Main Thread setting first message ***");
        workerWaiting.print("Hello World!");

        Thread.sleep(1000);

        // Main Thread should get stopped at method print() invocation until Worker Thread is done
        System.out.println("*** Main Thread setting second message ***");
        workerWaiting.print("Goodbye World!");

        Thread.sleep(1000);

        // Main Thread should get stopped at method terminate() invocation until Worker Thread is done
        System.out.println("*** Main Thread terminating Worker Thread ***");
        workerWaiting.terminate();

        System.out.println("---------------------------------------------");

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
    }

}
