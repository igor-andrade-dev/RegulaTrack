import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { LicenseService } from '../../services/license';

@Component({
  selector: 'app-licenses',
  imports: [AsyncPipe, FormsModule, RouterLink],
  templateUrl: './licenses.html',
  styleUrl: './licenses.scss'
})
export class Licenses {
  private readonly licenseService = inject(LicenseService);

  deletingId: number | null = null;

  filters = {
    status: '',
    category: '',
    name: ''
  };

  licensesResponse$ = this.licenseService.searchLicenses();

  search(): void {
    this.licensesResponse$ = this.licenseService.searchLicenses({
      status: this.filters.status || undefined,
      category: this.filters.category || undefined,
      name: this.filters.name || undefined,
      page: 0,
      size: 10
    });
  }

  clearFilters(): void {
    this.filters = {
      status: '',
      category: '',
      name: ''
    };

    this.licensesResponse$ = this.licenseService.searchLicenses();
  }

  deleteLicense(id: number): void {
    const confirmed = window.confirm(
      'Are you sure you want to delete this license? This action cannot be undone.'
    );

    if (!confirmed) {
      return;
    }

    this.deletingId = id;

    this.licenseService.deleteById(id).subscribe({
      next: () => {
        this.deletingId = null;
        this.search();
      },
      error: (error) => {
        console.error('Delete license error:', error);
        this.deletingId = null;
        alert('Could not delete license.');
      }
    });
  }

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
}
