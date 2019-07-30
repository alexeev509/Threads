package forkjoinpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

//В этом примере вообще нет shared fields
//важно что сумма ассоциативна и можно делать параллельную редукци/
//в методе compute()
public class App02_recursiveTask {
    public static void main(String[] args) {
        long result = new ForkJoinPool().invoke(new Task(0, 1000_000));
        System.out.println(result);
    }

    public static class Task extends RecursiveTask<Long> {
        private final int from;
        private final int to;

        public Task(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            if (to - from < 10_000) {
                return directCalc();
            } else {
                int mid = (from + to) >>> 1;
                Task taskLeft = new Task(from, mid);
                Task taskRight = new Task(mid, to);
                taskLeft.fork();  //start()
                taskRight.fork();//start()
                // (1+2+3+4+5+) + (6+7+8+9+0)
                //тут важна ассоциатисность !!!
                //Общение потоков в виде дерева
                return taskLeft.join() + taskRight.join();
                //join() - like thread join(), but return result like in future method get()
            }
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
