import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { LicenseEdit } from './license-edit';

describe('LicenseEdit', () => {
  let component: LicenseEdit;
  let fixture: ComponentFixture<LicenseEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LicenseEdit],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(LicenseEdit);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
