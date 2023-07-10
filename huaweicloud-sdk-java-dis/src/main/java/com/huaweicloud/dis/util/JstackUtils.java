package com.huaweicloud.dis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/**
 * 打印堆栈线程
 */
public class JstackUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JstackUtils.class);
    private static final Map<Thread, Long> cache = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService exe = Executors.newScheduledThreadPool(1);

    static {
        Thread thread = new Thread(() -> cleanup(), "jstackUtils-Thread");
        thread.setDaemon(true);
        thread.start();
    }

    public static void put(Thread thread, long requestConnectionTimeOut) {
        long requestExpireTimeout = 0L;
        if (requestConnectionTimeOut < 30000) {
            requestExpireTimeout = 30000 * 5;
        } else {
            requestExpireTimeout = requestConnectionTimeOut * 5;
        }
        cache.put(thread, System.currentTimeMillis() + requestExpireTimeout);
    }

    private static void cleanup() {
        while (true) {
            long now = System.currentTimeMillis();
            // find expired requests
            List<Thread> expired = cache.entrySet().stream()
                    .filter(e -> e.getValue() < now)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            expired.forEach(t -> {
                printStackTrace(t);
                sleep(3000);
                printStackTrace(t);
                cache.remove(t);
            });
            sleep(20000);
        }
    }

    public static void remove(Thread thread) {
        cache.remove(thread);
    }

    private static void printStackTrace(Thread thread) {
        StringBuilder builder = new StringBuilder();
        builder.append(thread.getThreadGroup()).append("     threadState: ")
               .append(thread.getState()).append("\n");
        for (StackTraceElement element : thread.getStackTrace()) {
            builder.append(element.toString());
        }
        LOG.info(builder.toString());
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
