package forkjoinpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class App00_iterative_2 {
    //Analog with countdownlatch
    public static void main(String[] args) throws InterruptedException {
        AtomicLong result = new AtomicLong(0);
        int cpuCount = Runtime.getRuntime().availableProcessors();
        System.out.println(cpuCount);
        ExecutorService pool = Executors.newFixedThreadPool(cpuCount);
        final CountDownLatch latch = new CountDownLatch(100);


        for (int k = 0; k < 100; k++) {
            final int finalK = k;

            pool.submit(() -> {
                //[0 ... 9999], [10000 ... 19999], ....
                long localResult = calc(10_000 * finalK, 10_000 * (finalK + 1));
                result.addAndGet(localResult);
                latch.countDown();//100-1-1...
            });
        }

        //waiting 100-1-1...=0
        latch.await();
        System.out.println(result);
        pool.shutdown();
    }

    //В данном же случае - нет обращения в общую память - это плюс данной реализации
    //те потоки не обращаются к общим переменным в отличие от прошлого случая
    private static long calc(int from, int to) {
        long result = 0;
        for (int index = from; index < to; index++) {
            if (index % 5 != 0 && index % 3 != 0) {
                result += index;
            }
        }
        return result;
    }
}
