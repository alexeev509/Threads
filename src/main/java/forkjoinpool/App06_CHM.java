package forkjoinpool;

import java.util.concurrent.ConcurrentHashMap;

public class App06_CHM {
    public static void main(String[] args) {
        ConcurrentHashMap<Long, Long> map = new ConcurrentHashMap<>();
        map.put(6l, 5l);
        map.put(4l, 6l);
        int cpuCount = Runtime.getRuntime().availableProcessors();

        Long result0 = map.search(cpuCount,
                (key, value) -> key.equals(value) ? key : null);
        System.out.println(result0);

        Long result1 = map.reduceKeys(cpuCount,
                (key0, key1) -> key0 + key1);
        System.out.println(result1);
    }
}
