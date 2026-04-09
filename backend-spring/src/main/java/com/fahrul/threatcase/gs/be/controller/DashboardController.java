package com.fahrul.threatcase.gs.be.controller;

import com.fahrul.threatcase.gs.be.dto.DashboardSummaryDTO;
import com.fahrul.threatcase.gs.be.service.ThreatService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ThreatService threatService;

    @GetMapping("/summary")
    public DashboardSummaryDTO summary() {
        return threatService.getDashboardSummary();
    }
}