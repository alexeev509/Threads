package actors.actorsMap;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

public class Container extends UntypedAbstractActor {
    private final ActorRef[] buckets = new ActorRef[16];

    public Container() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = getContext().actorOf(Props.create(Bucket.class), "actor-" + i);
        }
    }

    @Override
    public void onReceive(Object message) {
        Object[] msgArr = (Object[]) message;
        String command = (String) msgArr[0];
        String key = (String) msgArr[1];
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
        }
    }
}
