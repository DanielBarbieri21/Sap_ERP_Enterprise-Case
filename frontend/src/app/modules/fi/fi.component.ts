import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-fi',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, FormsModule],
  template: `
    <div class="module-page">
      <h2>Financeiro (FI)</h2>
      <a routerLink="/dashboard">Voltar ao Dashboard</a>

      <section class="card">
        <h3>Resumo</h3>
        <div class="row">
          <label>Início: <input type="date" [(ngModel)]="startDate"></label>
          <label>Fim: <input type="date" [(ngModel)]="endDate"></label>
          <button (click)="loadSummary()">Carregar</button>
        </div>
        <pre>{{ summary | json }}</pre>
      </section>

      <section class="card">
        <h3>Pendências</h3>
        <button (click)="loadPending()">Listar</button>
        <ul>
          <li *ngFor="let t of pending">
            {{ t.documentNumber }} - {{ t.description }} - {{ t.amount }} - {{ t.status }}
            <button (click)="pay(t.id)">Pagar</button>
          </li>
        </ul>
      </section>

      <section class="card">
        <h3>Novo Lançamento</h3>
        <div class="row">
          <label>Número Doc: <input [(ngModel)]="newTx.documentNumber"></label>
          <label>Data: <input type="date" [(ngModel)]="newTx.transactionDate"></label>
          <label>Vencimento: <input type="date" [(ngModel)]="newTx.dueDate"></label>
          <label>Tipo: 
            <select [(ngModel)]="newTx.type">
              <option value="RECEITA">RECEITA</option>
              <option value="DESPESA">DESPESA</option>
            </select>
          </label>
          <label>Conta:
            <select [(ngModel)]="newTx.accountId">
              <option [ngValue]="''">Selecione...</option>
              <option *ngFor="let a of accounts" [ngValue]="a.id">{{ a.code }} - {{ a.name }}</option>
            </select>
          </label>
          <label>Descrição: <input [(ngModel)]="newTx.description"></label>
          <label>Valor: <input type="number" step="0.01" [(ngModel)]="newTx.amount"></label>
          <button (click)="createTx()">Criar</button>
        </div>
        <pre>{{ created | json }}</pre>
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
export class FiComponent {
  api = environment.apiUrl;
  startDate = '';
  endDate = '';
  summary: any = null;
  pending: any[] = [];
  created: any = null;
  accounts: any[] = [];
  newTx: any = {
    documentNumber: '',
    transactionDate: '',
    dueDate: '',
    type: 'DESPESA',
    accountId: '',
    description: '',
    amount: 0
  };

  constructor(private http: HttpClient) {
    this.loadAccounts();
  }

  loadSummary() {
    const params: any = {};
    if (this.startDate) params.startDate = this.startDate;
    if (this.endDate) params.endDate = this.endDate;
    this.http.get(`${this.api}/fi/transactions/summary`, { params })
      .subscribe(res => this.summary = res);
  }

  loadPending() {
    this.http.get<any[]>(`${this.api}/fi/transactions/pending`)
      .subscribe(res => this.pending = res);
  }

  pay(id: string) {
    const amount = prompt('Valor a pagar:');
    if (!amount) return;
    this.http.post(`${this.api}/fi/transactions/${id}/pay`, { amount: parseFloat(amount) })
      .subscribe(_ => this.loadPending());
  }

  createTx() {
    // Monta payload conforme entidade de backend; usa accountId simplificado
    const payload = {
      documentNumber: this.newTx.documentNumber,
      transactionDate: this.newTx.transactionDate,
      dueDate: this.newTx.dueDate || null,
      type: this.newTx.type,
      description: this.newTx.description,
      amount: this.newTx.amount,
      account: { id: this.newTx.accountId }
    };
    this.http.post(`${this.api}/fi/transactions`, payload)
      .subscribe(res => this.created = res);
  }

  loadAccounts() {
    this.http.get<any[]>(`${this.api}/fi/accounts`).subscribe(res => this.accounts = res);
  }
}
