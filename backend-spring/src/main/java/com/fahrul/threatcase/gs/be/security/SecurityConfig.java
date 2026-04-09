package com.fahrul.threatcase.gs.be.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {

	private final RateLimitFilter rateLimitFilter;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(RateLimitFilter rateLimitFilter, JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.rateLimitFilter = rateLimitFilter;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				// ✅ WAJIB TAMBAH INI
				.cors(cors -> {})

				.csrf(csrf -> csrf.disable())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.httpBasic(basic -> basic.disable()).formLogin(form -> form.disable())

				// filter
				.addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

				// authorization
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/**", "/collector/webhook", "/swagger-ui/**", "/v3/api-docs/**")
						.permitAll()

						.requestMatchers("/api/**").authenticated().requestMatchers("/actuator/**").hasRole("ADMIN")

						.anyRequest().denyAll());

		return http.build();
	}
}