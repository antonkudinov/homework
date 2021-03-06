package ru.akudinov.test.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Component
/**
 * This class implements throttle pattern
 */
public class RateLimit {
    public static final int ONE_SECOND_DELAY = 1;
    private final ConcurrentHashMap<String, DelayQueue<Delayed>> limiters;

    @Value("${loansPerSecond}")
    @Setter
    private Long limit;

    public RateLimit() {
        this.limiters = new ConcurrentHashMap<>();
    }

    /**
     * try to acquire rate limiter by country
     **/
    public boolean acquire(String country) {
        DelayQueue<Delayed> delayQueue = limiters.computeIfAbsent(country, delayQueueFunction());

        final Delayed poll = delayQueue.poll(); // try to get delayed element, null if absent
        delayQueue.put(createDelayedElement(ONE_SECOND_DELAY));
        return poll != null;

    }

    private Function<String, DelayQueue<Delayed>> delayQueueFunction() {
        return name ->
        {
            DelayQueue<Delayed> dQueue = new DelayQueue<>();
            for (int i = 0; i < limit; i++) {
                dQueue.put(createDelayedElement(0));
            }
            return dQueue;
        };
    }

    private Delayed createDelayedElement(int delay) {
        return new Delayed() {
            @Override
            public long getDelay(TimeUnit unit) {
                return unit.toSeconds(delay);
            }

            @Override
            public int compareTo(Delayed o) {
                return Long.compare(getDelay(TimeUnit.SECONDS), o.getDelay(TimeUnit.SECONDS));
            }
        };
    }


}
