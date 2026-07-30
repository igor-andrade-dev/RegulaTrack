import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { BranchService } from '../../services/branch';

@Component({
  selector: 'app-branches',
  standalone: true,
  imports: [AsyncPipe, RouterLink, FormsModule],
  templateUrl: './branches.html',
  styleUrls: ['./branches.scss'],
})
export class Branches {
  private readonly branchService = inject(BranchService);

  branches$ = this.branchService.findAll();

  deletingId: number | null = null;

  errorMessage = '';

  // 🔥 NECESSÁRIO pro ngModel
  filters = {
    name: '',
  };

  refresh(): void {
    this.branches$ = this.branchService.findAll();
  }

  search(): void {
    this.branches$ = this.branchService.findAll({
      name: this.filters.name || undefined,
    });
  }

  clearFilters(): void {
    this.filters = { name: '' };
    this.refresh();
  }

  deleteBranch(id: number): void {
    const confirmed = window.confirm(
      'Are you sure you want to delete this branch? This action cannot be undone.',
    );

    if (!confirmed) return;

    this.deletingId = id;
    this.errorMessage = '';

    this.branchService.delete(id).subscribe({
      next: () => {
        this.deletingId = null;
        this.refresh();
      },
      error: (error) => {
        console.error('Delete branch error:', error);
        this.deletingId = null;
        this.errorMessage = 'Could not delete branch.';
      },
    });
  }
}
