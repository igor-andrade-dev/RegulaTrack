import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CompanyService, CreateCompanyRequest } from '../../services/company';

@Component({
  selector: 'app-company-create',
  imports: [FormsModule, RouterLink],
  templateUrl: './company-create.html',
  styleUrl: './company-create.scss'
})
export class CompanyCreate {
  private readonly companyService = inject(CompanyService);
  private readonly router = inject(Router);

  loading = false;
  errorMessage = '';

  form: CreateCompanyRequest = {
    name: '',
    documentNumber: '',
    segment: '',
    country: '',
    city: ''
  };

  submit(): void {
    this.loading = true;
    this.errorMessage = '';

    this.companyService.create(this.form).subscribe({
      next: (company) => {
        this.loading = false;
        this.router.navigate(['/companies', company.id]);
      },
      error: (error) => {
        console.error('Create company error:', error);
        this.loading = false;
        this.errorMessage = 'Could not create company. Please check the form data.';
      }
    });
  }
}
