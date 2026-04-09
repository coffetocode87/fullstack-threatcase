package com.fahrul.threatcase.gs.be.security;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class HmacUtil {

	public static String generateHmac(String payload, String secret) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			mac.init(secretKeySpec);

			byte[] hash = mac.doFinal(payload.getBytes());

			return Base64.getEncoder().encodeToString(hash);
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate HMAC", e);
		}
	}

}
