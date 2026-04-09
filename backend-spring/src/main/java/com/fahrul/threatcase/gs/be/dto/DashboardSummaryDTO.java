package com.fahrul.threatcase.gs.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryDTO {

    private long totalThreats;
    private long todayThreats;
    private String topThreatType;
    private String topCountry;

}