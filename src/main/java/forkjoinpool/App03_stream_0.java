package forkjoinpool;

import java.util.stream.LongStream;
import java.util.stream.Stream;

//Все строится на ForkJoin в паралелльных стримах 
public class App03_stream_0 {
    public static void main(String[] args) {
        {
            long result = LongStream.range(0, 1000_000)
                    .parallel()
                    .filter(x -> x % 3 != 0)
                    .filter(x -> x % 5 != 0)
                    .sum();
            System.out.println(result);
        }
        {
            //Тут небольшая ошибка (бесконечная генерация) но суть не в этом
            //Плохо то, что источником потоа является последовательная штука
            //Стрим без типа - значит будет постоянно boxing/unboxing
            //Поэтому правильно использовать LongStream
            long result = Stream.iterate(0, k -> k + 1)
                    .parallel()
                    .filter(x -> x % 3 != 0)
                    .filter(x -> x % 5 != 0)
                    .reduce(0, (x, y) -> x + y);
            System.out.println(result);
        }
    }
}
