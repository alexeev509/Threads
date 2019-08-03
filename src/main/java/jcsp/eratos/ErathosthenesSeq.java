package jcsp.eratos;

//Example of algorithm is here:
//https://ru.wikipedia.org/wiki/%D0%A0%D0%B5%D1%88%D0%B5%D1%82%D0%BE_%D0%AD%D1%80%D0%B0%D1%82%D0%BE%D1%81%D1%84%D0%B5%D0%BD%D0%B0

import java.util.Arrays;

//Bad, because we can make mistake and have arrayindexofboundexaption
public class ErathosthenesSeq {
    public static void main(String[] args) {
        int N = 1000;
        //[ 0 1 2 3...]
        boolean[] isPrime = new boolean[N + 1];
        Arrays.fill(isPrime, 2, N, true);
        for (int i = 2; i * i <= N; i++) {
            if (isPrime[i])
                for (int j = i; i * j <= N; j++) {
                    isPrime[i * j] = false;
                }
        }
        System.out.println(Arrays.toString(isPrime));
    }
}
