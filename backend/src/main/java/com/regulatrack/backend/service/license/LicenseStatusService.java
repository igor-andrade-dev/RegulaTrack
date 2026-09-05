package com.regulatrack.backend.service.license;

import com.regulatrack.backend.domain.license.LicenseStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LicenseStatusService {

    private static final int EXPIRING_SOON_DAYS = 30;

    public LicenseStatus calculateStatus(LocalDate expiresAt) {
        if (expiresAt == null) {
            return LicenseStatus.PENDING;
        }

        LocalDate today = LocalDate.now();

        if (expiresAt.isBefore(today)) {
            return LicenseStatus.EXPIRED;
        }

        if (!expiresAt.isAfter(today.plusDays(EXPIRING_SOON_DAYS))) {
            return LicenseStatus.EXPIRING_SOON;
        }

        return LicenseStatus.ACTIVE;
    }
}