package actors.actorsMap;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.IOException;

import static actors.actorsMap.AkkaUtils.msg;
import static akka.actor.ActorRef.noSender;


public class Main {
    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("demo");
        ActorRef container = system.actorOf(Props.create(Container.class), "container");
        ActorRef callback = system.actorOf(Props.create(Callback.class), "callback");

        container.tell(msg("put", "keyA", "valueA"), noSender());
        container.tell(msg("put", "keyB", "valueB"), noSender());
        container.tell(msg("put", "keyC", "valueC"), noSender());

        container.tell(msg("remove", "keyB"), noSender());

        container.tell(msg("get", "keyA", "valueA"), callback);
        container.tell(msg("get", "keyB", "valueB"), callback);
        container.tell(msg("get", "keyC", "valueC"), callback);

        System.in.read();
        system.terminate();
    }
}
