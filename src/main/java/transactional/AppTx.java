package transactional;

import org.multiverse.api.Txn;
import org.multiverse.api.callables.TxnCallable;
import org.multiverse.api.exceptions.DeadTxnException;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.multiverse.api.StmUtils.atomic;

public class AppTx {
    static Random rnd = new Random();
    static ExecutorService exec = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        final AccountTX[] accounts = new AccountTX[]{
                new AccountTX(100), new AccountTX(0),
                new AccountTX(100), new AccountTX(0),
                new AccountTX(100), new AccountTX(0),
                new AccountTX(100), new AccountTX(0),
                new AccountTX(100), new AccountTX(0)
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

    public static boolean transfer(final AccountTX from, final AccountTX to, final int ammount) {
        try {
            atomic(new Runnable() {
                @Override
                public void run() {
                    from.incBalance(-ammount);
                    to.incBalance(+ammount);
                }
            });
            return true;
        } catch (DeadTxnException e) {
            return false;
        }
    }

    public static int sum(final AccountTX[] accounts) {
        return atomic(new TxnCallable<Integer>() {
            @Override
            public Integer call(Txn txn) throws Exception {
                int result = 0;
                for (AccountTX account : accounts) {
                    result += account.getBalance();
                }
                return result;
            }
        });
    }

    public static String toStr(final AccountTX[] accounts) {
        return atomic(new TxnCallable<String>() {
            @Override
            public String call(Txn txn) throws Exception {
                return Arrays.toString(accounts);
            }
        });
    }
}
