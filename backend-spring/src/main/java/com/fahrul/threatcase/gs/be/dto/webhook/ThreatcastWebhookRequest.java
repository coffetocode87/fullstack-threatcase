package com.fahrul.threatcase.gs.be.dto.webhook;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ThreatcastWebhookRequest {

	@NotBlank(message = "ruleId must not be blank")
    private String ruleId;
	
	@NotBlank(message = "ruleName must not be blank")
    private String ruleName;
	
	@NotBlank(message = "appId must not be blank")
    private String appId;
    
	@NotBlank(message =  "appName must not be blank")
    private String appName;

	@NotBlank(message = "threats must not be empty")
	@Valid
    private List<ThreatItemDTO> threats;
}