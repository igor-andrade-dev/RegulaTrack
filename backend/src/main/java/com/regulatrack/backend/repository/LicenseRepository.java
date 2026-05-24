package com.regulatrack.backend.repository;

import com.regulatrack.backend.domain.License;
import com.regulatrack.backend.domain.LicenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicenseRepository extends JpaRepository<License, Long> {

    List<License> findByStatus(LicenseStatus status);

    List<License> findByCompanyId(Long companyId);

    List<License> findByBranchId(Long branchId);
}