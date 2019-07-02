package LockWithSyncronized;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Difference {
    private static final Object object1=new Object();
    private static final Object object2=new Object();

    private static Lock lock1=new ReentrantLock();
    private static Lock lock2=new ReentrantLock();


    public static void main(String[] args) {
        synchronized (object1){
            System.out.println("enter in first synch");
            synchronized (object2){
                System.out.println("enter in second synch");
                System.out.println("out from second synch");
            }
            System.out.println("out from first synch");
        }

        //Lock


        lock1.lock();
        System.out.println("enter in first lock");
        lock2.lock();
        System.out.println("enter in second lock");
        lock1.unlock();
        System.out.println("out from first lock");
        lock2.unlock();
        System.out.println("out from second lock");

        //RentrantLock can return a lot of Conditions-> a lot of wait sets and blocked sets;
        // if in condtructor renntrantlock - false - the next thread in lock - can be any of threads from blocked set.
        //But if - true - the next thread = first thread, which appeared in blocked set
    }
}
