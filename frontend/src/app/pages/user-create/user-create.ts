import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { KeyValuePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UserService, PermissionOption, UserPayload } from '../../services/user';

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [FormsModule, RouterLink, KeyValuePipe],
  templateUrl: './user-create.html',
  styleUrl: './user-create.scss',
})
export class UserCreate implements OnInit {
  private readonly service = inject(UserService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  options: PermissionOption[] = [];
  grouped: Record<string, PermissionOption[]> = {};
  loading = false;
  errorMessage = '';

  form: UserPayload = {
    username: '',
    email: '',
    password: '',
    role: 'USER',
    permissions: [],
  };

  ngOnInit(): void {
    this.service.permissions().subscribe({
      next: (options) => {
        this.options = options;
        this.grouped = this.group(options);
        this.cdr.detectChanges();
      },
      error: (error) => this.fail(error),
    });
  }

  group(options: PermissionOption[]): Record<string, PermissionOption[]> {
    return options.reduce((result, option) => {
      (result[option.module] ??= []).push(option);
      return result;
    }, {} as Record<string, PermissionOption[]>);
  }

  has(code: string): boolean {
    return this.form.permissions.includes(code);
  }

  toggle(code: string, checked: boolean): void {
    this.form.permissions = checked
      ? [...new Set([...this.form.permissions, code])]
      : this.form.permissions.filter((permission) => permission !== code);
  }

  submit(): void {
    if (this.loading) return;

    this.errorMessage = '';

    if (!this.form.username.trim()) {
      this.errorMessage = 'Username is required.';
      return;
    }

    if (!this.form.email.trim()) {
      this.errorMessage = 'Email is required.';
      return;
    }

    if (!this.form.password?.trim()) {
      this.errorMessage = 'Password is required.';
      return;
    }

    if (this.form.password.length < 6) {
      this.errorMessage = 'Password must contain at least 6 characters.';
      return;
    }

    this.loading = true;

    this.service.create(this.form).subscribe({
      next: (user) => {
        this.loading = false;
        this.cdr.detectChanges();
        this.router.navigate(['/users', user.id, 'edit']);
      },
      error: (error) => this.fail(error),
    });
  }

  private fail(error: any): void {
    console.error('User create error:', error);
    this.loading = false;
    this.errorMessage = error?.error?.message || 'Unable to save user.';
    this.cdr.detectChanges();
  }
}
