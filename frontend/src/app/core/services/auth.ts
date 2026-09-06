import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface CurrentUser {
  id: number;
  username: string;
  role: 'ADMIN' | 'USER' | string;
  permissions: string[];
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly api = environment.apiUrl;

  private readonly userSubject =
    new BehaviorSubject<CurrentUser | null>(this.readStoredUser());

  readonly user$ = this.userSubject.asObservable();

  constructor(private readonly http: HttpClient) {}

  login(username: string, password: string) {
    return this.http
      .post<{ token: string }>(
        `${this.api}/auth/login`,
        { username, password }
      )
      .pipe(
        tap(res => localStorage.setItem('token', res.token))
      );
  }

  loadCurrentUser(): Observable<CurrentUser> {
    return this.http
      .get<CurrentUser>(`${this.api}/auth/me`)
      .pipe(
        tap(user => {
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.userSubject.next(user);
        })
      );
  }

  requestPasswordReset(email: string) {
    return this.http.post<{ message: string }>(
      `${this.api}/auth/password-reset/request`,
      { email }
    );
  }

  resetPassword(token: string, password: string) {
    return this.http.post<{ message: string }>(
      `${this.api}/auth/password-reset/confirm`,
      { token, password }
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.userSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  isAdmin(): boolean {
    return this.userSubject.value?.role === 'ADMIN';
  }

  hasPermission(permission: string): boolean {
    const user = this.userSubject.value;

    return user?.role === 'ADMIN'
      || !!user?.permissions?.includes(permission);
  }

  get currentUser(): CurrentUser | null {
    return this.userSubject.value;
  }

  private readStoredUser(): CurrentUser | null {
    try {
      const raw = localStorage.getItem('currentUser');
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  }
}
