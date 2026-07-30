import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CompanyService, UpdateCompanyRequest } from '../../services/company';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-company-edit',
  imports: [FormsModule, RouterLink],
  templateUrl: './company-edit.html',
  styleUrl: './company-edit.scss',
})
export class CompanyEdit implements OnInit {
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly companyService = inject(CompanyService);

  companyId!: number;
  loading = true;
  saving = false;
  errorMessage = '';

  form: UpdateCompanyRequest = {
    name: '',
    documentNumber: '',
    segment: '',
    country: '',
    city: '',
  };

  ngOnInit(): void {
    this.companyId = Number(this.route.snapshot.paramMap.get('id'));

    this.companyService.findById(this.companyId).subscribe({
      next: (company) => {
        this.form = {
          name: company.name,
          documentNumber: company.documentNumber,
          segment: company.segment,
          country: company.country,
          city: company.city,
        };

        this.loading = false;
        this.cdr.detectChanges(); // 🔥 ESSENCIAL
      },

      error: (error) => {
        console.error(error);

        this.errorMessage = 'Could not load company data.';
        this.loading = false;
        this.cdr.detectChanges(); // 🔥 também aqui
      },
    });
  }

  submit(): void {
    console.log('FORM OBJECT:', this.form);

    this.saving = true;
    this.errorMessage = '';

    const request: UpdateCompanyRequest = {
      name: this.form.name || '',
      documentNumber: this.form.documentNumber || '',
      segment: this.form.segment || '',
      country: this.form.country || '',
      city: this.form.city || '',
    };

    this.companyService.update(this.companyId, request).subscribe({
      next: (company) => {
        this.saving = false;
        this.router.navigate(['/companies', company.id]);
      },
      error: (error) => {
        console.error('Update company error:', error);
        this.saving = false;
        this.errorMessage = 'Could not update company. Please check the form data.';
      },
    });
  }
}
