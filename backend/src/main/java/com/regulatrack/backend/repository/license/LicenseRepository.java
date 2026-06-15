package com.regulatrack.backend.repository.license;

import com.regulatrack.backend.domain.license.License;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicenseRepository extends JpaRepository<License, Long> {

    List<License> findByCompanyId(Long companyId);

    List<License> findByBranchId(Long branchId);
}