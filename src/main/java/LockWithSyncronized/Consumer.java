package LockWithSyncronized;

public class Consumer implements Runnable {
    SingleElemetBuffer singleElemetBuffer;

    public Consumer(SingleElemetBuffer singleElemetBuffer) {
        this.singleElemetBuffer = singleElemetBuffer;
    }

    public void run() {
        while (true) {
            try {
                int result = singleElemetBuffer.get();
                System.out.println(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
