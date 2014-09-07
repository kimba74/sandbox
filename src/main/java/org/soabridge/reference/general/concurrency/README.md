Java Concurrency Examples
=========================

## Guarded Shared Object
This example demonstrates simple, mutual exclusive locking mechanism that establishes a Producer/Consumer
pattern on a Shared Object. To accomplish this pattern, the example uses the `wait()` and `notify()` / `notifyAll()`
mechanism provided by Java to halt a thread allowing the other party to do their job and to notify the halted
Thread when the own work is done. The example also demonstrates a state check of the Shared Object to ensure,
that the notified Thread has a valid state to work with.

## Interrupted Thread
The purpose of this example is to demonstrate how to interrupt a Thread, how to handle an interrupt signal within
the `Runnable`, and to experiment with the _interrupted status_ of the Thread Object itself.

## Intrinsic Locks

## Synchronized Methods