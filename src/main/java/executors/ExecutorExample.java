package executors;

import java.util.concurrent.Executor;

public class ExecutorExample {
    public static void main(String[] args) {
        Executor executor = new Executor() {
            public void execute(Runnable command) {
                new Thread(command).start();
            }
        };
        executor.execute(() -> {
            while (true) {
                System.out.println("yee");
            }
        });

        System.out.println("hello");
    }
}
