package latchMonitorTransaction;

//CountDownLatch - аналог
public class LatchMonitor {
    private boolean open = false;

    public synchronized void doOpen() {
        open = true;
        this.notifyAll();
    }

    public synchronized void await() throws InterruptedException {
        while (!open) {
            this.wait();
        }
    }
}
