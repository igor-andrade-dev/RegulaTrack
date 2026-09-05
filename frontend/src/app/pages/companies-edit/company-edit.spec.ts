import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { CompanyEdit } from './company-edit';

describe('CompanyEdit', () => {
  let component: CompanyEdit;
  let fixture: ComponentFixture<CompanyEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompanyEdit],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(CompanyEdit);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
