import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth';

export const permissionGuard: CanActivateFn = route => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const permission = route.data['permission'] as string | undefined;

  if (!permission || auth.hasPermission(permission)) return true;
  return router.createUrlTree(['/dashboard']);
};
