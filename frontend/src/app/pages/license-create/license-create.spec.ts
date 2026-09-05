import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { LicenseCreate } from './license-create';

describe('LicenseCreate', () => {
  let component: LicenseCreate;
  let fixture: ComponentFixture<LicenseCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LicenseCreate],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(LicenseCreate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
