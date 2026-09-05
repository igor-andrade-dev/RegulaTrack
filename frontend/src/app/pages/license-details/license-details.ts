import { AsyncPipe, DatePipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { map, switchMap } from 'rxjs';
import { LicenseService } from '../../services/license';
import { LicenseDocument, LicenseDocumentService } from '../../services/license-document';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-license-details',
  standalone: true,
  imports: [AsyncPipe, DatePipe, RouterLink],
  templateUrl: './license-details.html',
  styleUrl: './license-details.scss'
})
export class LicenseDetails implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly licenseService = inject(LicenseService);
  private readonly documentService = inject(LicenseDocumentService);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  licenseId = 0;
  documents: LicenseDocument[] = [];
  selectedFile: File | null = null;
  uploading = false;
  deleting = false;
  deletingDocumentId: number | null = null;
  errorMessage = '';
  documentMessage = '';

  license$ = this.route.paramMap.pipe(
    map((params) => Number(params.get('id'))),
    switchMap((id) => this.licenseService.findById(id))
  );

  ngOnInit(): void {
    this.licenseId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadDocuments();
  }

  can(permission: string): boolean {
    return this.auth.hasPermission(permission);
  }

  loadDocuments(): void {
    if (!this.licenseId) return;
    this.documentService.list(this.licenseId).subscribe({
      next: (documents) => {
        this.documents = documents;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Load documents error:', error);
        this.documentMessage = 'Unable to load documents.';
        this.cdr.detectChanges();
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedFile = input.files?.[0] ?? null;
    this.documentMessage = '';
  }

  uploadDocument(): void {
    if (!this.selectedFile || this.uploading) return;
    this.uploading = true;
    this.documentMessage = '';
    this.documentService.upload(this.licenseId, this.selectedFile).subscribe({
      next: () => {
        this.selectedFile = null;
        this.uploading = false;
        this.documentMessage = 'Document uploaded successfully.';
        this.loadDocuments();
      },
      error: (error) => {
        console.error('Upload document error:', error);
        this.uploading = false;
        this.documentMessage = error?.error?.message || 'Unable to upload document.';
        this.cdr.detectChanges();
      }
    });
  }

  viewDocument(document: LicenseDocument): void {
    const popup = window.open('', '_blank');
    this.documentService.content(this.licenseId, document.id).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        if (popup) popup.location.href = url; else window.open(url, '_blank');
        setTimeout(() => URL.revokeObjectURL(url), 60_000);
      },
      error: (error) => {
        popup?.close();
        console.error('View document error:', error);
        this.documentMessage = 'Unable to open document.';
        this.cdr.detectChanges();
      }
    });
  }

  downloadDocument(document: LicenseDocument): void {
    this.documentService.download(this.licenseId, document.id).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const anchor = window.document.createElement('a');
        anchor.href = url;
        anchor.download = document.originalName;
        anchor.click();
        URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Download document error:', error);
        this.documentMessage = 'Unable to download document.';
        this.cdr.detectChanges();
      }
    });
  }

  deleteDocument(document: LicenseDocument): void {
    const confirmed = window.confirm(`Delete ${document.originalName}? This action cannot be undone.`);
    if (!confirmed || this.deletingDocumentId) return;
    this.deletingDocumentId = document.id;
    this.documentService.delete(this.licenseId, document.id).subscribe({
      next: () => {
        this.deletingDocumentId = null;
        this.loadDocuments();
      },
      error: (error) => {
        console.error('Delete document error:', error);
        this.deletingDocumentId = null;
        this.documentMessage = error?.error?.message || 'Unable to delete document.';
        this.cdr.detectChanges();
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

  formatFileSize(size: number): string {
    if (size < 1024) return `${size} B`;
    if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`;
    return `${(size / (1024 * 1024)).toFixed(1)} MB`;
  }

  deleteLicense(id: number): void {
    const confirmed = window.confirm('Are you sure you want to delete this license? This action cannot be undone.');
    if (!confirmed) return;
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
        this.cdr.detectChanges();
      }
    });
  }
}
