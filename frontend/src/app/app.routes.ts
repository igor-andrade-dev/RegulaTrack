import { Routes } from '@angular/router';

import { Login } from './features/auth/login/login';
import { ForgotPassword } from './features/auth/forgot-password/forgot-password';
import { ResetPassword } from './features/auth/reset-password/reset-password';
import { AuthGuard } from './core/guards/auth-guard';
import { permissionGuard } from './core/guards/permission-guard';
import { Users } from './pages/users/users';
import { UserCreate } from './pages/user-create/user-create';
import { UserEdit } from './pages/user-edit/user-edit';

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
  { path: 'login', component: Login },
  { path: 'forgot-password', component: ForgotPassword },
  { path: 'reset-password', component: ResetPassword },

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
        canActivate: [permissionGuard],
        data: { permission: 'DASHBOARD_VIEW' },
      },

      // LICENSES
      {
        path: 'licenses',
        component: Licenses,
        canActivate: [permissionGuard],
        data: { permission: 'LICENSES_VIEW' },
      },
      {
        path: 'licenses/new',
        component: LicenseCreate,
        canActivate: [permissionGuard],
        data: { permission: 'LICENSES_CREATE' },
      },
      {
        path: 'licenses/:id/edit',
        component: LicenseEdit,
        canActivate: [permissionGuard],
        data: { permission: 'LICENSES_UPDATE' },
      },
      {
        path: 'licenses/:id',
        component: LicenseDetails,
        canActivate: [permissionGuard],
        data: { permission: 'LICENSES_VIEW' },
      },

      // COMPANIES
      {
        path: 'companies',
        component: Companies,
        canActivate: [permissionGuard],
        data: { permission: 'COMPANIES_VIEW' },
      },
      {
        path: 'companies/new',
        component: CompanyCreate,
        canActivate: [permissionGuard],
        data: { permission: 'COMPANIES_CREATE' },
      },
      {
        path: 'companies/:id/edit',
        component: CompanyEdit,
        canActivate: [permissionGuard],
        data: { permission: 'COMPANIES_UPDATE' },
      },
      {
        path: 'companies/:id',
        component: CompanyDetails,
        canActivate: [permissionGuard],
        data: { permission: 'COMPANIES_VIEW' },
      },

      // BRANCHES
      {
        path: 'branches',
        component: Branches,
        canActivate: [permissionGuard],
        data: { permission: 'BRANCHES_VIEW' },
      },
      {
        path: 'branches/new',
        component: BranchCreate,
        canActivate: [permissionGuard],
        data: { permission: 'BRANCHES_CREATE' },
      },
      {
        path: 'branches/:id/edit',
        component: BranchEdit,
        canActivate: [permissionGuard],
        data: { permission: 'BRANCHES_UPDATE' },
      },
      {
        path: 'branches/:id',
        component: BranchDetails,
        canActivate: [permissionGuard],
        data: { permission: 'BRANCHES_VIEW' },
      },

      // USERS
      { path: 'users', component: Users, canActivate: [permissionGuard], data: { permission: 'USERS_VIEW' } },
      { path: 'users/new', component: UserCreate, canActivate: [permissionGuard], data: { permission: 'USERS_CREATE' } },
      { path: 'users/:id/edit', component: UserEdit, canActivate: [permissionGuard], data: { permission: 'USERS_UPDATE' } },
    ],
  },

  // 🚨 fallback
  {
    path: '**',
    redirectTo: 'login',
  },
];
