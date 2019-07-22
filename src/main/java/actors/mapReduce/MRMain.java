package actors.mapReduce;

import actors.actorsMap.Callback;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.IOException;
import java.util.Map;

import static actors.actorsMap.AkkaUtils.msg;
import static akka.actor.ActorRef.noSender;

public class MRMain {
    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("demo");
        ActorRef container = system.actorOf(Props.create(MRContainer.class), "container");
        ActorRef callback = system.actorOf(Props.create(Callback.class), "callback");

        container.tell(msg("put", "keyA", "valueA"), noSender());
        container.tell(msg("put", "keyB", "valueB"), noSender());
        container.tell(msg("put", "keyC", "valueC"), noSender());
        container.tell(msg("put", "keyD", "valueD"), noSender());

        Mapper<Map.Entry<String, String>, Integer> mapper = arg ->
                arg.getValue().length() == arg.getKey().length() ? 1 : 0;

        Reducer<Integer> reducer = (x, y) -> x + y;

        Integer initElem = 0;

        container.tell(msg("map-reduce", mapper, reducer, initElem), callback);

        System.in.read();
        system.terminate();

    }
}
