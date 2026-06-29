package com.regulatrack.backend.repository.license;

import com.regulatrack.backend.domain.license.License;
import com.regulatrack.backend.domain.license.LicenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LicenseRepository extends JpaRepository<License, Long>, JpaSpecificationExecutor<License> {

    List<License> findByCompanyId(Long companyId);

    List<License> findByBranchId(Long branchId);

    List<License> findByStatus(LicenseStatus status);

    long countByStatus(LicenseStatus status);
}