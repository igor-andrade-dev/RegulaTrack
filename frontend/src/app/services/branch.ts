import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

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

@Injectable({
  providedIn: 'root'
})
export class BranchService {
  private readonly apiUrl = 'http://localhost:8080/api/branches';

  constructor(private readonly http: HttpClient) {}

  findAll(companyId?: number) {
    const options = companyId != null
      ? { params: new HttpParams().set('companyId', companyId) }
      : {};

    return this.http.get<Branch[]>(this.apiUrl, options);
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
