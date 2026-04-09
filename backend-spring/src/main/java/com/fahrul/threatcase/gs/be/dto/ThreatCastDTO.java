package com.fahrul.threatcase.gs.be.dto;

import lombok.Data;

@Data
public class ThreatCastDTO {

    private String packageName;
    private String risk;
    private Boolean tampering;
    private String device;
    private String rawJson;

}