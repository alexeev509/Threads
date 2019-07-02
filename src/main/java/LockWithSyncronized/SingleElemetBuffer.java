package LockWithSyncronized;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleElemetBuffer {
    private final Lock lock = new ReentrantLock(true);
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private Integer elem = null;

    public void put(int newElem) throws InterruptedException {
        lock.lock();
        try {
            while (this.elem != null) {
                notFull.await();
            }
            this.elem = newElem;
            notEmpty.signal();//signal in another wait-blocking set, where thread consumers
        } finally {
            lock.unlock();
        }
    }

    public int get() throws InterruptedException {
        lock.lock();
        try {
            while (this.elem == null) {
                notEmpty.await();
            }
            Integer result = this.elem;
            this.elem = null;
            notFull.signal();//signal in another wait-blocking set, where thread consumers
            return result;
        } finally {
            lock.unlock();
        }
    }
}
