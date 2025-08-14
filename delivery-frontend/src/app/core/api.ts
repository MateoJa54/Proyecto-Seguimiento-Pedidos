import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment'; 
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  // clients
  getClients(): Observable<any> { return this.http.get(environment.api.clients); }
  getClient(id: number) { return this.http.get(`${environment.api.clients}/${id}`); }

  // orders
  getOrders() { return this.http.get(environment.api.orders); }
  getOrder(id: number) { return this.http.get(`${environment.api.orders}/${id}`); }
  createOrder(payload: any) { return this.http.post(environment.api.orders, payload); }
  updateOrder(id:number, payload:any) { return this.http.put(`${environment.api.orders}/${id}`, payload); }
  deleteOrder(id:number) { return this.http.delete(`${environment.api.orders}/${id}`); }

  // tracking
  getTracking(orderId:number) { return this.http.get(`${environment.api.tracking}/${orderId}`); }
  updateTracking(payload:any) { return this.http.post(`${environment.api.tracking}/sync`, payload); }
}
