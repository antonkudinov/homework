package ru.akudinov.test;


import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Created by akudinov on 04.10.16.
 */
@Slf4j
@DirtiesContext
public class RateLimitTest {
    private RateLimit rateLimit;

    @Before
    public void init(){
        rateLimit = new RateLimit();
    }

    @Test
    public void testFailedRateLimit() throws InterruptedException {
        rateLimit.setLimit(1L);
        long count = prepareCalculation(10000);

        assertTrue("Some request was not accepted", count > 0);
    }

    @Test
    public void testSuccessRateLimit() throws InterruptedException {
        rateLimit.setLimit(100L);
        long count = prepareCalculation(5);

        assertTrue("All requests must be accepted [ " + count + " ]", count == 0);
    }

    private long prepareCalculation(int requests) throws InterruptedException {
        List<Callable<Boolean>> tasks = new ArrayList<>();
        for (int i = 0; i < requests; i++){
            tasks.add(() -> rateLimit.acquire("test"));
        }

        List<Future<Boolean>> futures = Executors.newFixedThreadPool(requests).invokeAll(tasks);


        return futures.stream().filter(s -> {
            try {
                return !s.get();
            } catch (Exception e) {
                return false;
            }
        }).count();
    }



}
