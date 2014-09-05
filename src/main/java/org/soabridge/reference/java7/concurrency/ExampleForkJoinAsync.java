package org.soabridge.reference.java7.concurrency;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class ExampleForkJoinAsync {


    public static void main(String[] args) {
        System.out.println("-- ForkJoin Async ---------------------------------");
        int[] items = new int[40];
        // Create items list
        for (int i=0; i<40; i++)
            items[i] = i;
        // Create Thread Pool with max. 10 parallel Threads
        ForkJoinPool pool = new ForkJoinPool(10);
        // Create Task to be executed (Needed to check when the task is done)
        Task task = new Task(items);
        // Execute Action and continue with this thread
        pool.execute(task);
        System.out.println("*** Continuing with Main Thread ***");
        // Join task. Wait until the task is finished before proceeding
        task.join();
        System.out.println("*** All Done ***");
    }

    static class Task extends RecursiveAction {

        private int[] items;
        private String name;

        Task(int[] items) {
            this(items, "Action-1");
        }

        Task(int[] items, String name) {
            this.items = items;
            this.name = name;
        }

        private void print(int[] localitems) {
            try {
                for (int item : localitems) {
                    actionMessage(name + " - Item " + item);
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            }
            catch (InterruptedException e) {
                actionMessage("Received Interrupt");
            }
        }

        @Override
        protected void compute() {
            // If the array length is less or equal 10, do it yourself
            if (items.length <= 10) {
                print(items);
            }
            // Otherwise split in half and spin of actions
            else {
                int half = items.length / 2;
                invokeAll(new Task(Arrays.copyOfRange(items, 0, half), name + ".1"),
                        new Task(Arrays.copyOfRange(items, half, items.length), name + ".2"));
            }
        }

        private void actionMessage(String message) {
            String name = Thread.currentThread().getName();
            System.out.printf("[%s]: %s%n", name, message);
        }

    }
}
