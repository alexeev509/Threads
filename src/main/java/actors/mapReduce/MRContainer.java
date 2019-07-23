package actors.mapReduce;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MRContainer extends UntypedAbstractActor {
    final static int BUCKET_SIZE = 16;
    private final ActorRef[] buckets = new ActorRef[BUCKET_SIZE];
    //We need it because when slaves-threads - will return answer - they return on which task this answer is.
    private long lasdId = 0;
    private final Map<Long, Object> results = new HashMap<>();
    private final Map<Long, Reducer> reducers = new HashMap<>();
    private final Map<Long, Integer> counts = new HashMap<>();
    private final Map<Long, ActorRef> callbacks = new HashMap<>();

    public MRContainer() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = getContext().actorOf(Props.create(MRBucket.class), "bucket-" + i);
        }
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        Object[] msgArr = (Object[]) message;
        String command = (String) msgArr[0];
        String key = null;//this isnt right; this is temporally
        if (Arrays.asList("put", "get", "remove", "get/result").contains(command)) {
            key = (String) msgArr[1];
        }
        switch (command) {
            case "put":
            case "remove":
                buckets[Math.abs(key.hashCode() % 16)].tell(message, getSelf());
                break;
            case "get":
                Object[] nextGet = {"get", key, getSender()};
                buckets[Math.abs(key.hashCode() % 16)].tell(nextGet, getSelf());
                break;
            case "get/result":
                String value = (String) msgArr[2];
                ActorRef originalSender = (ActorRef) msgArr[3];
                Object[] responseGet = {"get/result", key, value};
                originalSender.tell(responseGet, getSelf());
                break;
            case "map-reduce":
                Object[] newMsg = {msgArr[0], ++lasdId, msgArr[1], msgArr[2], msgArr[3]};
                for (ActorRef bucket : buckets) {
                    bucket.tell(newMsg, getSelf());
                }
                results.put(lasdId, msgArr[3]);
                reducers.put(lasdId, (Reducer) msgArr[2]);
                counts.put(lasdId, 0);
                callbacks.put(lasdId, getSender());
                break;
            case "map-reduce/result":
                Long id = (Long) msgArr[1];
                counts.put(lasdId, counts.get(id) + 1);
                Object oldResult = results.get(id);
                Object newResult = msgArr[2];
                results.put(id, reducers.get(id).reduce(oldResult, newResult));

                if (counts.get(id) == BUCKET_SIZE) {
                    ActorRef originalAsker = callbacks.get(id);
                    originalAsker.tell(results.get(id), getSelf());
                    counts.remove(id);
                    reducers.remove(id);
                    results.remove(id);
                    callbacks.remove(id);
                }
                break;
        }
    }
}
