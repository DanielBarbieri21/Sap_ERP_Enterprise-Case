import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { EnterpriseShellComponent } from '../../shared/shell/enterprise-shell.component';

@Component({
  selector: 'app-wm',
  standalone: true,
  imports: [CommonModule, FormsModule, EnterpriseShellComponent],
  template: `
    <app-enterprise-shell
      section="WM"
      title="Estoque"
      subtitle="Controle de produtos, movimentacoes e monitoramento de itens criticos no estoque."
    >
      <section class="card">
        <h3>Produtos com estoque baixo</h3>
        <div class="toolbar">
          <button (click)="loadLowStock()">Carregar</button>
        </div>
        <pre>{{ lowStock | json }}</pre>
      </section>

      <section class="card">
        <h3>Novo produto</h3>
        <div class="grid-form">
          <label>Codigo <input [(ngModel)]="newProduct.code"></label>
          <label>Nome <input [(ngModel)]="newProduct.name"></label>
          <label>Unidade <input [(ngModel)]="newProduct.unit"></label>
          <label>Tipo
            <select [(ngModel)]="newProduct.type">
              <option value="MATERIAL">MATERIAL</option>
              <option value="SERVICE">SERVICE</option>
              <option value="FINISHED_GOOD">FINISHED_GOOD</option>
            </select>
          </label>
          <label>Custo <input type="number" step="0.01" [(ngModel)]="newProduct.costPrice"></label>
          <label>Preco <input type="number" step="0.01" [(ngModel)]="newProduct.salePrice"></label>
          <label>Estoque minimo <input type="number" step="0.001" [(ngModel)]="newProduct.minStock"></label>
          <label>Estoque maximo <input type="number" step="0.001" [(ngModel)]="newProduct.maxStock"></label>
        </div>
        <div class="toolbar">
          <button (click)="createProduct()">Criar produto</button>
        </div>
        <pre>{{ createdProduct | json }}</pre>
      </section>
    </app-enterprise-shell>
  `,
  styles: [`
    .card { background: rgba(255,255,255,0.9); padding: 1.5rem; margin-bottom: 1.5rem; border-radius: 20px; box-shadow: 0 18px 42px rgba(17,24,39,0.08); border: 1px solid rgba(17,24,39,0.06); }
    .toolbar, .grid-form { display:flex; gap:1rem; flex-wrap:wrap; align-items:end; margin-top:1rem; }
    label { display:flex; flex-direction:column; gap:.45rem; min-width:200px; flex:1 1 200px; }
    input, select, button { border-radius:12px; border:1px solid rgba(17,24,39,0.12); padding:.8rem .9rem; background:white; }
    button { background:#111827; color:white; cursor:pointer; }
  `]
})
export class WmComponent {
  api = environment.apiUrl;
  lowStock: any = null;
  createdProduct: any = null;
  newProduct: any = {
    code: '', name: '', unit: '', type: 'MATERIAL',
    costPrice: 0, salePrice: 0, minStock: 0, maxStock: 0
  };

  constructor(private http: HttpClient) {}

  loadLowStock() {
    this.http.get(`${this.api}/wm/products/low-stock`).subscribe((res) => this.lowStock = res);
  }

  createProduct() {
    this.http.post(`${this.api}/wm/products`, this.newProduct).subscribe((res) => this.createdProduct = res);
  }
}
