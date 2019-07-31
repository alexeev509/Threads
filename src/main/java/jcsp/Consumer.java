package jcsp;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelInputInt;

public class Consumer implements CSProcess {
    private final ChannelInputInt in;
    private String str;

    public Consumer(ChannelInputInt in, String str) {
        this.in = in;
        this.str = str;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(str + " : " + in.read());
        }
    }
}
