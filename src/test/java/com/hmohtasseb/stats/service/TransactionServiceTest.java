package com.hmohtasseb.stats.service;

import com.hmohtasseb.stats.model.InstantStats;
import com.hmohtasseb.stats.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    private final static int SEC = 1000;

    @Before
    public void setUp() throws Exception {
        transactionService.reset();
    }

    @Test
    public void testEmpty() {
        InstantStats stats = transactionService.getInstantStats();
        assertThat(stats.getMin()).isNull();
        assertThat(stats.getMax()).isNull();
        assertThat(stats.getSum()).isEqualTo(0.0);
        assertThat(stats.getCount()).isEqualTo(0);
    }


    @Test
    public void testSimple() {
        transactionService.recordTransaction(new Transaction(3.3, LocalTime.now()));
        transactionService.recordTransaction(new Transaction(50, LocalTime.now()));
        transactionService.recordTransaction(new Transaction(20, LocalTime.now()));

        InstantStats stats = transactionService.getInstantStats();
        assertThat(stats.getMin()).isEqualTo(3.3);
        assertThat(stats.getMax()).isEqualTo(50);
        assertThat(stats.getSum()).isEqualTo(73.3);

        System.out.println("transaction time "+ new Transaction(3.3, LocalTime.now()).getTime());
    }

    @Test
    public void testAdvanced() {
        transactionService.recordTransaction(new Transaction(20, LocalTime.now().minusSeconds(55)));
        transactionService.recordTransaction(new Transaction(10, LocalTime.now().minusSeconds(55)));
        transactionService.recordTransaction(new Transaction(30, LocalTime.now().minusSeconds(55)));

        assertStatsEquals(transactionService.getInstantStats(), 10, 30, 60, 3);
        wait(3);

        transactionService.recordTransaction(new Transaction(15, LocalTime.now()));
        transactionService.recordTransaction(new Transaction(15, LocalTime.now()));
        transactionService.recordTransaction(new Transaction(25, LocalTime.now()));

        assertStatsEquals(transactionService.getInstantStats(), 10, 30, 115, 6);
        wait(5);

        assertStatsEquals(transactionService.getInstantStats(), 15, 25, 55, 3);
    }

    private void wait(int seconds) {
        try {
            Thread.sleep(seconds*SEC);
        } catch (InterruptedException e) {
        }
    }

    private void assertStatsEquals(InstantStats stats, double min, double max, double sum, int count) {
        assertThat(stats.getMin()).isEqualTo(min);
        assertThat(stats.getMax()).isEqualTo(max);
        assertThat(stats.getSum()).isEqualTo(sum);
        assertThat(stats.getCount()).isEqualTo(count);
    }


}
