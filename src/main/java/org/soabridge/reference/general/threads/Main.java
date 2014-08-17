package org.soabridge.reference.general.threads;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Worker worker = new Worker();
        Thread t1 = new Thread(worker);
        t1.start();

        Thread.sleep(1000);

        System.out.println("*** Main Thread setting first message ***");
        worker.print("Hello World!");

        Thread.sleep(1000);

        // Main Thread should get stopped at method print() invocation until Worker Thread is done
        System.out.println("*** Main Thread setting second message ***");
        worker.print("Goodbye World!");

        Thread.sleep(1000);

        // Main Thread should get stopped at method terminate() invocation until Worker Thread is done
        System.out.println("*** Main Thread terminating Worker Thread ***");
        worker.terminate();
    }

}
