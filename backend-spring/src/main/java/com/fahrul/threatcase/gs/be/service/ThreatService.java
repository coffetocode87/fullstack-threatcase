package com.fahrul.threatcase.gs.be.service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fahrul.threatcase.gs.be.dto.DailyThreatStatsDTO;
import com.fahrul.threatcase.gs.be.dto.DashboardSummaryDTO;
import com.fahrul.threatcase.gs.be.entity.ThreatEvent;
import com.fahrul.threatcase.gs.be.repository.ThreatEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThreatService {

	private final ThreatEventRepository repo;

	// ===== List =====

	public Page<ThreatEvent> getAll(Pageable pageable) {
		return repo.findAll(pageable);
	}

	public Page<ThreatEvent> getByRuleId(String ruleId, Pageable pageable) {
		return repo.findByRuleId(ruleId, pageable);
	}

	public Page<ThreatEvent> getByThreatType(String threatType, Pageable pageable) {
		return repo.findByThreatType(threatType, pageable);
	}
	
	public ThreatEvent getById(Long id) {

	    return repo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Threat not found with id: " + id));

	}

	// ===== Daily Chart =====

	public List<DailyThreatStatsDTO> getDailyStats(int days) {

		OffsetDateTime startDate = OffsetDateTime.now(ZoneId.systemDefault()).minusDays(days);

		List<Object[]> result = repo.countDailyFrom(startDate);

		List<DailyThreatStatsDTO> stats = new ArrayList<>();

		for (Object[] row : result) {
			stats.add(new DailyThreatStatsDTO(row[0].toString(), ((Number) row[1]).longValue()));
		}

		return stats;
	}

	// ===== Dashboard =====

	public DashboardSummaryDTO getDashboardSummary() {

		long total = repo.count();

		OffsetDateTime startOfDay = OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

		long today = repo.countByThreatTimestampAfter(startOfDay);

		String topThreatType = repo.findTopThreatType();
		String topCountry = repo.findTopCountry();

		return new DashboardSummaryDTO(total, today, topThreatType, topCountry);
	}
}