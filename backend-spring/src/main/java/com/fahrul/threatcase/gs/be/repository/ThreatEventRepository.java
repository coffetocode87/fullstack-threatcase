package com.fahrul.threatcase.gs.be.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.fahrul.threatcase.gs.be.entity.ThreatEvent;

public interface ThreatEventRepository extends JpaRepository<ThreatEvent, Long> {

    Page<ThreatEvent> findByRuleId(String ruleId, Pageable pageable);

    Page<ThreatEvent> findByThreatType(String threatType, Pageable pageable);


    // ===== RECENT THREATS =====

    List<ThreatEvent> findTop50ByOrderByThreatTimestampDesc();


    // ===== TOP THREAT TYPES =====

    @Query("""
        SELECT t.threatType as type, COUNT(t) as total
        FROM ThreatEvent t
        GROUP BY t.threatType
        ORDER BY total DESC
    """)
    List<ThreatTypeStats> getTopThreatTypes();


    // ===== PLATFORM STATS =====

    @Query("""
        SELECT t.os as platform, COUNT(t) as total
        FROM ThreatEvent t
        GROUP BY t.os
    """)
    List<PlatformStats> getPlatformStats();


    // ===== COUNTRY STATS =====

    @Query("""
        SELECT t.country as country, COUNT(t) as total
        FROM ThreatEvent t
        GROUP BY t.country
        ORDER BY total DESC
    """)
    List<CountryStats> getCountryStats();


    // ===== DAILY THREAT COUNT =====

    @Query("""
        SELECT DATE(t.threatTimestamp), COUNT(t)
        FROM ThreatEvent t
        WHERE t.threatTimestamp >= :startDate
        GROUP BY DATE(t.threatTimestamp)
        ORDER BY DATE(t.threatTimestamp)
    """)
    List<Object[]> countDailyFrom(@Param("startDate") OffsetDateTime startDate);


    // ===== THREATS OVER TIME =====

    @Query(value = """
        SELECT DATE(threat_timestamp) as day, COUNT(*) as total
        FROM threat_events
        GROUP BY DATE(threat_timestamp)
        ORDER BY day
    """, nativeQuery = true)
    List<ThreatTimeStats> getThreatsOverTime();


    // ===== TODAY THREATS =====

    long countByThreatTimestampAfter(OffsetDateTime startOfDay);


    // ===== TOP THREAT TYPE =====

    @Query(value = """
        SELECT threat_type
        FROM threat_events
        GROUP BY threat_type
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """, nativeQuery = true)
    String findTopThreatType();


    // ===== TOP COUNTRY =====

    @Query(value = """
        SELECT country
        FROM threat_events
        GROUP BY country
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """, nativeQuery = true)
    String findTopCountry();
}