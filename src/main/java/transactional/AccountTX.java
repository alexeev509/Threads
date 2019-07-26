package transactional;

import org.multiverse.api.StmUtils;
import org.multiverse.api.references.TxnInteger;

import static org.multiverse.api.StmUtils.abort;

//https://www.codeflow.site/ru/article/java-multiverse-stm
public class AccountTX {
    private final TxnInteger balance;

    public AccountTX(int balance) {
        this.balance = StmUtils.newTxnInteger(balance);
    }

    public void incBalance(final int amount) {
        balance.increment(amount);
        if (balance.get() < 0) {
            abort();
        }
    }

    public int getBalance() {
        return balance.get();
    }

    @Override
    public String toString() {
        return "AccountTX{" +
                "balance=" + balance.get() +
                '}';
    }
}
