import { AsyncPipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { BranchService, UpdateBranchRequest } from '../../services/branch';
import { CompanyService } from '../../services/company';

@Component({
  selector: 'app-branch-edit',
  standalone: true,
  imports: [AsyncPipe, FormsModule, RouterLink],
  templateUrl: './branch-edit.html',
  styleUrl: './branch-edit.scss',
})
export class BranchEdit implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly branchService = inject(BranchService);
  private readonly companyService = inject(CompanyService);
  private readonly cdr = inject(ChangeDetectorRef);

  companies$ = this.companyService.findAll();

  branchId!: number;

  loading = true;
  saving = false;

  errorMessage = '';

  form: UpdateBranchRequest = {
    companyId: null,
    name: '',
    address: '',
    city: '',
    state: '',
    country: '',
  };

  ngOnInit(): void {
    this.branchId = Number(this.route.snapshot.paramMap.get('id'));

    this.branchService.findById(this.branchId).subscribe({
      next: (branch) => {
        this.form = {
          companyId: branch.companyId,
          name: branch.name,
          address: branch.address,
          city: branch.city,
          state: branch.state,
          country: branch.country,
        };

        this.loading = false;
        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('Load branch error:', error);

        this.errorMessage = 'Could not load branch data.';
        this.loading = false;

        this.cdr.detectChanges();
      },
    });
  }

  submit(): void {
    this.saving = true;
    this.errorMessage = '';

    const request: UpdateBranchRequest = {
      companyId: this.form.companyId,
      name: this.form.name,
      address: this.form.address,
      city: this.form.city,
      state: this.form.state,
      country: this.form.country,
    };

    this.branchService.update(this.branchId, request).subscribe({
      next: (branch) => {
        this.saving = false;
        this.router.navigate(['/branches', branch.id]);
      },

      error: (error) => {
        console.error('Update branch error:', error);

        this.saving = false;
        this.errorMessage = 'Could not update branch. Please check the form data.';
      },
    });
  }
}
