import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { map, switchMap } from 'rxjs';
import { CompanyService } from '../../services/company';

@Component({
  selector: 'app-company-details',
  imports: [AsyncPipe, RouterLink],
  templateUrl: './company-details.html',
  styleUrl: './company-details.scss'
})
export class CompanyDetails {
  private readonly route = inject(ActivatedRoute);
  private readonly companyService = inject(CompanyService);
  private readonly router = inject(Router);

  deleting = false;
  errorMessage = '';

  company$ = this.route.paramMap.pipe(
    map((params) => Number(params.get('id'))),
    switchMap((id) => this.companyService.findById(id))
  );


  deleteCompany(id: number): void {
    const confirmed = window.confirm(
      'Are you sure you want to delete this company? This action cannot be undone.'
    );

    if (!confirmed) return;

    this.deleting = true;
    this.errorMessage = '';

    this.companyService.delete(id).subscribe({
      next: () => {
        this.deleting = false;
        this.router.navigate(['/companies']);
      },
      error: (error) => {
        console.error('Delete company error:', error);
        this.deleting = false;
        this.errorMessage = 'Could not delete company.';
      }
    });
  }
}
