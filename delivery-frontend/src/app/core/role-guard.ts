// src/app/core/role-guard.ts
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from './auth';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRole = route.data['role'] as string;
    if (!this.auth.isLoggedIn()) { this.auth.login(); return false; }
    if (expectedRole && !this.auth.hasRole(expectedRole)) {
      this.router.navigate(['/unauthorized']);
      return false;
    }
    return true;
  }
}
