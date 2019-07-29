package latchMonitorTransaction;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppTx {
    public static void main(String[] args) {
        final LatchTx latch = new LatchTx();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    latch.doOpen();
                    System.out.println("Open");
                } catch (InterruptedException e) {
//                    NOP
                }
            }
        }).start();

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int k = 0; k < 5; k++) {
            exec.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    latch.await();
                    System.out.println("hello");
                    return null;
                }
            });
        }
    }
}
