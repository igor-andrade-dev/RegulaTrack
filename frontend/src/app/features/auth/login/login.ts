import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../../core/services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  username = '';
  password = '';

  loading = false;
  errorMessage = '';

  showPassword = false;

  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  login(): void {
    if (this.loading) {
      return;
    }

    this.errorMessage = '';

    const username = this.username.trim();

    if (!username) {
      this.errorMessage = 'Username is required.';
      return;
    }

    if (!this.password) {
      this.errorMessage = 'Password is required.';
      return;
    }

    this.loading = true;

    this.auth.login(username, this.password).subscribe({
      next: () => {
        this.auth.loadCurrentUser().subscribe({
          next: () => {
            this.router.navigate(['/dashboard']);
          },

          error: () => {
            this.router.navigate(['/dashboard']);
          },
        });
      },

      error: (error) => {
        console.error('Login error:', error);

        this.loading = false;

        this.errorMessage = 'Invalid username or password.';
      },
    });
  }
}
