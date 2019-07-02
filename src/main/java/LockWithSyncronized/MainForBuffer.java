package LockWithSyncronized;

public class MainForBuffer {
    public static void main(String[] args) {
        SingleElemetBuffer singleElemetBuffer = new SingleElemetBuffer();
        new Thread(new Producer(singleElemetBuffer)).start();
        new Thread(new Producer(singleElemetBuffer)).start();

        new Thread(new Consumer(singleElemetBuffer)).start();
        new Thread(new Consumer(singleElemetBuffer)).start();
    }
}
