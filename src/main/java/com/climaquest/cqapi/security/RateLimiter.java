package com.climaquest.cqapi.security;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// Contador de janela fixa em memória: cada chave tem um contador que zera
// sempre que a requisição cai em um minuto diferente do último registrado.
@Component
public class RateLimiter {

    private static final long WINDOW_MILLIS = 60_000;

    private record Window(long windowStart, AtomicInteger count) {
    }

    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    public boolean tryConsume(String key, int maxRequestsPerMinute) {
        long currentWindow = System.currentTimeMillis() / WINDOW_MILLIS;

        Window window = windows.compute(key, (k, existing) -> {
            if (existing == null || existing.windowStart() != currentWindow) {
                return new Window(currentWindow, new AtomicInteger(1));
            }
            existing.count().incrementAndGet();
            return existing;
        });

        return window.count().get() <= maxRequestsPerMinute;
    }

    // Remove janelas antigas para não acumular memória
    @Scheduled(fixedRate = 600_000)
    public void cleanup() {
        long currentWindow = System.currentTimeMillis() / WINDOW_MILLIS;
        windows.entrySet().removeIf(entry -> entry.getValue().windowStart() < currentWindow - 1);
    }
}
