import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { BranchService, CreateBranchRequest } from '../../services/branch';
import { CompanyService } from '../../services/company';

@Component({
  selector: 'app-branch-create',
  imports: [AsyncPipe, FormsModule, RouterLink],
  templateUrl: './branch-create.html',
  styleUrl: './branch-create.scss'
})
export class BranchCreate {
  private readonly branchService = inject(BranchService);
  private readonly companyService = inject(CompanyService);
  private readonly router = inject(Router);

  companies$ = this.companyService.findAll();

  loading = false;
  errorMessage = '';

  form: CreateBranchRequest = {
    companyId: null,
    name: '',
    address: '',
    city: '',
    state: '',
    country: ''
  };

  submit(): void {
    this.loading = true;
    this.errorMessage = '';

    this.branchService.create(this.form).subscribe({
      next: (branch) => {
        this.loading = false;
        this.router.navigate(['/branches', branch.id]);
      },
      error: (error) => {
        console.error('Create branch error:', error);
        this.loading = false;
        this.errorMessage = 'Could not create branch. Please check the form data.';
      }
    });
  }
}
