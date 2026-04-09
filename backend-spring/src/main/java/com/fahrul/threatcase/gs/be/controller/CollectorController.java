package com.fahrul.threatcase.gs.be.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;

import com.fahrul.threatcase.gs.be.collector.ThreatCastCollectorService;
import com.fahrul.threatcase.gs.be.dto.webhook.ThreatcastWebhookRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/collector")
@RequiredArgsConstructor
public class CollectorController {

	private final ThreatCastCollectorService service;
	
	@Value("${threatcast.webhook.secret}")
	private String secret;
	
	private final ObjectMapper objectMapper;

//	@PostMapping("/webhook")
//	public String webhook(@RequestHeader("X-API-KEY") String apiKey,@Valid @RequestBody ThreatcastWebhookRequest request) {
//
//		service.saveThreat(request);
//
//		return "saved";
//	}
	
	@PostMapping("/webhook")
	public ResponseEntity<?> webhook(
	        @RequestHeader("x-threatcast-webhook-signature") String signature,
	        @RequestBody String rawBody) throws JsonMappingException, JsonProcessingException {

	    String calculated = hmacSha256(secret, rawBody);

//	    if (!signature.equals(calculated)) {
//	        return ResponseEntity.status(401).body("Invalid signature");
//	    }

	    ThreatcastWebhookRequest request =
	        objectMapper.readValue(rawBody, ThreatcastWebhookRequest.class);

	    service.saveThreat(request);

	    return ResponseEntity.ok("saved");
	}
	
	private String hmacSha256(String secret, String data) {

	    try {

	        Mac mac = Mac.getInstance("HmacSHA256");
	        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
	        mac.init(secretKey);

	        byte[] rawHmac = mac.doFinal(data.getBytes());

	        return Base64.getEncoder().encodeToString(rawHmac);

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to calculate HMAC", e);
	    }
	}
}