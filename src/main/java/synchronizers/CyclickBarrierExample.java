package synchronizers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclickBarrierExample {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3,
                () -> System.out.println("Барьер достигнут"));


        System.out.println("Запуск потоков");

        new MyThread(cyclicBarrier, "A1");
        new MyThread(cyclicBarrier, "B1");
        new MyThread(cyclicBarrier, "C1");

        //Отличие от CountDownLatch - повторное использование
        new MyThread(cyclicBarrier, "A2");
        new MyThread(cyclicBarrier, "B2");
        new MyThread(cyclicBarrier, "C2");


    }
}

class MyThread implements Runnable {

    CyclicBarrier cbar;
    String name;

    MyThread(CyclicBarrier c, String n) {
        cbar = c;

        name = n;
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println(name);


        try {
            cbar.await();
        } catch (BrokenBarrierException exc) {
            System.out.println(exc);
        } catch (InterruptedException exc) {
            System.out.println(exc);
        }
    }
}
