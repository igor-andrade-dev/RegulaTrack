import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { map, switchMap } from 'rxjs';
import { BranchService } from '../../services/branch';

@Component({
  selector: 'app-branch-details',
  imports: [AsyncPipe, RouterLink],
  templateUrl: './branch-details.html',
  styleUrl: './branch-details.scss'
})
export class BranchDetails {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly branchService = inject(BranchService);

  deleting = false;
  errorMessage = '';

  branch$ = this.route.paramMap.pipe(
    map((params) => Number(params.get('id'))),
    switchMap((id) => this.branchService.findById(id))
  );

  deleteBranch(id: number): void {
    const confirmed = window.confirm(
      'Are you sure you want to delete this branch? This action cannot be undone.'
    );

    if (!confirmed) {
      return;
    }

    this.deleting = true;
    this.errorMessage = '';

    this.branchService.delete(id).subscribe({
      next: () => {
        this.deleting = false;
        this.router.navigate(['/branches']);
      },
      error: (error) => {
        console.error('Delete branch error:', error);
        this.deleting = false;
        this.errorMessage = 'Could not delete branch.';
      }
    });
  }
}
