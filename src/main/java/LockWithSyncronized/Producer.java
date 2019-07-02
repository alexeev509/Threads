package LockWithSyncronized;

public class Producer implements Runnable {
    private SingleElemetBuffer singleElemetBuffer;

    public Producer(SingleElemetBuffer singleElemetBuffer) {
        this.singleElemetBuffer = singleElemetBuffer;
    }

    public void run() {
        while (true) {
            try {
                singleElemetBuffer.put(4);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
