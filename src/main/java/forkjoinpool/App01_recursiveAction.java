package forkjoinpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;

public class App01_recursiveAction {
    public static void main(String[] args) {
        //аналогичная проблема как в App00_iterative_0 - shared field
        //обрачения к этой перемнной из разных потоков
        AtomicLong result = new AtomicLong(0);
        new ForkJoinPool().invoke(new Task(0, 1000_000, result));
        System.out.println(result.get());
    }

    public static class Task extends RecursiveTask {
        private final int from;
        private final int to;
        private final AtomicLong result;

        public Task(int from, int to, AtomicLong result) {
            this.from = from;
            this.to = to;
            this.result = result;
        }

        //Analog Runnable
        @Override
        protected Object compute() {
            if (to - from < 10_000) {
                result.addAndGet(directCalc());
            } else {
                int mid = (from + to) >>> 1;
                Task taskLeft = new Task(from, mid, result);
                Task taskRight = new Task(mid, to, result);
                invokeAll(taskLeft, taskRight);
            }
            return null;
        }

        private long directCalc() {
            long result = 0;
            for (int index = from; index < to; index++) {
                if (index % 3 != 0 && index % 5 != 0) {
                    result += index;
                }
            }
            return result;
        }
    }
}
