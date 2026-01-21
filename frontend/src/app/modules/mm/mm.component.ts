import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-mm',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, FormsModule],
  template: `
    <div class="module-page">
      <h2>Compras (MM)</h2>
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
        <h3>Nova Requisição</h3>
        <div class="row">
          <label>Fornecedor:
            <select [(ngModel)]="req.supplierId">
              <option [ngValue]="''">Selecione...</option>
              <option *ngFor="let s of suppliers" [ngValue]="s.supplierId">{{ s.supplierName }}</option>
            </select>
          </label>
          <label>Produto:
            <select [(ngModel)]="req.productId">
              <option [ngValue]="''">Selecione...</option>
              <option *ngFor="let p of products" [ngValue]="p.id">{{ p.code }} - {{ p.name }}</option>
            </select>
          </label>
          <label>Quantidade: <input type="number" step="0.001" [(ngModel)]="req.qty"></label>
          <button (click)="createRequisition()">Criar</button>
        </div>
        <pre>{{ createdReq | json }}</pre>
      </section>

      <section class="card">
        <h3>Novo Pedido</h3>
        <div class="row">
          <label>Fornecedor:
            <select [(ngModel)]="ord.supplierId">
              <option [ngValue]="''">Selecione...</option>
              <option *ngFor="let s of suppliers" [ngValue]="s.supplierId">{{ s.supplierName }}</option>
            </select>
          </label>
          <label>Produto:
            <select [(ngModel)]="ord.productId">
              <option [ngValue]="''">Selecione...</option>
              <option *ngFor="let p of products" [ngValue]="p.id">{{ p.code }} - {{ p.name }}</option>
            </select>
          </label>
          <label>Quantidade: <input type="number" step="0.001" [(ngModel)]="ord.qty"></label>
          <label>Preço: <input type="number" step="0.01" [(ngModel)]="ord.price"></label>
          <button (click)="createOrder()">Criar</button>
        </div>
        <pre>{{ createdOrder | json }}</pre>
      </section>

      <section class="card">
        <h3>Entrada (Recebimento)</h3>
        <textarea [(ngModel)]="receiptJson" rows="4" style="width:100%" placeholder='{"date":"2026-01-21","purchaseOrderId":"...","items":[{"productId":"...","qty":10}]}'></textarea>
        <button (click)="createReceipt()">Registrar</button>
        <pre>{{ createdReceipt | json }}</pre>
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
export class MmComponent {
  api = environment.apiUrl;
  startDate = '';
  endDate = '';
  orders: any = null;
  products: any[] = [];
  suppliers: any[] = [];
  req: any = { supplierId: '', productId: '', qty: 1 };
  ord: any = { supplierId: '', productId: '', qty: 1, price: 0 };
  receiptJson = '';
  createdReq: any = null;
  createdOrder: any = null;
  createdReceipt: any = null;

  constructor(private http: HttpClient) {
    this.loadProducts();
    this.loadSuppliers();
  }

  loadOrders() {
    if (!this.startDate || !this.endDate) return;
    this.http.get(`${this.api}/mm/orders`, { params: { startDate: this.startDate, endDate: this.endDate } })
      .subscribe(res => this.orders = res);
  }

  createRequisition() {
    if (!this.req.supplierId || !this.req.productId || !this.req.qty) { alert('Preencha fornecedor, produto e quantidade'); return; }
    const payload = {
      supplierId: this.req.supplierId,
      items: [ { product: { id: this.req.productId }, quantity: this.req.qty } ]
    };
    this.http.post(`${this.api}/mm/requisitions`, payload).subscribe(res => this.createdReq = res);
  }

  createOrder() {
    if (!this.ord.supplierId || !this.ord.productId || !this.ord.qty) { alert('Preencha fornecedor, produto e quantidade'); return; }
    const payload = {
      supplierId: this.ord.supplierId,
      items: [ { product: { id: this.ord.productId }, quantity: this.ord.qty, price: this.ord.price } ]
    };
    this.http.post(`${this.api}/mm/orders`, payload).subscribe(res => this.createdOrder = res);
  }

  loadProducts() {
    this.http.get<any[]>(`${this.api}/wm/products`).subscribe(res => this.products = res);
  }

  loadSuppliers() {
    this.http.get<any[]>(`${this.api}/mm/suppliers`).subscribe(res => this.suppliers = res);
  }

  createReceipt() {
    let payload: any; try { payload = JSON.parse(this.receiptJson); } catch { alert('JSON inválido'); return; }
    this.http.post(`${this.api}/mm/receipts`, payload).subscribe(res => this.createdReceipt = res);
  }
}
