package latchMonitorTransaction;

import static org.multiverse.api.StmUtils.atomic;
import static org.multiverse.api.StmUtils.retry;

public class AppStack {
    public static void main(String[] args) {
        final MyTxStack<String> stack0 = new MyTxStack<>();
        final MyTxStack<String> stack1 = new MyTxStack<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        /*NOP*/
                    }
                    atomic(new Runnable() {
                        @Override
                        public void run() {
                            if (Math.random() > 0.5) {
                                stack0.push("A0");
                            } else {
                                stack1.push("B1");
                            }
                        }
                    });
                }
            }
        }).start();

        while (true) {
            atomic(new Runnable() {
                @Override
                public void run() {
                    //Idea of twu queues (we dont have such in Concurrent)
                    if (!stack0.isEmpty() && !stack1.isEmpty()) {
                        System.out.println(stack0.pop() + ":" + stack1.pop());
                    } else {
                        retry();
                    }
                }
            });
        }
    }
}
