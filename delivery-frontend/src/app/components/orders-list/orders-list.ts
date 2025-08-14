import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ApiService } from '../../core/api';
import { AuthService } from '../../core/auth';

@Component({
  selector: 'app-orders-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './orders-list.html'
})
export class OrdersListComponent implements OnInit {
  orders: any[] = [];
  loading = false;
  error = '';

  constructor(
    private api: ApiService,
    private router: Router,
    public auth: AuthService
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.api.getOrders().subscribe({
      next: (res: any) => { this.orders = Array.isArray(res) ? res : []; this.loading = false; },
      error: (err: any) => { this.error = err?.message || 'Error al cargar pedidos'; this.loading = false; }
    });
  }

  isAdminOrCliente(): boolean {
    return this.auth.hasRole('ADMIN') || this.auth.hasRole('CLIENTE');
  }

  view(id: number) { this.router.navigate(['/orders', id]); }
  create() { this.router.navigate(['/orders/new']); }
}
