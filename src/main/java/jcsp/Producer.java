package jcsp;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.CSTimer;
import org.jcsp.lang.ChannelOutputInt;

public class Producer implements CSProcess {
    private final ChannelOutputInt out;
    private int k;

    public Producer(ChannelOutputInt out, int k) {
        this.out = out;
        this.k = k;
    }


    @Override
    public void run() {
        CSTimer timer = new CSTimer();
        while (true) {
            out.write(k++);
            timer.sleep(250);
        }
    }
}
