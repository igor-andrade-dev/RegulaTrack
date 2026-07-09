import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

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

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private readonly apiUrl = 'http://localhost:8080/api/companies';

  constructor(private readonly http: HttpClient) {}

  findAll() {
    return this.http.get<Company[]>(this.apiUrl);
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
