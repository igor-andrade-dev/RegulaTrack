import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CompanyService } from '../../services/company';

@Component({
  selector: 'app-companies',
  standalone: true,
  imports: [AsyncPipe, RouterLink, FormsModule],
  templateUrl: './companies.html',
  styleUrl: './companies.scss',
})
export class Companies {
  private readonly companyService = inject(CompanyService);

  companies$ = this.companyService.findAll();

  deletingId: number | null = null;

  filters = {
    name: ''
  };

  errorMessage = '';

  search(): void {
    this.companies$ = this.companyService.findAll({
      name: this.filters.name || undefined
    });
  }

  clearFilters(): void {
    this.filters = { name: '' };
    this.companies$ = this.companyService.findAll();
  }

  deleteCompany(id: number): void {
    const confirmed = window.confirm('Are you sure?');
    if (!confirmed) return;

    this.deletingId = id;

    this.companyService.delete(id).subscribe({
      next: () => {
        this.deletingId = null;
        this.search();
      },
      error: () => {
        this.deletingId = null;
        this.errorMessage = 'Could not delete company.';
      }
    });
  }
}

