package ru.akudinov.test;


import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import ru.akudinov.test.service.RateLimit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by akudinov on 04.10.16.
 */
@Slf4j
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

        assertTrue("Some request was not accepted [" + count + "]", count > 0);
    }

    @Test
    public void testSuccessRateLimit() throws InterruptedException {
        rateLimit.setLimit(100L);
        long count = prepareCalculation(15);

        assertEquals("All requests must be accepted [ " + count + " ]", 0, count);
    }

    private long prepareCalculation(int requests) throws InterruptedException {
        List<Callable<Boolean>> tasks = new ArrayList<>();
        for (int i = 0; i < requests; i++){
            tasks.add(() -> rateLimit.acquire("test"));
        }

        List<Future<Boolean>> futures = Executors.newCachedThreadPool().invokeAll(tasks);


        return futures.stream().filter(s -> {
            try {
                return !s.get();
            } catch (Exception e) {
                return false;
            }
        }).count();
    }



}

