package actors.actorsMap;

import akka.actor.UntypedAbstractActor;

import java.util.Arrays;

public class Callback extends UntypedAbstractActor {
    @Override
    public void onReceive(Object message) {
        if (message instanceof Object[]) {
            System.out.println("result: " + Arrays.toString((Object[]) message));
        } else {
            System.out.println("result: " + message);
        }
    }
}
