import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { EnterpriseShellComponent } from '../../shared/shell/enterprise-shell.component';

@Component({
  selector: 'app-mm',
  standalone: true,
  imports: [CommonModule, FormsModule, EnterpriseShellComponent],
  template: `
    <app-enterprise-shell
      section="MM"
      title="Compras"
      subtitle="Operacao de requisicoes, pedidos e recebimentos com foco em suprimentos."
    >
      <section class="card">
        <h3>Pedidos por periodo</h3>
        <div class="toolbar">
          <label>Inicio: <input type="date" [(ngModel)]="startDate"></label>
          <label>Fim: <input type="date" [(ngModel)]="endDate"></label>
          <button (click)="loadOrders()">Carregar</button>
        </div>
        <pre>{{ orders | json }}</pre>
      </section>

      <section class="card">
        <h3>Nova requisicao</h3>
        <div class="toolbar">
          <label>Fornecedor
            <select [(ngModel)]="req.supplierId">
              <option [ngValue]="''">Selecione...</option>
              <option *ngFor="let s of suppliers" [ngValue]="s.supplierId">{{ s.supplierName }}</option>
            </select>
          </label>
          <label>Produto
            <select [(ngModel)]="req.productId">
              <option [ngValue]="''">Selecione...</option>
              <option *ngFor="let p of products" [ngValue]="p.id">{{ p.code }} - {{ p.name }}</option>
            </select>
          </label>
          <label>Quantidade <input type="number" step="0.001" [(ngModel)]="req.qty"></label>
          <button (click)="createRequisition()">Criar</button>
        </div>
        <pre>{{ createdReq | json }}</pre>
      </section>

      <section class="card">
        <h3>Novo pedido</h3>
        <div class="toolbar">
          <label>Fornecedor
            <select [(ngModel)]="ord.supplierId">
              <option [ngValue]="''">Selecione...</option>
              <option *ngFor="let s of suppliers" [ngValue]="s.supplierId">{{ s.supplierName }}</option>
            </select>
          </label>
          <label>Produto
            <select [(ngModel)]="ord.productId">
              <option [ngValue]="''">Selecione...</option>
              <option *ngFor="let p of products" [ngValue]="p.id">{{ p.code }} - {{ p.name }}</option>
            </select>
          </label>
          <label>Quantidade <input type="number" step="0.001" [(ngModel)]="ord.qty"></label>
          <label>Preco <input type="number" step="0.01" [(ngModel)]="ord.price"></label>
          <button (click)="createOrder()">Criar</button>
        </div>
        <pre>{{ createdOrder | json }}</pre>
      </section>
    </app-enterprise-shell>
  `,
  styles: [`
    .card { background: rgba(255,255,255,0.9); padding: 1.5rem; margin-bottom: 1.5rem; border-radius: 20px; box-shadow: 0 18px 42px rgba(17,24,39,0.08); border: 1px solid rgba(17,24,39,0.06); }
    .toolbar { display:flex; gap:1rem; flex-wrap:wrap; align-items:end; margin-top:1rem; }
    label { display:flex; flex-direction:column; gap:.45rem; min-width:200px; }
    input, select, button { border-radius:12px; border:1px solid rgba(17,24,39,0.12); padding:.8rem .9rem; background:white; }
    button { background:#111827; color:white; cursor:pointer; }
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
  createdReq: any = null;
  createdOrder: any = null;

  constructor(private http: HttpClient) {
    this.loadProducts();
    this.loadSuppliers();
  }

  loadOrders() {
    if (!this.startDate || !this.endDate) return;
    this.http.get(`${this.api}/mm/orders`, { params: { startDate: this.startDate, endDate: this.endDate } })
      .subscribe((res) => this.orders = res);
  }

  createRequisition() {
    if (!this.req.supplierId || !this.req.productId || !this.req.qty) { alert('Preencha fornecedor, produto e quantidade'); return; }
    const payload = {
      supplierId: this.req.supplierId,
      items: [{ product: { id: this.req.productId }, quantity: this.req.qty }]
    };
    this.http.post(`${this.api}/mm/requisitions`, payload).subscribe((res) => this.createdReq = res);
  }

  createOrder() {
    if (!this.ord.supplierId || !this.ord.productId || !this.ord.qty) { alert('Preencha fornecedor, produto e quantidade'); return; }
    const payload = {
      supplierId: this.ord.supplierId,
      items: [{ product: { id: this.ord.productId }, quantity: this.ord.qty, price: this.ord.price }]
    };
    this.http.post(`${this.api}/mm/orders`, payload).subscribe((res) => this.createdOrder = res);
  }

  loadProducts() {
    this.http.get<any[]>(`${this.api}/wm/products`).subscribe((res) => this.products = res);
  }

  loadSuppliers() {
    this.http.get<any[]>(`${this.api}/mm/suppliers`).subscribe((res) => this.suppliers = res);
  }
}
