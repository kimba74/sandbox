Java5 Concurrency Examples
==========================

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

## Executors and Threadpools
[Example](ExampleExecutor.java)

## Enhanced Intrinsic Locks
[Example](ExampleLock.java)

## Read-Write Locks
[Example](ExampleLockReadWrite.java)

## Semaphore Locks
[Example](ExampleSemaphore.java)
