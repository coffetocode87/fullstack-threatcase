package com.fahrul.threatcase.gs.be.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahrul.threatcase.gs.be.repository.CountryStats;
import com.fahrul.threatcase.gs.be.repository.PlatformStats;
import com.fahrul.threatcase.gs.be.repository.ThreatTimeStats;
import com.fahrul.threatcase.gs.be.repository.ThreatTypeStats;
import com.fahrul.threatcase.gs.be.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

	private final AnalyticsService service;

	@GetMapping("/threat-types")
	public List<ThreatTypeStats> threatTypes() {
		return service.topThreatTypes();
	}

	@GetMapping("/platforms")
	public List<PlatformStats> platforms() {
		return service.platformStats();
	}

	@GetMapping("/countries")
	public List<CountryStats> countries() {
		return service.countryStats();
	}
	
	@GetMapping("/threats-over-time")
	public List<ThreatTimeStats> threatsOverTime() {
	    return service.threatsOverTime();
	}
}
