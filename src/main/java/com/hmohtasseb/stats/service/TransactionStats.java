package com.hmohtasseb.stats.service;

import com.google.common.collect.MinMaxPriorityQueue;
import com.hmohtasseb.stats.model.InstantStats;
import com.hmohtasseb.stats.model.Transaction;
import lombok.Getter;

import java.util.Comparator;

@Getter
public class TransactionStats {

    private int count;
    private double sum;

    private MinMaxPriorityQueue<Transaction> minMaxQueue = MinMaxPriorityQueue.orderedBy(new MinMaxComparator()).create();

    protected void add(Transaction transaction) {
        minMaxQueue.add(transaction);
        count++;
        sum += transaction.getAmount();
    }

    protected void remove(Transaction transaction) {
        minMaxQueue.remove(transaction);
        count--;
        sum -= transaction.getAmount();
    }

    public InstantStats getInstantStats() {
        synchronized (TransactionStats.class) {
            return new InstantStats(min(), max(), sum, count);
        }
    }

    private Double min() {
        Transaction minTransaction = minMaxQueue.peekFirst();
        return minTransaction == null ? null : minTransaction.getAmount();
    }

    private Double max() {
        Transaction maxTransaction = minMaxQueue.peekLast();
        return maxTransaction == null ? null : maxTransaction.getAmount();
    }

    protected void reset() {
        count = 0;
        sum = 0;
        minMaxQueue.clear();
    }

    class MinMaxComparator implements Comparator<Transaction> {

        @Override
        public int compare(Transaction o1, Transaction o2) {
            return Double.compare(o1.getAmount(), o2.getAmount());
        }
    }
}
