package jcsp;

import org.jcsp.lang.*;

import java.util.Random;

public class App00_one2one_oneClass {
    //Плюсы csp - валидация
    public static void main(String[] args) {
        final One2OneChannelInt c = Channel.one2oneInt();
        new Parallel(
                new CSProcess[]{
                        new CSProcess() {
                            @Override
                            public void run() {
                                Random rnd = new Random(0);
                                CSTimer timer = new CSTimer();
                                while (true) {
                                    timer.sleep(500);
                                    c.out().write(1 + rnd.nextInt(100));
                                }
                            }
                        },
                        new CSProcess() {
                            @Override
                            public void run() {
                                while (true) {
                                    System.out.println(c.in().read());
                                }
                            }
                        }
                }
        ).run();

    }
}
