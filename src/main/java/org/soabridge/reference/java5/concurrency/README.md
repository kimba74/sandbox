Java5 Concurrency Examples
==========================

## Executors and Threadpools
Before Java5 running Threads involved either the implementation of a `Runnable`, which was then passed to the instance
of a `Thread` object or the extension of the `Thread` class itself. The runtime had to be managed manually by storing
the `Thread` object in a central location and monitoring it's life-cycle. Threadpools had to be manually designed
and implemented. `Executor` and its sub- and supporting classes take a load of work off the developer's hands. Instead
of passing a `Runnable` implementation to a `Thread` object amd managing it manually one can now just pass it to
the instance of a `ExecutorService`.  
Different implementations of an `ExecutorService` can be obtained via the static factory methods of the `Executors`
class. Should none of the provided implementations meet ones needs, a custom ExecutorService can be implemented via
the provided interfaces and abstract classes.

[Example](ExampleExecutor.java)

## Callables
This is a new for of `Runnable` that is capable of returning values once it finishes. A regular `Runnable` Object
terminates the Thread it is running in once its `run()` method returns. Passing values back to the initiating Thread
can only be done via a previously passed in shared object. To make things simpler the `Callable` interface was 
introduced into Java5. An implementation of this new interface needs to be passed to an `ExecutorService` by calling
its `submit()` method (vs. calling its `execute()` method). The `submit()` method will return a `Future` object that
is type-cast to the exact same object the submitted `Callable` implementation is. Once the `Callable` terminates by
successfully exiting its `call()` method, the return valuable can be retrieved from the `Future<>` object by invoking
one of its `get()` methods.
  
Example of a `Callable` implementation:  
```java
    public class FactorialWorker implements Callable<Integer> {
        private int maximum;

        public FactorialWorker(int maximum) {
            this.maximum = maximum;
        }
        
        public Integer call() {
            int factorial = 0;
            for(int i=0; i<= maximum; i++) {
                factorial = factorial * i;
            }
            return factorial;
        }
    }
```  
  
To execute a `Callable` and retrieve its returned value it needs to be handed to the `ExecutorService` via the
`submit()` rather than the `execute()` method. The `submit()` method will return a `Future` object which can be used to
control the `Callable` runtime behavior and retrieve the returned value once it finishes.
  
Example of submitting a `Callable` for execution and the retrieval of the return value:  
```java
    ExecutorService service = Executors.newSingleThreadExecutor();
    Future<Integer> future = service.submit(new FactorialWorker(50));
    Integer result = null;
    try {
        result = future.get(60, TimeUnit.SECONDS);
    }
    catch (TimeoutException e) {
        future.cancel(true);
    }
```  
The `get()` method of the `Future` object can take either no arguments at all or a timeout combination consisting of the the amount and the time unit type. Should the set timeout expire before a result was received this method will throw a `TimeoutException` indicating that it took longer than expected to receive the return value. The method without any parameters will wait until the return value was received.
  
[Example](ExampleCallable.java)

## Cyclic Barriers
[Example](ExampleCyclicBarrier.java)

## Enhanced Intrinsic Locks
The new locking mechanism for intrinsic locks addressed a problem in which a Thread could not make its intention known
to call a synchronized block in a shared object owned by another `Thread`, thus potentially leading leading to a
dead-lock if the other Thread tried to access a shared object owned by the first Thread (bower/bowee example in Java
tutorial. With the new `ReentrantLock` a Thread can pre-acquire lock and only execute if it possesses all involved locks
or back off it not all locks could be acquired. 

The new `ReentrantLock` is easily instantiated in the shared object with the following line:  
```java
    private ReentrantLock lock = new ReentrantLock();
```  
  
The following example demonstrates how to protect a block of code with the lock object:  
```java
    boolean myLock = false;
    try {
        myLock = lock.tryLock();
    }
    finally {
        if (myLock)
            lock.unlock();
        }
    }
```

[Example](ExampleLock.java)

## Read-Write Locks
A special form of the new locks are the Read-Write locks. This lock consists out of two individual locks that are joined
together in a higher level lock object. The two locks are a Read- and a Write lock. When synchronizing blocks the
developer has the option to choose if the operation is read-only or if it has some write elements embedded in it. The
Read lock can be acquired by as many Threads as the developer chooses as long as there is no Write-lock imposed. Only
one Thread can own a Write-lock at a time. While owning a Write-lock no Read-lock can be acquired and therefore no read
operation can be performed. A Write-lock can be downgraded to a Read-Lock to allow waiting Threads to proceed.

A simple Read-Write Lock is instantiated in the shared resource with the following line:  
  
```java
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
```  
  
To protect the code with this Read-Write lock no `synchronized()` block is necessary. Instead the following mechanism
is recommended:

For protecting code with a _Write_ lock:  
```java
    lock.writeLock().lock();
    try {
        // Protected code goes here
    }
    finally {
        lock.writeLock().unlock();
    }
```
  
For protecting code with a _Read_ lock:  
```java
    boolean myLock = lock.readLock().tryLock();
    try {
        if (myLock) {
            // Protected code goes here
        }
        else {
            // Code to be executed if a lock could
            // not be acquired goes here
        }
    }
    finally {
        if (myLock) lock.readLock().unlock();
    }
```  
  
It is important to notice that other than the `lock()` method the `tryLock()` method will not halt if a lock could not
be acquired. Instead this method will return a boolean value indicating if the lock was successfully acquired or not.
This method is preferable if other code than the protected block should be executed. When using the `tryLock()` method
it is important to test if the lock acquisition was successful before releasing the lock with the `unlock()` method.
An exception is thrown when a Thread tries to return a lock it doesn't have.  
  
Both snippets, the one demonstrating the _Read_ and the other the _Write_ lock wrap the code execution in a try-finally 
block to ensure the lock is being released even if the protected code throws an exception. Otherwise, should a Thread
prematurely leave the method due to an exception the `unlock()` method won't be executed and the lock never be released
thus preventing all waiting Threads from ever being able to execute the protected code.

[Example](ExampleLockReadWrite.java)

## Semaphore Locks
The Semaphore lock is a lock that can be initialized with a number. This number determines how many Threads can acquire
the lock and proceed into the protected block at one time. Once the maximum number is reached all newly arriving
Threads have to wait until one previous Thread exits the protected block.

The `Semaphore` is initialized within the shared resource as follows:
  
```java
    private Semaphore semaphore = new Semaphore(2, true);
```
  
The first parameter is the number of concurrent Threads that can enter the protected block at one time, the
second argument specifies whether the lock should handle following Threads _fair_. If set to `true` the waiting Threads
will be served in the order in which they arrived, if set to `false` the next Thread trying to acquire the lock will 
get it regardless of how many Threads are already waiting for a free Semaphore.
  
To protect code with this Semaphore lock no `synchronized()` is required. Instead the following way is suggested:
  
```java
   semaphore.acquire();
   // This is where the protected code goes
   semaphore.release();
```
  
It is probably a good practice to place the `semaphore.acquire()` statement into the `try{}` and the 
`semaphore.release()` statement into the `finally{}` clause of the same try-catch block. This will prevent a Thread from 
never returning the Semaphore in case an `Exception` was thrown in the protected code block.  
_(TODO: Test last statement)_   

[Example](ExampleSemaphore.java)
