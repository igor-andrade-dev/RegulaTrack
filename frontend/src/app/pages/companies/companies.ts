import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CompanyService } from '../../services/company';

@Component({
  selector: 'app-companies',
  standalone: true,
  imports: [AsyncPipe, RouterLink],
  templateUrl: './companies.html',
  styleUrl: './companies.scss'
})
export class Companies {

  private readonly companyService = inject(CompanyService);

  companies$ = this.companyService.findAll();
  deletingId: number | null = null;
  errorMessage = '';

  deleteCompany(id: number): void {
    const confirmed = window.confirm(
      'Are you sure you want to delete this company? This action cannot be undone.'
    );

    if (!confirmed) {
      return;
    }

    this.deletingId = id;
    this.errorMessage = '';

    this.companyService.delete(id).subscribe({
      next: () => {
        this.deletingId = null;
        this.companies$ = this.companyService.findAll();
      },
      error: (err) => {
        console.error('Delete error:', err);
        this.deletingId = null;
        this.errorMessage = 'Could not delete company.';
      }
    });
  }
}
