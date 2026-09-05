import { AsyncPipe } from '@angular/common';
import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BranchService } from '../../services/branch';
import { CompanyService } from '../../services/company';
import { LicenseService, UpdateLicenseRequest } from '../../services/license';
import { LicenseDocument, LicenseDocumentService } from '../../services/license-document';
import { AuthService } from '../../core/services/auth';


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
  private readonly documentService = inject(LicenseDocumentService);
  readonly auth = inject(AuthService);

  companies$ = this.companyService.findAll();
  branches$ = this.branchService.findAll();

  licenseId!: number;
  loading = true;
  saving = false;
  errorMessage = '';
  documents: LicenseDocument[] = [];
  selectedFile: File | null = null;
  uploading = false;
  deletingDocumentId: number | null = null;
  documentMessage = '';

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

    this.loadDocuments();

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

  can(permission: string): boolean {
    return this.auth.hasPermission(permission);
  }

  loadDocuments(): void {
    if (!this.licenseId) return;
    this.documentService.list(this.licenseId).subscribe({
      next: (documents) => { this.documents = documents; this.cdr.detectChanges(); },
      error: () => { this.documentMessage = 'Unable to load documents.'; this.cdr.detectChanges(); }
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
    this.documentService.upload(this.licenseId, this.selectedFile).subscribe({
      next: () => { this.selectedFile = null; this.uploading = false; this.documentMessage = 'Document uploaded successfully.'; this.loadDocuments(); },
      error: (error) => { this.uploading = false; this.documentMessage = error?.error?.message || 'Unable to upload document.'; this.cdr.detectChanges(); }
    });
  }

  viewDocument(document: LicenseDocument): void {
    const popup = window.open('', '_blank');
    this.documentService.content(this.licenseId, document.id).subscribe({
      next: (blob) => { const url = URL.createObjectURL(blob); if (popup) popup.location.href = url; else window.open(url, '_blank'); setTimeout(() => URL.revokeObjectURL(url), 60000); },
      error: () => { popup?.close(); this.documentMessage = 'Unable to open document.'; this.cdr.detectChanges(); }
    });
  }

  downloadDocument(document: LicenseDocument): void {
    this.documentService.download(this.licenseId, document.id).subscribe({
      next: (blob) => { const url = URL.createObjectURL(blob); const a = window.document.createElement('a'); a.href = url; a.download = document.originalName; a.click(); URL.revokeObjectURL(url); },
      error: () => { this.documentMessage = 'Unable to download document.'; this.cdr.detectChanges(); }
    });
  }

  deleteDocument(document: LicenseDocument): void {
    if (!window.confirm(`Delete ${document.originalName}? This action cannot be undone.`) || this.deletingDocumentId) return;
    this.deletingDocumentId = document.id;
    this.documentService.delete(this.licenseId, document.id).subscribe({
      next: () => { this.deletingDocumentId = null; this.loadDocuments(); },
      error: () => { this.deletingDocumentId = null; this.documentMessage = 'Unable to delete document.'; this.cdr.detectChanges(); }
    });
  }

  formatFileSize(size: number): string {
    if (size < 1024) return `${size} B`;
    if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`;
    return `${(size / (1024 * 1024)).toFixed(1)} MB`;
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
