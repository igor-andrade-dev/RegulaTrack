package com.regulatrack.backend.repository.license;

import com.regulatrack.backend.domain.license.LicenseDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LicenseDocumentRepository extends JpaRepository<LicenseDocument, Long> {
    List<LicenseDocument> findByLicenseIdOrderByUploadedAtDesc(Long licenseId);
    Optional<LicenseDocument> findByIdAndLicenseId(Long id, Long licenseId);
}
