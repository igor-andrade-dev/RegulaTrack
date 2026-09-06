import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from '../../environments/environment';

export interface DashboardSummary {
  totalCompanies: number;
  totalBranches: number;
  totalLicenses: number;
  activeLicenses: number;
  expiringSoonLicenses: number;
  expiredLicenses: number;
  pendingLicenses: number;
}

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private readonly apiUrl =
    `${environment.apiUrl}/api/dashboard/summary`;

  constructor(private readonly http: HttpClient) {}

  getSummary() {
    return this.http.get<DashboardSummary>(this.apiUrl);
  }
}
