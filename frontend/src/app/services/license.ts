import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface License {
  id: number;
  companyId: number;
  branchId: number;
  name: string;
  description: string;
  category: string;
  licenseNumber: string;
  issuer: string;
  issuedAt: string;
  expiresAt: string | null;
  responsibleName: string;
  responsibleEmail: string;
  status: 'ACTIVE' | 'EXPIRING_SOON' | 'EXPIRED' | 'PENDING';
  notes: string;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface LicenseSearchFilters {
  status?: string;
  category?: string;
  name?: string;
  page?: number;
  size?: number;
}
export interface CreateLicenseRequest {
  companyId: number | null;
  branchId: number | null;
  name: string;
  description: string;
  category: string;
  licenseNumber: string;
  issuer: string;
  issuedAt: string;
  expiresAt: string | null;
  responsibleName: string;
  responsibleEmail: string;
  notes: string;
}

export interface UpdateLicenseRequest {
  companyId: number | null;
  branchId: number | null;
  name: string;
  description: string;
  category: string;
  licenseNumber: string;
  issuer: string;
  issuedAt: string;
  expiresAt: string | null;
  responsibleName: string;
  responsibleEmail: string;
  notes: string;
}

@Injectable({
  providedIn: 'root'
})
export class LicenseService {
  private readonly apiUrl = 'http://localhost:8080/api/licenses';


  constructor(private readonly http: HttpClient) {}



  searchLicenses(filters: LicenseSearchFilters = {}) {
    let params = new HttpParams()
      .set('page', filters.page ?? 0)
      .set('size', filters.size ?? 10);

    if (filters.status) {
      params = params.set('status', filters.status);
    }

    if (filters.category) {
      params = params.set('category', filters.category);
    }

    if (filters.name) {
      params = params.set('name', filters.name);
    }

    return this.http.get<PageResponse<License>>(`${this.apiUrl}/search`, {
      params
    });
  }

  findById(id: number) {
    return this.http.get<License>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateLicenseRequest) {
    return this.http.post<License>(this.apiUrl, request);
  }

  update(id: number, request: UpdateLicenseRequest) {
    return this.http.put<License>(`${this.apiUrl}/${id}`, request);
  }

  deleteById(id: number) {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
