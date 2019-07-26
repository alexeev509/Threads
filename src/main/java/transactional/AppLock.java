package transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppLock {
    static Random rnd = new Random();
    static ExecutorService exec = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        final AccountLock[] accounts = new AccountLock[]{
                new AccountLock(100), new AccountLock(0),
                new AccountLock(100), new AccountLock(0),
                new AccountLock(100), new AccountLock(0),
                new AccountLock(100), new AccountLock(0),
                new AccountLock(100), new AccountLock(0)
        };
        for (int k = 0; k < 4 * Runtime.getRuntime().availableProcessors(); k++) {
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        int from = rnd.nextInt(accounts.length);
                        int to = rnd.nextInt(accounts.length);
                        if (from != to) {
                            int delta = rnd.nextInt(50);
                            transfer(accounts[from], accounts[to], delta);
                        }
                    }
                }
            });
        }
        Thread.sleep(1000);
        System.out.println("ggg");

        System.out.println(sum(accounts));
        System.out.println(toStr(accounts));
    }

    private static String toStr(AccountLock[] accounts) throws Exception {
        AccountLock[] tmp = accounts.clone();
        return lockRecursivly(accounts, new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Arrays.toString(tmp);
            }
        });
    }

    private static int sum(AccountLock[] accounts) throws Exception {
        //поверхостное клонирование - нужно было бы если бы счета не были упорядочены по умолчанию
        //(когда содаем счета из id по порядку 0-1-2-3...
        final AccountLock[] tmp = accounts.clone();
        Arrays.sort(tmp, new Comparator<AccountLock>() {
            @Override
            public int compare(AccountLock acc0, AccountLock acc1) {
                return acc0.id - acc1.id;
            }
        });
        return lockRecursivly(tmp, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int result = 0;
                for (AccountLock acc : tmp) {
                    result += acc.getBalance();
                }
                return result;
            }
        });
    }

    private static void transfer(final AccountLock from, final AccountLock to, final int amount) {
        //Ordering with id
        AccountLock fst = (from.id < to.id) ? from : to;
        AccountLock snd = (from.id >= to.id) ? from : to;
//        With such realization we can have deadlock:
//        AccountLock fst=from;
//        AccountLock snd=to;

        fst.lock.lock();
        try {
            snd.lock.lock();
            try {
                if (from.incBalance(-amount)) {
                    if (!to.incBalance(+amount)) {
                        from.incBalance(+amount);
                    }
                }
            } finally {
                snd.lock.unlock();
            }
        } finally {
            fst.lock.unlock();
        }
    }

    private static <T> T lockRecursivly(AccountLock[] accounts, Callable<T> c) throws Exception {
        if (accounts.length > 0) {
            accounts[0].lock.lock();
            try {
                return lockRecursivly(Arrays.copyOfRange(accounts, 1, accounts.length), c);
            } finally {
                accounts[0].lock.unlock();
            }
        } else {
            return c.call();
        }
    }

}
