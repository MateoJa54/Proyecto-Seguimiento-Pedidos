// src/app/core/auth.service.ts
import { Injectable } from '@angular/core';
import { OAuthService, AuthConfig } from 'angular-oauth2-oidc';
import { environment } from '../../environments/environment';

export const AUTH_CONFIG: AuthConfig = {
  issuer: environment.oauth.issuer,
  redirectUri: window.location.origin,
  clientId: 'angular-spa',
  responseType: 'code',        // code flow -> PKCE implicito en versiones modernas
  scope: 'openid profile',
  showDebugInformation: true,
  strictDiscoveryDocumentValidation: false
};

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private oauthService: OAuthService) {
    this.oauthService.configure(AUTH_CONFIG);
    // No ejecutar carga en constructor; se hará desde AppComponent.init()
  }

  /** Inicialización (llamar desde AppComponent.ngOnInit) */
  public async init(): Promise<void> {
    try {
      await this.oauthService.loadDiscoveryDocumentAndTryLogin();
      console.log('Discovery loaded. loggedIn=', this.isAuthenticated());
    } catch (err) {
      console.error('Error loading discovery/login', err);
      throw err;
    }
  }

  login(): void {
    this.oauthService.initCodeFlow();
  }

  logout(): void {
    // logOut() redirecciona al end_session endpoint si está configurado
    this.oauthService.logOut();
  }

  /** Nombre "oficial" */
  isAuthenticated(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

  /** Alias para compatibilidad con templates/guards existentes */
  isLoggedIn(): boolean {
    return this.isAuthenticated();
  }

getAccessToken(): string | null {
  return this.oauthService.getAccessToken() || null;
}


  getIdentityClaims(): any | null {
    return (this.oauthService.getIdentityClaims() as any) || null;
  }

  
  hasRole(role: string): boolean {
    const claims = this.getIdentityClaims();
    if (!claims) return false;
    const roles = claims['roles'] || claims['role'] || claims['authorities'];
    if (!roles) return false;
    return Array.isArray(roles) ? roles.includes(role) : roles === role;
  }
}
