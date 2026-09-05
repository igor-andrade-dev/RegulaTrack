import { AsyncPipe, DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import { DashboardService, DashboardSummary } from '../../services/dashboard';

import { License, LicenseService } from '../../services/license';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [AsyncPipe, DatePipe, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {
  private readonly dashboardService = inject(DashboardService);

  private readonly licenseService = inject(LicenseService);

  summary$!: ReturnType<DashboardService['getSummary']>;

  recentLicenses$!: ReturnType<LicenseService['searchLicenses']>;

  ngOnInit(): void {
    this.summary$ = this.dashboardService.getSummary();

    this.recentLicenses$ = this.licenseService.searchLicenses({
      page: 0,
      size: 5,
    });
  }

  getTotalStatuses(summary: DashboardSummary): number {
    return (
      summary.activeLicenses +
      summary.expiringSoonLicenses +
      summary.expiredLicenses +
      summary.pendingLicenses
    );
  }

  getPercentage(value: number, total: number): number {
    if (total === 0) {
      return 0;
    }

    return Math.round((value / total) * 100);
  }

  getStatusLabel(status: string): string {
    const labels: Record<string, string> = {
      ACTIVE: 'Active',

      EXPIRING_SOON: 'Expiring Soon',

      EXPIRED: 'Expired',

      PENDING: 'Pending',
    };

    return labels[status] ?? status;
  }

  getStatusClass(status: string): string {
    return status.toLowerCase().replace('_', '-');
  }

  trackLicense(_index: number, license: License): number {
    return license.id;
  }
}
