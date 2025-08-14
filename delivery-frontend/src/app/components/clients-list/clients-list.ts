import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/api';

@Component({
  selector: 'app-clients-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './clients-list.html'
})
export class ClientsListComponent implements OnInit {
  clients: any[] = [];
  loading = false;
  error = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.api.getClients().subscribe({
      next: (res:any) => { this.clients = Array.isArray(res) ? res : []; this.loading = false; },
      error: (err:any) => { this.error = err?.message || 'Error al cargar clientes'; this.loading = false; }
    });
  }
}
