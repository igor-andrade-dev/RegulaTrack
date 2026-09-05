import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { LicenseService } from './license';

describe('LicenseService', () => {
  let service: LicenseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(LicenseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
