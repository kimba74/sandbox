package org.soabridge.reference.general.threads;

import java.util.Random;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleGuarded {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-- Guarded Block ----------------------------------");
        // Create example shared object
        SharedObject drop = new SharedObject();
        // Create example threads
        Thread thread1 = new Thread(new Worker(Worker.Type.CONSUMER, drop), "Guarded1");
        Thread thread2 = new Thread(new Worker(Worker.Type.PRODUCER, drop), "Guarded2");
        // Start example threads
        thread1.start();
        thread2.start();
        // Wait until both example threads are finished
        thread1.join();
        thread2.join();
    }

    static class SharedObject {
        private boolean empty = true;
        private String message;
        private int deposit_cntr = 0;
        private int pickup_cntr = 0;

        public synchronized void deposit(String message) throws InterruptedException {
            deposit_cntr++;
            threadMessage(deposit_cntr + ". Thread entered deposit procedure [" + message + "]");
            while (!empty) {
                wait();
            }
            this.message = message;
            this.empty = false;
            notifyAll();
            threadMessage(deposit_cntr + ". Thread commenced deposit procedure");
        }

        public synchronized String pickup() throws InterruptedException {
            pickup_cntr++;
            threadMessage(pickup_cntr + ". Thread entered pickup procedure");
            while (empty) {
                wait();
            }
            empty = true;
            notifyAll();
            threadMessage(pickup_cntr + ". Thread commenced pickup procedure [" + message + "]");
            return message;
        }

        private void threadMessage(String message) {
            String name = Thread.currentThread().getName();
            System.out.printf("[%s]: %s%n", name, message);
        }
    }

    static class Worker implements LoggingRunnable {
        public static enum Type {
            CONSUMER,
            PRODUCER
        }

        private Type type;
        private SharedObject drop;

        public Worker(Type type, SharedObject drop) {
            this.type = type;
            this.drop = drop;
        }

        @Override
        public void run() {
            try {
                switch (type) {
                    case CONSUMER:
                        consume();
                        break;
                    case PRODUCER:
                        produce();
                        break;
                }
            } catch (InterruptedException e) {
                threadMessage("Thread was interrupted!");
            }
        }

        private void produce() throws InterruptedException {
            int rest;
            Random rand = new Random(System.currentTimeMillis());
            String[] message = {"OneTwo was a racing horse",
                    "TwoOne was one, too",
                    "OneTwo won one race",
                    "TwoOne won one, too"};
            for (String msg : message) {
                drop.deposit(msg);
                rest = rand.nextInt(5000);
                threadMessage(type.toString() + " resting for " + rest + "ms");
                Thread.sleep(rest);
            }
            drop.deposit("DONE");
        }

        private void consume() throws InterruptedException {
            int rest;
            Random rand = new Random(System.currentTimeMillis());
            String message = "";
            while (!message.equals("DONE")) {
                message = drop.pickup();
                rest = rand.nextInt(5000);
                threadMessage(type.toString() + " resting for " + rest + "ms");
                Thread.sleep(rest);
            }
        }
    }
}
