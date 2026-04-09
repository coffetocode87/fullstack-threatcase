package com.fahrul.threatcase.gs.be.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

	private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

	private Bucket createNewBucket() {

		Bandwidth limit = Bandwidth.builder().capacity(50) // max 50 request
				.refillIntervally(50, Duration.ofMinutes(1)) // refill tiap 1 menit
				.build();

		return Bucket.builder().addLimit(limit).build();
	}

	private Bucket resolveBucket(String ip) {
		return buckets.computeIfAbsent(ip, k -> createNewBucket());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String uri = request.getRequestURI();

		// Rate limit hanya untuk collector webhook
		if (!uri.startsWith("/collector/webhook")) {
			filterChain.doFilter(request, response);
			return;
		}

		String ip = getClientIP(request);

		Bucket bucket = resolveBucket(ip);

		if (bucket.tryConsume(1)) {

			filterChain.doFilter(request, response);

		} else {

			response.setStatus(429);
			response.setContentType("application/json");

			response.getWriter().write("""
					{
					  "error":"Too Many Requests",
					  "message":"Rate limit exceeded",
					  "status":429
					}
					""");
		}
	}

	private String getClientIP(HttpServletRequest request) {

		String xfHeader = request.getHeader("X-Forwarded-For");

		if (xfHeader == null) {
			return request.getRemoteAddr();
		}

		return xfHeader.split(",")[0];
	}
}