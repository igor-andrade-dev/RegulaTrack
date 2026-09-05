import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { BranchService } from '../../services/branch';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-branches',
  standalone: true,
  imports: [AsyncPipe, RouterLink, FormsModule],
  templateUrl: './branches.html',
  styleUrls: ['./branches.scss'],
})
export class Branches {
  readonly auth = inject(AuthService);
  private readonly branchService = inject(BranchService);

  branches$ = this.branchService.findAll();
  deletingId: number | null = null;
  errorMessage = '';

  filters = { name: '' };

  can(permission: string): boolean { return this.auth.hasPermission(permission); }

  refresh(): void { this.branches$ = this.branchService.findAll(); }
  search(): void { this.branches$ = this.branchService.findAll({ name: this.filters.name || undefined }); }
  clearFilters(): void { this.filters = { name: '' }; this.refresh(); }

  deleteBranch(id: number): void {
    if (!window.confirm('Are you sure you want to delete this branch? This action cannot be undone.')) return;
    this.deletingId = id;
    this.errorMessage = '';
    this.branchService.delete(id).subscribe({
      next: () => { this.deletingId = null; this.refresh(); },
      error: (error) => { console.error('Delete branch error:', error); this.deletingId = null; this.errorMessage = error?.error?.message || 'Unable to delete branch.'; },
    });
  }
}
