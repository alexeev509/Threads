package actors.mapReduce;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MRBucket extends UntypedAbstractActor {
    private Map data = new HashMap();

    @Override
    public void onReceive(Object message) throws Throwable {
        Object[] msgArr = (Object[]) message;
        String command = (String) msgArr[0];
        String key = null;
        if (Arrays.asList("put", "get", "remove", "get/result").contains(command)) {
            key = (String) msgArr[1];
        }
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
            case "map-reduce":
                Object id = msgArr[1];
                Mapper mapper = (Mapper) msgArr[2];
                Reducer reducer = (Reducer) msgArr[3];
                Object initElem = msgArr[4];
                Object result = initElem;
                for (Map.Entry entry : (Set<Map.Entry>) data.entrySet()) {
                    result = reducer.reduce(result, mapper.map(entry));
                }
                Object[] response2 = {"map-reduce/result", id, result};
                getSender().tell(response2, getSelf());
                break;
        }
    }
}
