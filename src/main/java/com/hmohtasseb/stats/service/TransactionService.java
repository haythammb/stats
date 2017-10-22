package com.hmohtasseb.stats.service;

import com.google.common.annotations.VisibleForTesting;
import com.hmohtasseb.stats.model.InstantStats;
import com.hmohtasseb.stats.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    public static final int TIMEOUT_SEC = 60;
    private static final int REMOVE_JOB_INTERVAL_SEC = 1;
    
    private PriorityBlockingQueue<Transaction> transactions = new PriorityBlockingQueue<>(10, new TimeComparator());
    private TransactionStats transactionStats = new TransactionStats();

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    public TransactionService() {
        new TimedOutTransactionRemoveJob().start();
    }

    public synchronized boolean recordTransaction(Transaction transaction) {
        transactions.add(transaction);
        transactionStats.add(transaction);

        logger.info("New transaction recorded {}", transaction);

        return true;
    }

    public InstantStats getInstantStats() {
        return transactionStats.getInstantStats();
    }

    @VisibleForTesting
    protected void reset() {
        transactionStats.reset();
        transactions.clear();
    }

    class TimedOutTransactionRemoveJob {
        public void start() {
            Runnable runnable = () -> {

                while (!transactions.isEmpty()) {
                    Transaction transaction = transactions.peek();
                    long duration = transaction.getDurationInSec();

                    logger.info("testing transaction {}, duration:{} sec", transaction, duration);
                    // if the top of the sorted queue is not timed out -> nothing to remove
                    if (duration <= TIMEOUT_SEC) {
                        break;
                    }

                    logger.info("removing transaction {}", transaction);
                    transactions.remove(transaction);
                    transactionStats.remove(transaction);
                }
            };

            ScheduledExecutorService service = Executors
                    .newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(runnable, 0, REMOVE_JOB_INTERVAL_SEC, TimeUnit.SECONDS);
        }
    }

    class TimeComparator implements Comparator<Transaction> {
        @Override
        public int compare(Transaction o1, Transaction o2) {
            return o1.getTime().compareTo(o2.getTime());
        }
    }
}
