package com.regulatrack.backend.service.license;

import com.regulatrack.backend.domain.license.LicenseStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LicenseStatusServiceTest {

    private final LicenseStatusService licenseStatusService = new LicenseStatusService();

    @Test
    void shouldReturnPendingWhenExpirationDateIsNull() {
        LicenseStatus status = licenseStatusService.calculateStatus(null);

        assertEquals(LicenseStatus.PENDING, status);
    }

    @Test
    void shouldReturnExpiredWhenExpirationDateIsInThePast() {
        LocalDate expiresAt = LocalDate.now().minusDays(1);

        LicenseStatus status = licenseStatusService.calculateStatus(expiresAt);

        assertEquals(LicenseStatus.EXPIRED, status);
    }

    @Test
    void shouldReturnExpiringSoonWhenExpirationDateIsWithinThirtyDays() {
        LocalDate expiresAt = LocalDate.now().plusDays(15);

        LicenseStatus status = licenseStatusService.calculateStatus(expiresAt);

        assertEquals(LicenseStatus.EXPIRING_SOON, status);
    }

    @Test
    void shouldReturnActiveWhenExpirationDateIsMoreThanThirtyDaysAway() {
        LocalDate expiresAt = LocalDate.now().plusDays(31);

        LicenseStatus status = licenseStatusService.calculateStatus(expiresAt);

        assertEquals(LicenseStatus.ACTIVE, status);
    }
}