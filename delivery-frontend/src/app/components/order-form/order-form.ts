import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../core/api';

@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './order-form.html'
})
export class OrderFormComponent implements OnInit {
  model: any = { producto: '', clienteId: null, direccionEntrega: '', fecha: new Date().toISOString().slice(0,10), estado: 'CREADO' };
  clients: any[] = [];
  saving = false;
  errorMessage = '';

  constructor(private api: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.api.getClients().subscribe({ next: (res:any) => this.clients = Array.isArray(res) ? res : [], error: ()=> this.clients=[] });
  }

  submit(): void {
    this.errorMessage = '';
    if (!this.model.producto || !this.model.clienteId || !this.model.direccionEntrega) {
      this.errorMessage = 'Completa todos los campos obligatorios.';
      return;
    }
    this.saving = true;
    this.api.createOrder(this.model).subscribe({
      next: () => { this.saving = false; this.router.navigate(['/orders']); },
      error: (err:any) => { this.saving = false; this.errorMessage = err?.error || err?.message || 'Error al crear pedido'; }
    });
  }
}
