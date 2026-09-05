import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { KeyValuePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UserService, PermissionOption, AppUser, UserPayload } from '../../services/user';

@Component({
  selector: 'app-user-edit', standalone: true,
  imports: [FormsModule, RouterLink, KeyValuePipe],
  templateUrl: './user-edit.html', styleUrl: './user-edit.scss',
})
export class UserEdit implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly service = inject(UserService);
  private readonly cdr = inject(ChangeDetectorRef);

  userId = 0;
  options: PermissionOption[] = [];
  grouped: Record<string, PermissionOption[]> = {};
  user: AppUser | null = null;
  loading = true;
  saving = false;
  errorMessage = '';
  form: UserPayload = { username: '', email: '', password: '', role: 'USER', permissions: [] };

  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    if (!this.userId) { this.loading = false; this.errorMessage = 'Invalid user.'; this.cdr.detectChanges(); return; }

    this.service.permissions().subscribe({
      next: (permissions) => {
        this.options = permissions;
        this.grouped = this.group(permissions);
        this.service.findById(this.userId).subscribe({
          next: (user) => {
            this.user = user;
            this.form = { username: user.username, email: user.email, password: '', role: user.role, permissions: [...user.permissions] };
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: (error) => this.fail(error, 'Unable to load user.'),
        });
      },
      error: (error) => this.fail(error, 'Unable to load permissions.'),
    });
  }

  group(options: PermissionOption[]): Record<string, PermissionOption[]> {
    return options.reduce((result, option) => { (result[option.module] ??= []).push(option); return result; }, {} as Record<string, PermissionOption[]>);
  }

  has(permission: string): boolean { return this.form.permissions.includes(permission); }
  toggle(permission: string, checked: boolean): void { this.form.permissions = checked ? [...new Set([...this.form.permissions, permission])] : this.form.permissions.filter((current) => current !== permission); }

  submit(): void {
    if (!this.user || this.saving) return;
    this.saving = true;
    this.errorMessage = '';
    const request: UserPayload = { username: this.form.username, email: this.form.email, password: this.form.password, role: this.form.role, permissions: [...this.form.permissions] };
    this.service.update(this.userId, request).subscribe({
      next: () => { this.saving = false; this.router.navigate(['/users']); },
      error: (error) => this.fail(error, 'Unable to save changes.'),
    });
  }

  private fail(error: any, fallback: string): void {
    console.error('User edit error:', error);
    this.loading = false;
    this.saving = false;
    this.errorMessage = error?.error?.message || fallback;
    this.cdr.detectChanges();
  }
}
