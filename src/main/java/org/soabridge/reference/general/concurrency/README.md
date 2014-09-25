Java Concurrency Examples
=========================

## Guarded Shared Object
This example demonstrates simple, mutual exclusive locking mechanism that establishes a Producer/Consumer
pattern on a Shared Object. To accomplish this pattern, the example uses the `wait()` and `notify()`/`notifyAll()`
mechanism provided by Java to halt a thread allowing the other party to do their job and to notify the halted
Thread when the own work is done. The example also demonstrates a state check of the Shared Object to ensure,
that the notified Thread has a valid state to work with.

[Example](ExampleGuarded.java)

## Interrupted Thread
The purpose of this example is to demonstrate how to interrupt a Thread, how to handle an interrupt signal within
the `Runnable`, and to experiment with the _interrupted status_ of the Thread Object itself.

[Example](ExampleInterrupted.java)

## Intrinsic Locks
_Intrinsic Locks_ are objects that protect sections in the code of Shared Objects that are surrounded by
`synchronized()` blocks. This approach allows for a finer locking granularity than the _Synchronized Methods_
by providing multiple, user-defined locks per Shared Object. As with _synchronized Methods_ _Intrinsic Locks_
are so called _Reentrant Locks_ meaning that once a Thread has acquired and currently owns it, the Thread can
"reacquire" it either directly or indirectly when encountering another _synchronized_ block locked by the same
_Intrinsic Lock_.

[Example](ExampleLocked.java)

## Synchronized Methods
_Synchronized Methods_ are the simplest form of controlling access to a Shared Object in a concurrent Thread
environment. They provide a single, implicit lock to a Shared Object and allow access to the _synchronized_
methods only to one Thread at the time. Other than with the _Intrinsic Locks_ no block has to be manually
specified by the developer protected methods have to be marked with the `synchronized` keyword. The owning
Thread of the lock will return it the moment it leaves the synchronized method thus permitting access to all
other synchronized to the Thread next in line.

```java
public synchronized void decrease() throws InterruptedException {
   ...
   Thread.sleep(wait);
}
```  
[Example](ExampleSynchronized.java)