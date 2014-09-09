Java7 Concurrency Examples
==========================

## Fork / Join Threads and Threadpools
To tackle large problems it sometimes becomes necessary to split work between multiple Threads. The Fork/Join mechanism
introduced in Java 7 makes this task easier. This special Threadpool handles a type of `Runnable` that allows the
developer to implement a splitting logic and let the Thread handle the forking and joining of sub-tasks automatically.
The `ForkJoinTask` is this kind of `Runnable`. The `ForkJoinTask` is rarely used directly but instead offers multiple
subclasses that are tailored for the most common needs.
`ForkJoinTask` implementations can be executed either via the `invoke()` method for synchronous or the `execute()`
method for asynchronous processing. The `invoke()` method will wait until the Thread processing has stopped and all
spun of sub-tasks completed. Unfortunately there is no time-out mechanism like with the e.g. `wait()` method, the
owning Thread will halt. To avoid that a developer may choose the asynchronous execution of the tasks via the
`execute()` method. This method will continue without waiting for the task and its potential sub-tasks to finish.
The `ForkJoinPool` provides numerous methods to inquire the state of the Thread processing. It has to be noted that
the default Thread configuration chosen by the `ForkJoinPool` are daemon Threads. Daemon Threads die with the completion
of their parent Thread. In other words: Once the application ends the Threadpool will destroy all Threads regardless
of their state of execution. This default behavior can be modified by providing a custom `ForkJoinWorkerThreadFactory`
to the `ForkJoinPool` via its constructor.

[Example _Sync_](ExampleForkJoin.java)  
[Example _Async_](ExampleForkJoinAsync.java)  

## Phaser Locks
This kind of locks are very similar to the [Cyclic Locks](/src/main/java/org/soabridge/reference/java5/concurrency#cyclic-barrier)
locks introduced in Java 5. The major difference however is that a `Phaser` lock does not require a fix amount of
participating Threads but allows for the registration of new participants during runtime.
 
[Example](ExamplePhaser.java)
