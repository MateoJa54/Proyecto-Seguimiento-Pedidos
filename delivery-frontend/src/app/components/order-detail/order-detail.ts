import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, timer } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { ApiService } from '../../core/api';

@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-detail.html'
})
export class OrderDetailComponent implements OnInit, OnDestroy {
  order: any = null;
  tracking: any = null;
  loadingOrder = false;
  loadingTracking = false;
  error = '';
  pollSub: Subscription | null = null;
  orderId!: number;

  constructor(private api: ApiService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    const id = idStr ? +idStr : null;
    if (!id) { this.error = 'ID de pedido inválido'; return; }
    this.orderId = id;
    this.loadOrder(id);

    this.pollSub = timer(0, 5000).pipe(switchMap(()=> this.api.getTracking(id))).subscribe({
      next: (t:any) => { this.tracking = t; this.loadingTracking = false; },
      error: () => { this.tracking = null; this.loadingTracking = false; }
    });
  }

  ngOnDestroy(): void { if (this.pollSub) this.pollSub.unsubscribe(); }

  loadOrder(id:number): void {
    this.loadingOrder = true;
    this.api.getOrder(id).subscribe({
      next: (o:any) => { this.order = o; this.loadingOrder = false; },
      error: (err:any) => { this.loadingOrder = false; this.error = err?.message || 'Pedido no encontrado'; }
    });
  }

  staleClass(): 'fresh'|'stale'|'outdated' {
    try {
      if (!this.tracking || !this.order) return 'outdated';
      const orderDate = this.order.fecha ? new Date(this.order.fecha).getTime() : null;
      const trackDate = (this.tracking.timestamp || this.tracking.lastUpdated || this.tracking.date) ?
        new Date(this.tracking.timestamp || this.tracking.lastUpdated || this.tracking.date).getTime() : null;
      if (!orderDate || !trackDate) return 'outdated';
      const diff = Math.abs(orderDate - trackDate) / 1000;
      if (diff < 10) return 'fresh';
      if (diff < 60) return 'stale';
      return 'outdated';
    } catch { return 'outdated'; }
  }
}
