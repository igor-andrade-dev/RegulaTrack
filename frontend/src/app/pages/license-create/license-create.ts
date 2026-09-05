import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { BranchService } from '../../services/branch';
import { CompanyService } from '../../services/company';
import { CreateLicenseRequest, LicenseService } from '../../services/license';

@Component({
  selector: 'app-license-create',
  imports: [AsyncPipe, FormsModule, RouterLink],
  templateUrl: './license-create.html',
  styleUrl: './license-create.scss'
})
export class LicenseCreate {
  private readonly licenseService = inject(LicenseService);
  private readonly companyService = inject(CompanyService);
  private readonly branchService = inject(BranchService);
  private readonly router = inject(Router);

  companies$ = this.companyService.findAll();
  branches$ = this.branchService.findAll();

  loading = false;
  errorMessage = '';

  form: CreateLicenseRequest = {
    companyId: null,
    branchId: null,
    name: '',
    description: '',
    category: '',
    licenseNumber: '',
    issuer: '',
    issuedAt: '',
    expiresAt: null,
    responsibleName: '',
    responsibleEmail: '',
    notes: ''
  };

  submit(): void {
    this.loading = true;
    this.errorMessage = '';

    const request: CreateLicenseRequest = {
      ...this.form,
      expiresAt: this.form.expiresAt || null
    };

    this.licenseService.create(request).subscribe({
      next: (license) => {
        this.loading = false;
        this.router.navigate(['/licenses', license.id]);
      },
      error: (error) => {
        console.error('Create license error:', error);
        this.loading = false;
        this.errorMessage = 'Could not create license. Please check the form data.';
      }
    });
  }
}
