import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-wm',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, FormsModule],
  template: `
    <div class="module-page">
      <h2>Estoque (WM)</h2>
      <a routerLink="/dashboard">Voltar ao Dashboard</a>

      <section class="card">
        <h3>Produtos com Estoque Baixo</h3>
        <button (click)="loadLowStock()">Carregar</button>
        <pre>{{ lowStock | json }}</pre>
      </section>

      <section class="card">
        <h3>Novo Produto</h3>
        <div class="row">
          <label>Código: <input [(ngModel)]="newProduct.code"></label>
          <label>Nome: <input [(ngModel)]="newProduct.name"></label>
          <label>Unidade: <input [(ngModel)]="newProduct.unit" placeholder="UN, KG, M"></label>
          <label>Tipo:
            <select [(ngModel)]="newProduct.type">
              <option value="MATERIAL">MATERIAL</option>
              <option value="SERVICE">SERVICE</option>
              <option value="FINISHED_GOOD">FINISHED_GOOD</option>
            </select>
          </label>
          <label>Custo: <input type="number" step="0.01" [(ngModel)]="newProduct.costPrice"></label>
          <label>Preço: <input type="number" step="0.01" [(ngModel)]="newProduct.salePrice"></label>
          <label>Estoque Mín.: <input type="number" step="0.001" [(ngModel)]="newProduct.minStock"></label>
          <label>Estoque Máx.: <input type="number" step="0.001" [(ngModel)]="newProduct.maxStock"></label>
          <button (click)="createProduct()">Criar</button>
        </div>
        <pre>{{ createdProduct | json }}</pre>
      </section>

      <section class="card">
        <h3>Movimentação</h3>
        <div class="row">
          <label>Produto:
            <select [(ngModel)]="movement.productId">
              <option [ngValue]="''">Selecione...</option>
              <option *ngFor="let p of products" [ngValue]="p.id">{{ p.code }} - {{ p.name }}</option>
            </select>
          </label>
          <label>Data: <input type="date" [(ngModel)]="movement.movementDate"></label>
          <label>Tipo:
            <select [(ngModel)]="movement.movementType">
              <option value="ENTRADA">ENTRADA</option>
              <option value="SAIDA">SAIDA</option>
            </select>
          </label>
          <label>Quantidade: <input type="number" step="0.001" [(ngModel)]="movement.quantity"></label>
          <button (click)="createMovement()">Registrar</button>
        </div>
        <pre>{{ createdMovement | json }}</pre>
      </section>

      <section class="card">
        <h3>Inventário</h3>
        <textarea [(ngModel)]="inventoryJson" rows="4" style="width:100%" placeholder='{"date":"2026-01-21","items":[{"productId":"...","countedQty":10}]}'></textarea>
        <button (click)="createInventory()">Criar</button>
        <pre>{{ createdInventory | json }}</pre>
      </section>
    </div>
  `,
  styles: [`
    .module-page { padding: 2rem; }
    h2 { margin-bottom: .5rem; }
    .card { background:#fff; padding:1rem; margin:1rem 0; border-radius:8px; box-shadow:0 1px 3px rgba(0,0,0,.1); }
  `]
})
export class WmComponent {
  api = environment.apiUrl;
  lowStock: any = null;
  inventoryJson = '';
  createdProduct: any = null;
  createdMovement: any = null;
  createdInventory: any = null;
  products: any[] = [];
  newProduct: any = {
    code: '', name: '', unit: '', type: 'MATERIAL',
    costPrice: 0, salePrice: 0, minStock: 0, maxStock: 0
  };
  movement: any = {
    productId: '', movementDate: '', movementType: 'ENTRADA', quantity: 0
  };

  constructor(private http: HttpClient) {
    this.loadProducts();
  }

  loadLowStock() {
    this.http.get(`${this.api}/wm/products/low-stock`).subscribe(res => this.lowStock = res);
  }

  createProduct() {
    this.http.post(`${this.api}/wm/products`, this.newProduct).subscribe(res => this.createdProduct = res);
  }

  createMovement() {
    const payload = {
      product: { id: this.movement.productId },
      movementDate: this.movement.movementDate || null,
      movementType: this.movement.movementType,
      quantity: this.movement.quantity
    };
    this.http.post(`${this.api}/wm/movements`, payload).subscribe(res => this.createdMovement = res);
  }

  createInventory() {
    let payload: any; try { payload = JSON.parse(this.inventoryJson); } catch { alert('JSON inválido'); return; }
    this.http.post(`${this.api}/wm/inventories`, payload).subscribe(res => this.createdInventory = res);
  }

  loadProducts() {
    this.http.get<any[]>(`${this.api}/wm/products`).subscribe(res => this.products = res);
  }
}
