package com.fahrul.threatcase.gs.be.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class WebhookHmacFilter implements Filter {

	@Value("${security.webhook.secret}")
	private String secret;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (!httpRequest.getRequestURI().contains("/collector/webhook")) {
			chain.doFilter(request, response);
			return;
		}
		String signatureHeader = httpRequest.getHeader("X-Signature");

		if (signatureHeader == null) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String body = new String(httpRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

		String calculatedHmac = HmacUtil.generateHmac(body, secret);

		if (!calculatedHmac.equals(signatureHeader)) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		chain.doFilter(request, response);

	}

}
