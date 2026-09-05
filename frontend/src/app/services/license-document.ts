import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface LicenseDocument {
  id: number;
  originalName: string;
  contentType: string;
  size: number;
  uploadedAt: string;
}

@Injectable({ providedIn: 'root' })
export class LicenseDocumentService {
  private readonly baseUrl = 'http://localhost:8083/api/licenses';

  constructor(private readonly http: HttpClient) {}

  list(licenseId: number) {
    return this.http.get<LicenseDocument[]>(
      `${this.baseUrl}/${licenseId}/documents`
    );
  }

  upload(licenseId: number, file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<LicenseDocument>(
      `${this.baseUrl}/${licenseId}/documents`,
      formData
    );
  }

  content(licenseId: number, documentId: number) {
    return this.http.get(
      `${this.baseUrl}/${licenseId}/documents/${documentId}/content`,
      { responseType: 'blob' }
    );
  }

  download(licenseId: number, documentId: number) {
    return this.http.get(
      `${this.baseUrl}/${licenseId}/documents/${documentId}/download`,
      { responseType: 'blob' }
    );
  }

  delete(licenseId: number, documentId: number) {
    return this.http.delete<void>(
      `${this.baseUrl}/${licenseId}/documents/${documentId}`
    );
  }
}
