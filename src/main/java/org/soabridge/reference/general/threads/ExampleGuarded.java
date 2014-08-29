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
        SharedGuarded drop = new SharedGuarded();
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

    static class Worker implements LoggingRunnable {
        public static enum Type {
            CONSUMER,
            PRODUCER
        }

        private Type type;
        private SharedGuarded drop;

        public Worker(Type type, SharedGuarded drop) {
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
