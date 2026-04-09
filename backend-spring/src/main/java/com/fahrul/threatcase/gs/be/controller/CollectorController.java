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
import com.fahrul.threatcase.gs.be.entity.ThreatEvent;
import com.fahrul.threatcase.gs.be.repository.ThreatEventRepository;

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
	private final ThreatEventRepository threatEventRepository;
	
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
	    String appId = (String) requestBody.get("appId");
	    String appName = (String) requestBody.get("appName");
	    List<Map<String, Object>> threats = (List<Map<String, Object>>) requestBody.get("threats");
	    
	    System.out.println("Received webhook with ruleId: " + ruleId);
	    System.out.println("Rule name: " + ruleName);
	    System.out.println("Number of threats: " + (threats != null ? threats.size() : 0));
	    
	    // Simple approach: save each threat as ThreatEvent directly
	    try {
	        if (threats != null && !threats.isEmpty()) {
	            for (Map<String, Object> threat : threats) {
	                ThreatEvent threatEvent = new ThreatEvent();
	                threatEvent.setRuleId(ruleId);
	                threatEvent.setRuleName(ruleName);
	                threatEvent.setAppId(appId);
	                threatEvent.setAppName(appName);
	                threatEvent.setThreatId((String) threat.get("id"));
	                threatEvent.setThreatType((String) threat.get("threatType"));
	                threatEvent.setAppVersion((String) threat.get("appVersion"));
	                threatEvent.setOs((String) threat.get("os"));
	                threatEvent.setOsVersion((String) threat.get("osVersion"));
	                threatEvent.setDeviceModel((String) threat.get("deviceModel"));
	                threatEvent.setDeviceId((String) threat.get("deviceId"));
	                threatEvent.setCountry((String) threat.get("country"));
	                threatEvent.setIpAddress((String) threat.get("IP"));
	                threatEvent.setAppUserId((String) threat.get("appUserId"));
	                
	                // Save raw JSON for audit
	                threatEvent.setRawJson(objectMapper.writeValueAsString(threat));
	                
	                // Save to database using repository
	                threatEventRepository.save(threatEvent);
	                
	                System.out.println("Saved threat: " + threat.get("id") + " - " + threat.get("threatType"));
	            }
	        }
	        System.out.println("All threats saved to database!");
	    } catch (Exception e) {
	        System.err.println("Failed to save threat data: " + e.getMessage());
	        e.printStackTrace();
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