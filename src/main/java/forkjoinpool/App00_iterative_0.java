package forkjoinpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

//Пример итеративного параллеллилизма
public class App00_iterative_0 {
    public static void main(String[] args) throws InterruptedException {
        //Все потоки постоянно используют эту переменную - это плохо.
        //Возможно даже отрицательная масштабируемость - те удвоение количества потоков ведет к падению скорости
        //Когда инкрементируется на одном ядре - то на другом происходит проигрыш
        AtomicLong result = new AtomicLong(0);
        //tHIS IS BAD TO CHOOSE HOW MANY COUNTS WE NEED
        int cpuCount = Runtime.getRuntime().availableProcessors();
        System.out.println(cpuCount);
        ExecutorService pool = Executors.newFixedThreadPool(cpuCount);

        List<Callable<Void>> taskList = new ArrayList<>();
        //Это стоит использовать лишь в случае если знаем количество задач, зачастую мы этого не знаем
        //В этом минус этого решения
        for (int k = 0; k < 100; k++) {
            //In inner classes we cant use not final field
            final int finalK = k;
//            It can be too, because we need  one = This is efficient final field;
//            int finalK=k;
            taskList.add(() -> {
                //[0 ... 9999], [10000 ... 19999], ....
                calc(result, 10_000 * finalK, 10_000 * (finalK + 1));
                return null;
            });
        }
        pool.invokeAll(taskList);
        System.out.println(result);
        pool.shutdown();
    }

    private static void calc(AtomicLong result, int from, int to) {
        for (int index = from; index < to; index++) {
            if (index % 5 != 0 && index % 3 != 0) {
                result.addAndGet(index);
            }
        }
    }

}
