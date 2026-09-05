package com.regulatrack.backend.controller.dashboard;

import com.regulatrack.backend.dto.dashboard.DashboardSummaryResponse;
import com.regulatrack.backend.service.dashboard.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@org.springframework.security.access.prepost.PreAuthorize("hasAuthority('PERM_DASHBOARD_VIEW')")
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }
}