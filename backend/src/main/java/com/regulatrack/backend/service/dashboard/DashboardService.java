package com.regulatrack.backend.service.dashboard;

import com.regulatrack.backend.domain.license.LicenseStatus;
import com.regulatrack.backend.dto.dashboard.DashboardSummaryResponse;
import com.regulatrack.backend.repository.branch.BranchRepository;
import com.regulatrack.backend.repository.company.CompanyRepository;
import com.regulatrack.backend.repository.license.LicenseRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;
    private final LicenseRepository licenseRepository;

    public DashboardService(
            CompanyRepository companyRepository,
            BranchRepository branchRepository,
            LicenseRepository licenseRepository
    ) {
        this.companyRepository = companyRepository;
        this.branchRepository = branchRepository;
        this.licenseRepository = licenseRepository;
    }

    public DashboardSummaryResponse getSummary() {
        return new DashboardSummaryResponse(
                companyRepository.count(),
                branchRepository.count(),
                licenseRepository.count(),
                licenseRepository.countByStatus(LicenseStatus.ACTIVE),
                licenseRepository.countByStatus(LicenseStatus.EXPIRING_SOON),
                licenseRepository.countByStatus(LicenseStatus.EXPIRED),
                licenseRepository.countByStatus(LicenseStatus.PENDING)
        );
    }
}