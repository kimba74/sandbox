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

[Example](ExampleCallable.java)

## Cyclic Barriers
[Example](ExampleCyclicBarrier.java)

## Enhanced Intrinsic Locks
The new locking mechanism for intrinsic locks addressed a problem in which a Thread could not make its intention known
to call a synchronized block in a shared object owned by another `Thread`, thus potentially leading leading to a
dead-lock if the other Thread tried to access a shared object owned by the first Thread (bower/bowee example in Java
tutorial. With the new `ReentrantLock` a Thread can pre-acquire lock and only execute if it possesses all involved locks
or back off it not all locks could be acquired. 

[Example](ExampleLock.java)

## Read-Write Locks
A special for of the new locks are the Read-Write locks. This lock consists out of two individual locks that are joined
together in a higher level lock object. The two locks are a Read- and a Write lock. When synchronizing blocks the
developer has the option to choose if the operation is read-only or if it has some write elements embedded in it. The
Read lock can be acquired by as many Threads as the developer chooses as long as there is no Write-lock imposed. Only
one Thread can own a Write-lock at a time. While owning a Write-lock no Read-lock can be acquired and therefore no read
operation can be performed. A Write-lock can be downgraded to a Read-Lock to allow waiting Threads to proceed.

[Example](ExampleLockReadWrite.java)

## Semaphore Locks
[Example](ExampleSemaphore.java)
