import { Routes } from '@angular/router';

import { Login } from './features/auth/login/login';
import { AuthGuard } from './core/guards/auth-guard';

import { AppLayout } from './layout/app-layout/app-layout';

import { Dashboard } from './pages/dashboard/dashboard';
import { Licenses } from './pages/licenses/licenses';
import { Companies } from './pages/companies/companies';
import { Branches } from './pages/branches/branches';

import { LicenseDetails } from './pages/license-details/license-details';
import { LicenseCreate } from './pages/license-create/license-create';
import { LicenseEdit } from './pages/license-edit/license-edit';

import { CompanyCreate } from './pages/companies-create/company-create';
import { CompanyDetails } from './pages/companies-details/company-details';
import { CompanyEdit } from './pages/companies-edit/company-edit';

import { BranchCreate } from './pages/branches-create/branch-create';
import { BranchDetails } from './pages/branches-details/branch-details';
import { BranchEdit } from './pages/branches-edit/branch-edit';

export const routes: Routes = [
  // 🔐 LOGIN FORA DO LAYOUT
  {
    path: 'login',
    component: Login,
  },

  // 🧱 APP COM LAYOUT + PROTEÇÃO
  {
    path: '',
    component: AppLayout,
    canActivate: [AuthGuard],
    children: [
      // DASHBOARD
      {
        path: 'dashboard',
        component: Dashboard,
      },

      // LICENSES
      {
        path: 'licenses',
        component: Licenses,
      },
      {
        path: 'licenses/new',
        component: LicenseCreate,
      },
      {
        path: 'licenses/:id/edit',
        component: LicenseEdit,
      },
      {
        path: 'licenses/:id',
        component: LicenseDetails,
      },

      // COMPANIES
      {
        path: 'companies',
        component: Companies,
      },
      {
        path: 'companies/new',
        component: CompanyCreate,
      },
      {
        path: 'companies/:id/edit',
        component: CompanyEdit,
      },
      {
        path: 'companies/:id',
        component: CompanyDetails,
      },

      // BRANCHES
      {
        path: 'branches',
        component: Branches,
      },
      {
        path: 'branches/new',
        component: BranchCreate,
      },
      {
        path: 'branches/:id/edit',
        component: BranchEdit,
      },
      {
        path: 'branches/:id',
        component: BranchDetails,
      },
    ],
  },

  // 🚨 fallback
  {
    path: '**',
    redirectTo: 'login',
  },
];
