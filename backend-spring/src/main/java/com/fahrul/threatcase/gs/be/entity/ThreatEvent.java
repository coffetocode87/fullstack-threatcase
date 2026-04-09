package com.fahrul.threatcase.gs.be.entity;

import java.time.OffsetDateTime;

import com.fahrul.threatcase.gs.be.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "threat_events", indexes = { @Index(name = "idx_device_id", columnList = "deviceId"),
		@Index(name = "idx_threat_type", columnList = "threatType"),
		@Index(name = "idx_app_user_id", columnList = "appUserId"),
		@Index(name = "idx_created_at", columnList = "createdAt"),
		@Index(name = "idx_country", columnList = "country") })
@Data
public class ThreatEvent extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ==== Rule Metadata ====
	private String ruleId;
	private String ruleName;

	// ==== Threat Info ====
	private String threatId; // id dari webhook

	@Column(name = "threat_type")
	private String threatType;

	// ==== App Info ====
	private String appId;
	private String appName;
	private String appVersion;

	// ==== Device Info ====
	private String os;
	private String osVersion;
	private String deviceModel;

	@Column(name = "device_id")
	private String deviceId;

	private String country;

	@Column(name = "ip_address")
	private String ipAddress;

	// ==== User Info ====
	@Column(name = "app_user_id")
	private String appUserId;

	// ==== Threat Timestamp dari Guardsquare ====
	@Column(name = "threat_timestamp")
	private OffsetDateTime threatTimestamp;

	// ==== Full Raw Payload (untuk audit & forensik) ====
	@Column(columnDefinition = "TEXT")
	private String rawJson;
}