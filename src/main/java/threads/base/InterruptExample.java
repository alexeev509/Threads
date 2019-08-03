package threads.base;

public class InterruptExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("hi");
                }
            }
        });
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
        System.out.println("finish");
    }
}
