import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.scss',
})
export class ResetPassword implements OnInit {
  password = '';
  confirmPassword = '';
  loading = false;
  success = false;
  showPassword = false;
  showConfirmPassword = false;
  errorMessage = '';
  token = '';

  private readonly auth = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  ngOnInit(): void {
    // New links use a URL fragment so the token is not sent in the HTTP request.
    // Keep query-string support for reset links issued by earlier versions.
    const fragment = this.route.snapshot.fragment || '';
    const fragmentToken = new URLSearchParams(fragment).get('token') || '';
    this.token = fragmentToken || this.route.snapshot.queryParamMap.get('token') || '';

    // Remove the one-time token from the address bar and the current history entry.
    if (this.token) {
      this.router.navigate([], {
        relativeTo: this.route,
        queryParams: {},
        fragment: undefined,
        replaceUrl: true,
      });
    } else {
      this.errorMessage = 'This reset link is missing or invalid. Request a new one.';
    }
  }

  submit(): void {
    if (this.loading) return;
    this.errorMessage = '';

    if (!this.token) {
      this.errorMessage = 'This reset link is missing or invalid. Request a new one.';
      return;
    }
    if (this.password.length < 12) {
      this.errorMessage = 'Use at least 12 characters for your new password.';
      return;
    }
    if (this.password.length > 128) {
      this.errorMessage = 'Use no more than 128 characters.';
      return;
    }
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'The passwords do not match.';
      return;
    }

    this.loading = true;
    this.auth.resetPassword(this.token, this.password).subscribe({
      next: () => {
        this.loading = false;
        this.success = true;
        this.auth.logout();
        this.token = '';
        setTimeout(() => this.router.navigate(['/login']), 1600);
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'The reset link may have expired or already been used. Request a new link and try again.';
      },
    });
  }
}
