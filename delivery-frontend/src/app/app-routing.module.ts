// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './components/home/home';
import { LoginComponent } from './components/login/login';
import { OrdersListComponent } from './components/orders-list/orders-list';
import { OrderFormComponent } from './components/order-form/order-form';
import { OrderDetailComponent } from './components/order-detail/order-detail';
import { ClientsListComponent } from './components/clients-list/clients-list';
import { UnauthorizedComponent } from './components/unauthorized/unauthorized';

import { AuthGuard } from './core/auth-guard';
import { RoleGuard } from './core/role-guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'orders', component: OrdersListComponent, canActivate: [AuthGuard] },
  { path: 'orders/new', component: OrderFormComponent, canActivate: [AuthGuard, RoleGuard], data: { role: 'ADMIN' } },
  { path: 'orders/:id', component: OrderDetailComponent, canActivate: [AuthGuard] },
  { path: 'clients', component: ClientsListComponent, canActivate: [AuthGuard] },
  { path: 'unauthorized', component: UnauthorizedComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
