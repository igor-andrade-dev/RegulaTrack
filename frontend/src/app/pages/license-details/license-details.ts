import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { map, switchMap } from 'rxjs';
import { LicenseService } from '../../services/license';

@Component({
  selector: 'app-license-details',
  imports: [AsyncPipe, RouterLink],
  templateUrl: './license-details.html',
  styleUrl: './license-details.scss'
})
export class LicenseDetails {
  private readonly route = inject(ActivatedRoute);
  private readonly licenseService = inject(LicenseService);
  private readonly router = inject(Router);

  deleting = false;
  errorMessage = '';

  license$ = this.route.paramMap.pipe(
    map((params) => Number(params.get('id'))),
    switchMap((id) => this.licenseService.findById(id))
  );

  getStatusLabel(status: string): string {
    const labels: Record<string, string> = {
      ACTIVE: 'Active',
      EXPIRING_SOON: 'Expiring Soon',
      EXPIRED: 'Expired',
      PENDING: 'Pending'
    };

    return labels[status] ?? status;
  }

  getStatusClass(status: string): string {
    return status.toLowerCase().replace('_', '-');
  }

  deleteLicense(id: number): void {
    const confirmed = window.confirm(
      'Are you sure you want to delete this license? This action cannot be undone.'
    );

    if (!confirmed) {
      return;
    }

    this.deleting = true;
    this.errorMessage = '';

    this.licenseService.deleteById(id).subscribe({
      next: () => {
        this.deleting = false;
        this.router.navigate(['/licenses']);
      },
      error: (error) => {
        console.error('Delete license error:', error);
        this.deleting = false;
        this.errorMessage = 'Could not delete license.';
      }
    });
  }
}
