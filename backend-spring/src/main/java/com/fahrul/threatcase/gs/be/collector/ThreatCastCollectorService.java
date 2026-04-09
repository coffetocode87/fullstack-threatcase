package com.fahrul.threatcase.gs.be.collector;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.MeterRegistry;

import com.fahrul.threatcase.gs.be.dto.webhook.ThreatItemDTO;
import com.fahrul.threatcase.gs.be.dto.webhook.ThreatcastWebhookRequest;
import com.fahrul.threatcase.gs.be.entity.ThreatEvent;
import com.fahrul.threatcase.gs.be.repository.ThreatEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThreatCastCollectorService {

    private final ThreatEventRepository threatEventRepository;
    private final ObjectMapper objectMapper;
    private final MeterRegistry meterRegistry;

    public void saveThreat(ThreatcastWebhookRequest request) {

        List<ThreatEvent> events = new ArrayList<>();

        for (ThreatItemDTO item : request.getThreats()) {

            ThreatEvent event = new ThreatEvent();

            event.setRuleId(request.getRuleId());
            event.setRuleName(request.getRuleName());

            event.setThreatId(item.getId());
            event.setThreatType(item.getThreatType());

            event.setAppId(request.getAppId());
            event.setAppName(request.getAppName());
            event.setAppVersion(item.getAppVersion());

            event.setOs(item.getOs());
            event.setOsVersion(item.getOsVersion());
            event.setDeviceModel(item.getDeviceModel());
            event.setDeviceId(item.getDeviceId());

            event.setCountry(item.getCountry());
            event.setIpAddress(item.getIP());

            event.setAppUserId(item.getAppUserId());

            if (item.getTimestamp() != null) {
                event.setThreatTimestamp(OffsetDateTime.parse(item.getTimestamp()));
            }

            try {
                event.setRawJson(objectMapper.writeValueAsString(request));
            } catch (JsonProcessingException e) {
                event.setRawJson("{}");
            }

            events.add(event);
        }

        threatEventRepository.saveAll(events);

        log.info("Threat webhook saved - ruleId={}, events={}",
                request.getRuleId(),
                events.size());

        meterRegistry.counter("threat.webhook.received").increment();
        meterRegistry.counter("threat.events.saved").increment(events.size());
    }
}