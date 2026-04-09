package com.fahrul.threatcase.gs.be.dto.webhook;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ThreatItemDTO {

	@NotBlank(message = "threat id must not be blank")
    private String id;
	
	@NotBlank(message ="threatType must not be blank")
    private String threatType;
	
    private String appVersion;
    private String os;
    private String osVersion;
    private String deviceModel;
    private String deviceId;
    private String country;
    private String IP;
    private String appUserId;
    
    @NotBlank(message = "timestamp must not be blank")
    private String timestamp;

    private List<EventDetailDTO> events;
}