package org.example.service;

import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
public class DeadlockService {

    private ShareableObject object1 = new ShareableObject("a");
    private ShareableObject object2 = new ShareableObject("b");

    private final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

    /**
     * Method to trigger 2 threads that will enter into deadlock
     * this will be an idempotent operation
     * if there are two threads already in deadlock it will not attempt
     * to create more threads in deadlock
     */
    public boolean triggerDeadLock(Long timeoutInSeconds) throws InterruptedException {

        /**
         * Spawn threads only if there are no deadlock threads present
         */
        if ( !getDeadLockedThreads().isPresent()) {
            Deadlockable deadlockable1 = new Deadlockable(object1, object2, timeoutInSeconds);
            Deadlockable deadlockable2 = new Deadlockable(object2, object1, timeoutInSeconds);

            Thread thread1 = new Thread(deadlockable1);
            Thread thread2 = new Thread(deadlockable2);
            thread1.start();
            thread2.start();
            return true;
        }

        return false;
    }

    private Optional<List<ThreadInfo>> getDeadLockedThreads() {
        long[] deadLockedThreadIds = mbean.findDeadlockedThreads();

        if (deadLockedThreadIds == null) {
            return Optional.empty();
        }

        ThreadInfo[] deadLockedThreadInfo =  mbean.getThreadInfo(deadLockedThreadIds);
        return Optional.of(Arrays.asList(deadLockedThreadInfo));
    }

    /**
     * returns a list of thread names that are in deadlock
     * @return
     */
    public Optional<List<String>> detectDeadLock() {

        Optional<List<ThreadInfo>> deadLockedThreadInfos = getDeadLockedThreads();

        if (deadLockedThreadInfos.isPresent()) {
            List<String> deadLockedThreadNames = deadLockedThreadInfos.get().stream()
                    .map(ThreadInfo::getThreadName)
                    .collect(Collectors.toList());
            return Optional.of(deadLockedThreadNames);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Static class to create shareable objects that threads can acquire locks on
     */
    static class ShareableObject {

        String name;
        Lock lock = new ReentrantLock();

        ShareableObject(String name) {
            this.name = name;
        }

        Lock getLock() {
            return lock;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ShareableObject{");
            sb.append("name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * static class that would represent Deadlockable threads
     */
    static class Deadlockable implements Runnable {

        private ShareableObject object1;
        private ShareableObject object2;
        long timeoutInSeconds;
        public static final String acquiredMessage = "Thread Id: %s, ACQUIRED lock on Object: %s";
        public static final String waitingMessage =  "Thread Id: %s, WAITING to acquire lock on Object: %s";

        public Deadlockable(ShareableObject object1, ShareableObject object2, long timeoutInSeconds) {
            this.object1 = object1;
            this.object2 = object2;
            this.timeoutInSeconds = timeoutInSeconds;
        }

        @Override
        public void run() {
            try {
                actOnObjectsInOrder();
            } catch (InterruptedException e) {
                System.out.println(String.format("Thread: %s was interrupted", Thread.currentThread().getName()));
            }
        }

        /**
         * Act on the shareable objects in a particular order
         * @throws InterruptedException
         */
        private void actOnObjectsInOrder() throws InterruptedException {

            //acquire intrinsic lock on the Rentrant lock
            //so that we can use the synchronized keyword
            synchronized (object1.getLock()) {
                object1.getLock().lock();
                System.out.println(String.format(acquiredMessage, Thread.currentThread().getName(), object1));

                //sleep for 1 second
                // to allow the other thread to access object2 and hence simulate a deadlock
                Thread.sleep(1000);
                System.out.println(String.format(waitingMessage, Thread.currentThread().getName(), object2));

                boolean isLockAcquired = object2.getLock().tryLock(timeoutInSeconds, TimeUnit.SECONDS);

                if (isLockAcquired) {
                    object2.getLock().lock();
                    System.out.println(String.format(acquiredMessage, Thread.currentThread().getName(),object1));
                    object2.getLock().unlock();
                }
                object1.getLock().unlock();
            }
            System.out.println(String.format("Exiting Thread: %s", Thread.currentThread().getName()));

        }
    }
}
