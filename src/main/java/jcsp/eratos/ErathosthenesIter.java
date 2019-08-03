package jcsp.eratos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

//In this realization we dont have risk to have arrayindexofboundexeption
//But we have infinite cycle while - and we have risk, that it can dont stop
public class ErathosthenesIter implements Iterator<Integer> {
    private List<Integer> primes = new ArrayList<>(asList(2));

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        int k = primes.get(primes.size() - 1);
        next_probe:
        while (true) {
            k++;
            for (int prime : primes) {
                if (k % prime == 0)
                    continue next_probe;
            }
            primes.add(k);
            return k;
        }
    }
}

class ErathosthenesIterDemo {
    public static void main(String[] args) {
        Iterator<Integer> primes = new ErathosthenesIter();
        for (int i = 0; i < 10; i++) {
            System.out.println(primes.next());
        }
    }
}