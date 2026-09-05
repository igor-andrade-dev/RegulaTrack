import { Component, inject } from '@angular/core';
import {
  Router,
  RouterLink,
  RouterLinkActive,
  RouterOutlet,
} from '@angular/router';

import { AuthService } from '../../core/services/auth';
import { ThemeService } from '../../core/services/theme';

@Component({
  selector: 'app-app-layout',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './app-layout.html',
  styleUrl: './app-layout.scss',
})
export class AppLayout {
  readonly auth = inject(AuthService);
  readonly theme = inject(ThemeService);

  private readonly router = inject(Router);

  sidebarOpen = false;

  get currentUser() {
    return this.auth.currentUser;
  }

  get userInitials(): string {
    const username = this.currentUser?.username?.trim() || 'U';

    return username
      .split(/\s+/)
      .slice(0, 2)
      .map((name) => name.charAt(0).toUpperCase())
      .join('');
  }

  get userRole(): string {
    return this.currentUser?.role === 'ADMIN' ? 'Administrator' : 'User';
  }

  can(permission: string): boolean {
    return this.auth.hasPermission(permission);
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
  }

  closeSidebar(): void {
    this.sidebarOpen = false;
  }

  toggleTheme(): void {
    this.theme.toggle();
  }

  logout(): void {
    this.auth.logout();
    this.sidebarOpen = false;
    this.router.navigate(['/login']);
  }
}
