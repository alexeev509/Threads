package latchMonitorTransaction;

import org.multiverse.api.StmUtils;
import org.multiverse.api.Txn;
import org.multiverse.api.callables.TxnBooleanCallable;
import org.multiverse.api.references.TxnBoolean;

import static org.multiverse.api.StmUtils.atomic;
import static org.multiverse.api.StmUtils.retry;

public class LatchTx {
    private TxnBoolean open = StmUtils.newTxnBoolean(false);

    public boolean isOpen() {
        return atomic(new TxnBooleanCallable() {
            @Override
            public boolean call(Txn txn) throws Exception {
                return open.get();
            }
        });
    }

    public void doOpen() {
        atomic(new Runnable() {
            @Override
            public void run() {
                open.set(true);
            }
        });
    }

    public void await() {
        atomic(new Runnable() {
            @Override
            public void run() {
                //Like spinlock
                if (!open.get()) {
                    retry();
                }
            }
        });
    }
}
