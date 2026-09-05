import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { UserService } from '../../services/user';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [AsyncPipe, RouterLink],
  templateUrl: './users.html',
  styleUrl: './users.scss',
})
export class Users {
  private readonly service = inject(UserService);
  readonly auth = inject(AuthService);

  users$ = this.service.findAll();
  deletingId: number | null = null;
  errorMessage = '';

  can(permission: string): boolean {
    return this.auth.hasPermission(permission);
  }

  reload(): void {
    this.users$ = this.service.findAll();
  }

  deleteUser(id: number): void {
    if (!window.confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
      return;
    }

    this.deletingId = id;
    this.errorMessage = '';

    this.service.delete(id).subscribe({
      next: () => {
        this.deletingId = null;
        this.reload();
      },
      error: (error) => {
        this.deletingId = null;
        this.errorMessage = error?.error?.message || 'Unable to delete user.';
      },
    });
  }
}
