import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { CompanyCreate } from './company-create';

describe('CompanyCreate', () => {
  let component: CompanyCreate;
  let fixture: ComponentFixture<CompanyCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompanyCreate],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(CompanyCreate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
