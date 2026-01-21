import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-sd',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, FormsModule],
  template: `
    <div class="module-page">
      <h2>Vendas (SD)</h2>
      <a routerLink="/dashboard">Voltar ao Dashboard</a>

      <section class="card">
        <h3>Pedidos por Período</h3>
        <div class="row">
          <label>Início: <input type="date" [(ngModel)]="startDate"></label>
          <label>Fim: <input type="date" [(ngModel)]="endDate"></label>
          <button (click)="loadOrders()">Carregar</button>
        </div>
        <pre>{{ orders | json }}</pre>
      </section>

      <section class="card">
        <h3>Novo Pedido</h3>
        <textarea [(ngModel)]="orderJson" rows="4" style="width:100%" placeholder='{"date":"2026-01-21","customerId":"...","items":[{"productId":"...","qty":1,"price":100}]}'></textarea>
        <button (click)="createOrder()">Criar</button>
        <pre>{{ createdOrder | json }}</pre>
      </section>

      <section class="card">
        <h3>Nova Nota (Fatura)</h3>
        <textarea [(ngModel)]="invoiceJson" rows="4" style="width:100%" placeholder='{"date":"2026-01-21","salesOrderId":"...","amount":100}'></textarea>
        <button (click)="createInvoice()">Criar</button>
        <pre>{{ createdInvoice | json }}</pre>
      </section>
    </div>
  `,
  styles: [`
    .module-page { padding: 2rem; }
    h2 { margin-bottom: .5rem; }
    .card { background:#fff; padding:1rem; margin:1rem 0; border-radius:8px; box-shadow:0 1px 3px rgba(0,0,0,.1); }
    .row { display:flex; gap:1rem; flex-wrap:wrap; align-items:center; }
  `]
})
export class SdComponent {
  api = environment.apiUrl;
  startDate = '';
  endDate = '';
  orders: any = null;
  orderJson = '';
  invoiceJson = '';
  createdOrder: any = null;
  createdInvoice: any = null;

  constructor(private http: HttpClient) {}

  loadOrders() {
    if (!this.startDate || !this.endDate) return;
    this.http.get(`${this.api}/sd/orders`, { params: { startDate: this.startDate, endDate: this.endDate } })
      .subscribe(res => this.orders = res);
  }

  createOrder() {
    let payload: any; try { payload = JSON.parse(this.orderJson); } catch { alert('JSON inválido'); return; }
    this.http.post(`${this.api}/sd/orders`, payload).subscribe(res => this.createdOrder = res);
  }

  createInvoice() {
    let payload: any; try { payload = JSON.parse(this.invoiceJson); } catch { alert('JSON inválido'); return; }
    this.http.post(`${this.api}/sd/invoices`, payload).subscribe(res => this.createdInvoice = res);
  }
}
