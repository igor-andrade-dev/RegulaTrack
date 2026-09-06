import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from '../../environments/environment';

export interface Branch {
  id: number;
  companyId: number;
  companyName: string;
  name: string;
  address: string;
  city: string;
  state: string;
  country: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateBranchRequest {
  companyId: number | null;
  name: string;
  address: string;
  city: string;
  state: string;
  country: string;
}

export interface UpdateBranchRequest extends CreateBranchRequest {}

export interface BranchSearchParams {
  companyId?: number;
  name?: string;
  city?: string;
}

@Injectable({
  providedIn: 'root',
})
export class BranchService {
  private readonly apiUrl = `${environment.apiUrl}/api/branches`;

  constructor(private readonly http: HttpClient) {}

  findAll(params?: BranchSearchParams) {
    let httpParams = new HttpParams();

    if (params?.companyId != null) {
      httpParams = httpParams.set('companyId', params.companyId);
    }

    if (params?.name) {
      httpParams = httpParams.set('name', params.name);
    }

    if (params?.city) {
      httpParams = httpParams.set('city', params.city);
    }

    return this.http.get<Branch[]>(this.apiUrl, { params: httpParams });
  }

  findById(id: number) {
    return this.http.get<Branch>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateBranchRequest) {
    return this.http.post<Branch>(this.apiUrl, request);
  }

  update(id: number, request: UpdateBranchRequest) {
    return this.http.put<Branch>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
