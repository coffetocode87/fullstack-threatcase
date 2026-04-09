package com.fahrul.threatcase.gs.be.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahrul.threatcase.gs.be.dto.DailyThreatStatsDTO;
import com.fahrul.threatcase.gs.be.entity.ThreatEvent;
import com.fahrul.threatcase.gs.be.service.AnalyticsService;
import com.fahrul.threatcase.gs.be.service.ThreatService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/threats")
@RequiredArgsConstructor
public class ThreatController {

	private final ThreatService service;

	private final AnalyticsService analyticsService;

	@GetMapping
	public Page<ThreatEvent> list(@RequestParam(required = false) String ruleId,
			@RequestParam(required = false) String threatType, Pageable pageable) {

		if (ruleId != null) {
			return service.getByRuleId(ruleId, pageable);
		}

		if (threatType != null) {
			return service.getByThreatType(threatType, pageable);
		}

		return service.getAll(pageable);
	}

	@GetMapping("/stats/daily")
	public List<DailyThreatStatsDTO> dailyStats(@RequestParam(defaultValue = "7") int days) {

		return service.getDailyStats(days);
	}

	@GetMapping("/recent")
	public List<ThreatEvent> recentThreats(@RequestParam(defaultValue = "10") int limit) {
		return analyticsService.recentThreats();
	}
	
	@GetMapping("/{id}")
	public ThreatEvent getById(@PathVariable Long id) {
	    return service.getById(id);
	}
}
