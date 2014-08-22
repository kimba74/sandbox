package org.soabridge.reference.general.threads;

import java.util.Random;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class WorkerGuarded implements LoggingRunnable {

    public static enum Type {
        CONSUMER,
        PRODUCER
    }

    private Type type;
    private SharedGuarded drop;

    public WorkerGuarded(Type type, SharedGuarded drop) {
        this.type = type;
        this.drop = drop;
    }

    @Override
    public void run() {
        try {
            switch(type){
                case CONSUMER:
                    consume();
                    break;
                case PRODUCER:
                    produce();
                    break;
            }
        }
        catch (InterruptedException e) {
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
