import { AsyncPipe } from '@angular/common';
import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BranchService } from '../../services/branch';
import { CompanyService } from '../../services/company';
import { LicenseService, UpdateLicenseRequest } from '../../services/license';

@Component({
  selector: 'app-license-edit',
  imports: [AsyncPipe, FormsModule, RouterLink],
  templateUrl: './license-edit.html',
  styleUrl: './license-edit.scss'
})
export class LicenseEdit implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly licenseService = inject(LicenseService);
  private readonly companyService = inject(CompanyService);
  private readonly branchService = inject(BranchService);
  private readonly cdr = inject(ChangeDetectorRef);

  companies$ = this.companyService.findAll();
  branches$ = this.branchService.findAll();

  licenseId!: number;
  loading = true;
  saving = false;
  errorMessage = '';

  form: UpdateLicenseRequest = {
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

  ngOnInit(): void {
    this.licenseId = Number(this.route.snapshot.paramMap.get('id'));

    this.licenseService.findById(this.licenseId).subscribe({
      next: (license) => {
        this.form = {
          companyId: license.companyId,
          branchId: license.branchId,
          name: license.name,
          description: license.description,
          category: license.category,
          licenseNumber: license.licenseNumber,
          issuer: license.issuer,
          issuedAt: license.issuedAt,
          expiresAt: license.expiresAt,
          responsibleName: license.responsibleName,
          responsibleEmail: license.responsibleEmail,
          notes: license.notes
        };

        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Load license error:', error);

        this.errorMessage = 'Could not load license data.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  submit(): void {
    this.saving = true;
    this.errorMessage = '';

    const request: UpdateLicenseRequest = {
      ...this.form,
      expiresAt: this.form.expiresAt || null
    };

    this.licenseService.update(this.licenseId, request).subscribe({
      next: (license) => {
        this.saving = false;
        this.router.navigate(['/licenses', license.id]);
      },
      error: (error) => {
        console.error('Update license error:', error);
        this.saving = false;
        this.errorMessage = 'Could not update license. Please check the form data.';
      }
    });
  }
}
