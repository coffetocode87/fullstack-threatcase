package com.fahrul.threatcase.gs.be.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fahrul.threatcase.gs.be.entity.ThreatEvent;
import com.fahrul.threatcase.gs.be.repository.CountryStats;
import com.fahrul.threatcase.gs.be.repository.PlatformStats;
import com.fahrul.threatcase.gs.be.repository.ThreatEventRepository;
import com.fahrul.threatcase.gs.be.repository.ThreatTimeStats;
import com.fahrul.threatcase.gs.be.repository.ThreatTypeStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

	private final ThreatEventRepository repository;

	public List<ThreatTypeStats> topThreatTypes() {
		return repository.getTopThreatTypes();
	}

	public List<PlatformStats> platformStats() {
		return repository.getPlatformStats();
	}

	public List<CountryStats> countryStats() {
		return repository.getCountryStats();
	}
	
	public List<ThreatTimeStats> threatsOverTime() {
	    return repository.getThreatsOverTime();
	}
	
	public List<ThreatEvent> recentThreats() {
	    return repository.findTop50ByOrderByThreatTimestampDesc();
	}
}
