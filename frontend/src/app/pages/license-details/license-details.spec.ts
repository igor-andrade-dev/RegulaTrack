import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { LicenseDetails } from './license-details';

describe('LicenseDetails', () => {
  let component: LicenseDetails;
  let fixture: ComponentFixture<LicenseDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LicenseDetails],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(LicenseDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
