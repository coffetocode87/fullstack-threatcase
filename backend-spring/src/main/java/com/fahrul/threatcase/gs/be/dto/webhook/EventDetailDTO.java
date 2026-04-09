package com.fahrul.threatcase.gs.be.dto.webhook;

import java.util.List;
import lombok.Data;

@Data
public class EventDetailDTO {

    private String timestamp;
    private List<String> reasons;
    private String codeLocation;
}