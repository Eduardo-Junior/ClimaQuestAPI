package com.climaquest.cqapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int PUBLIC_LIMIT_PER_MINUTE = 5;
    private static final int AUTHENTICATED_LIMIT_PER_MINUTE = 60;

    private final RateLimiter rateLimiter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String key;
        int limit;

        if (isPublicLimitedEndpoint(request)) {
            key = "ip:" + request.getRemoteAddr() + ":" + request.getRequestURI();
            limit = PUBLIC_LIMIT_PER_MINUTE;
        } else {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (!(auth != null && auth.getPrincipal() instanceof UUID playerId)) {
                filterChain.doFilter(request, response);
                return;
            }
            key = "player:" + playerId;
            limit = AUTHENTICATED_LIMIT_PER_MINUTE;
        }

        if (!rateLimiter.tryConsume(key, limit)) {
            response.setStatus(429); 
            response.setHeader("Retry-After", "60");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("""
                    {"status":429,"error":"Too Many Requests","messages":["Limite de requisições excedido. Tente novamente em breve."],"timestamp":"%s"}""".formatted(LocalDateTime.now()));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicLimitedEndpoint(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        return ("POST".equals(method) && "/api/auth/login".equals(uri))
                || ("POST".equals(method) && "/api/players".equals(uri));
    }
}
