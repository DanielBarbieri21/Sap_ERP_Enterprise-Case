import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { EnterpriseShellComponent } from '../../shared/shell/enterprise-shell.component';

@Component({
  selector: 'app-sd',
  standalone: true,
  imports: [CommonModule, FormsModule, EnterpriseShellComponent],
  template: `
    <app-enterprise-shell
      section="SD"
      title="Vendas"
      subtitle="Acompanhamento comercial com pedidos e faturamento para demonstracao do fluxo de vendas."
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
        <h3>Novo pedido</h3>
        <textarea [(ngModel)]="orderJson" rows="5"></textarea>
        <div class="toolbar">
          <button (click)="createOrder()">Criar pedido</button>
        </div>
        <pre>{{ createdOrder | json }}</pre>
      </section>

      <section class="card">
        <h3>Nova nota</h3>
        <textarea [(ngModel)]="invoiceJson" rows="5"></textarea>
        <div class="toolbar">
          <button (click)="createInvoice()">Criar nota</button>
        </div>
        <pre>{{ createdInvoice | json }}</pre>
      </section>
    </app-enterprise-shell>
  `,
  styles: [`
    .card { background: rgba(255,255,255,0.9); padding: 1.5rem; margin-bottom: 1.5rem; border-radius: 20px; box-shadow: 0 18px 42px rgba(17,24,39,0.08); border: 1px solid rgba(17,24,39,0.06); }
    .toolbar { display:flex; gap:1rem; flex-wrap:wrap; align-items:end; margin-top:1rem; }
    label { display:flex; flex-direction:column; gap:.45rem; }
    input, textarea, button { border-radius:12px; border:1px solid rgba(17,24,39,0.12); padding:.8rem .9rem; background:white; width:100%; }
    button { width:auto; background:#111827; color:white; cursor:pointer; }
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
      .subscribe((res) => this.orders = res);
  }

  createOrder() {
    let payload: any; try { payload = JSON.parse(this.orderJson); } catch { alert('JSON invalido'); return; }
    this.http.post(`${this.api}/sd/orders`, payload).subscribe((res) => this.createdOrder = res);
  }

  createInvoice() {
    let payload: any; try { payload = JSON.parse(this.invoiceJson); } catch { alert('JSON invalido'); return; }
    this.http.post(`${this.api}/sd/invoices`, payload).subscribe((res) => this.createdInvoice = res);
  }
}
