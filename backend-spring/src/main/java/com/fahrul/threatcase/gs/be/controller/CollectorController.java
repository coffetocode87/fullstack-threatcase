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
import java.util.Map;
import java.util.HashMap;
import java.util.List;

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
	        @RequestHeader(value = "x-threatcast-webhook-signature", required = false) String signature,
	        @RequestBody Map<String, Object> requestBody) throws JsonMappingException, JsonProcessingException {

	    // Process the webhook data with the expected format
	    String ruleId = (String) requestBody.get("ruleId");
	    String ruleName = (String) requestBody.get("ruleName");
	    String appId = (String) requestBody.get("appName");
	    List<Map<String, Object>> threats = (List<Map<String, Object>>) requestBody.get("threats");
	    
	    // Log or process the received data
	    System.out.println("Received webhook with ruleId: " + ruleId);
	    System.out.println("Rule name: " + ruleName);
	    System.out.println("Number of threats: " + (threats != null ? threats.size() : 0));
	    
	    if (threats != null && !threats.isEmpty()) {
	        for (Map<String, Object> threat : threats) {
	            System.out.println("Threat ID: " + threat.get("id"));
	            System.out.println("Threat Type: " + threat.get("threatType"));
	            System.out.println("Device: " + threat.get("deviceModel"));
	        }
	    }

	    return ResponseEntity.ok(Map.of(
	        "status", "success",
	        "message", "Webhook received successfully",
	        "ruleId", ruleId,
	        "threatsProcessed", threats != null ? threats.size() : 0
	    ));
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