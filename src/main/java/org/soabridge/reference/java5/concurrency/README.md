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
[Example](ExampleLock.java)

## Read-Write Locks
[Example](ExampleLockReadWrite.java)

## Semaphore Locks
[Example](ExampleSemaphore.java)
