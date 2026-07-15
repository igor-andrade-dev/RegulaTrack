import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { BranchService } from '../../services/branch';

@Component({
  selector: 'app-branches',
  imports: [AsyncPipe, RouterLink],
  templateUrl: './branches.html',
  styleUrls: ['./branches.scss'],
})
export class Branches {
  private readonly branchService = inject(BranchService);

  branches$ = this.branchService.findAll();
  deletingId: number | null = null;
  errorMessage = '';

  deleteBranch(id: number): void {
    const confirmed = window.confirm(
      'Are you sure you want to delete this branch? This action cannot be undone.',
    );

    if (!confirmed) {
      return;
    }

    this.deletingId = id;
    this.errorMessage = '';

    this.branchService.delete(id).subscribe({
      next: () => {
        this.deletingId = null;
        this.branches$ = this.branchService.findAll();
      },
      error: (error) => {
        console.error('Delete branch error:', error);
        this.deletingId = null;
        this.errorMessage = 'Could not delete branch.';
      },
    });
  }
}
