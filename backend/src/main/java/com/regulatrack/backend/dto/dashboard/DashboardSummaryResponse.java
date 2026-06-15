package com.regulatrack.backend.dto.dashboard;

public record DashboardSummaryResponse(
        long totalCompanies,
        long totalBranches,
        long totalLicenses,
        long activeLicenses,
        long expiringSoonLicenses,
        long expiredLicenses,
        long pendingLicenses
) {
}