// src/environments/environment.prod.ts
export const environment = {
  production: true,
oauth: {
  issuer: 'http://localhost:9000',
  clientId: 'oidc-client',
  redirectUri: 'http://localhost:4200',
  scope: 'openid profile'
}
,
  api: {
    orders: 'http://localhost:8083/api/orders',
    clients: 'http://localhost:8081/api/clients',
    tracking: 'http://localhost:8084/api/tracking'
  }
};
