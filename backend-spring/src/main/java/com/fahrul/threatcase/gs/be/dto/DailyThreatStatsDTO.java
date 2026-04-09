package com.fahrul.threatcase.gs.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyThreatStatsDTO {

    private String date;
    private long count;

}