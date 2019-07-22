package actors.actorsMap;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;

import java.util.HashMap;
import java.util.Map;

public class Bucket extends UntypedAbstractActor {
    private Map data = new HashMap();

    @Override
    public void onReceive(Object message) {
        Object[] msgArr = (Object[]) message;
        String command = (String) msgArr[0];
        String key = (String) msgArr[1];
        switch (command) {
            case "put":
                Object value = msgArr[2];
                data.put(key, value);
                break;
            case "remove":
                data.remove(key);
                break;
            case "get":
                ActorRef originalSender = (ActorRef) msgArr[2];
                Object[] response = {"get/result", key, data.get(key), originalSender};
                //getSender().forward(response,  getSelf());
                getSender().forward(response, getContext());//??
                break;
        }
    }
}
