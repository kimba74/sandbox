package org.soabridge.reference.java5.concurrency;

/**
 * @author <a href="steffen.krause@soabridge.com">Steffen Krause</a>
 * @since 1.0
 */
public class CallableSummation implements LoggingCallable<Integer> {

    private int[] items;

    public CallableSummation(int[] items) {
        this.items = items;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for(int num : items) {
            callerMessage("adding " + num + " to " + sum);
            sum = sum + num;
            Thread.sleep(500);
        }
        return sum;
    }
}
