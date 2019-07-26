package transactional;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountLock {
    private final static AtomicInteger idGenerator = new AtomicInteger(0);
    public final int id = idGenerator.getAndIncrement();
    public final Lock lock = new ReentrantLock();
    private int balance;

    public AccountLock(int balance) {
        this.balance = balance;
    }

    public boolean incBalance(int amount) {
        if (balance + amount >= 0) {
            balance += amount;
            return true;
        } else {
            return false;
        }
    }

    public int getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "AccountLock{" +
                "balance=" + balance +
                '}';
    }
}
