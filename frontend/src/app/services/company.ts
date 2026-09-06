import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from '../../environments/environment';

export interface Company {
  id: number;
  name: string;
  documentNumber: string;
  segment: string;
  country: string;
  city: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateCompanyRequest {
  name: string;
  documentNumber: string;
  segment: string;
  country: string;
  city: string;
}

export interface UpdateCompanyRequest extends CreateCompanyRequest {}

export interface CompanySearchParams {
  name?: string;
}

@Injectable({
  providedIn: 'root',
})
export class CompanyService {
  private readonly apiUrl = `${environment.apiUrl}/api/companies`;

  constructor(private readonly http: HttpClient) {}

  findAll(params?: CompanySearchParams) {
    let httpParams = new HttpParams();

    if (params?.name) {
      httpParams = httpParams.set('name', params.name);
    }

    return this.http.get<Company[]>(this.apiUrl, { params: httpParams });
  }

  findById(id: number) {
    return this.http.get<Company>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateCompanyRequest) {
    return this.http.post<Company>(this.apiUrl, request);
  }

  update(id: number, request: UpdateCompanyRequest) {
    return this.http.put<Company>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
