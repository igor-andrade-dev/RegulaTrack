import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface AppUser {
  id: number;
  username: string;
  email: string;
  role: string;
  permissions: string[];
  createdAt: string;
  updatedAt: string | null;
}
export interface PermissionOption { code: string; module: string; action: string; }
export interface UserPayload { username: string; email: string; password?: string; role: string; permissions: string[]; }

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly api = 'http://localhost:8083/api/users';
  constructor(private readonly http: HttpClient) {}
  findAll() { return this.http.get<AppUser[]>(this.api); }
  findById(id: number) { return this.http.get<AppUser>(`${this.api}/${id}`); }
  permissions() { return this.http.get<PermissionOption[]>(`${this.api}/permissions`); }
  create(payload: UserPayload) { return this.http.post<AppUser>(this.api, payload); }
  update(id: number, payload: UserPayload) { return this.http.put<AppUser>(`${this.api}/${id}`, payload); }
  delete(id: number) { return this.http.delete<void>(`${this.api}/${id}`); }
}
