import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { EnterpriseShellComponent } from '../../shared/shell/enterprise-shell.component';

@Component({
  selector: 'app-fi',
  standalone: true,
  imports: [CommonModule, FormsModule, EnterpriseShellComponent],
  template: `
    <app-enterprise-shell
      section="FI"
      title="Financeiro"
      subtitle="Acompanhamento financeiro com resumo, pendencias e lancamentos controlados por conta contabil."
    >
      <section class="card">
        <h3>Resumo financeiro</h3>
        <div class="toolbar">
          <label>Inicio: <input type="date" [(ngModel)]="startDate"></label>
          <label>Fim: <input type="date" [(ngModel)]="endDate"></label>
          <button (click)="loadSummary()">Carregar</button>
        </div>
        <pre>{{ summary | json }}</pre>
      </section>

      <section class="card">
        <h3>Pendencias</h3>
        <div class="toolbar">
          <button (click)="loadPending()">Listar transacoes pendentes</button>
        </div>
        <ul class="list">
          <li *ngFor="let t of pending">
            <div>
              <strong>{{ t.documentNumber }}</strong>
              <span>{{ t.description }}</span>
            </div>
            <div class="list-actions">
              <span>{{ t.amount }}</span>
              <button (click)="pay(t.id)">Pagar</button>
            </div>
          </li>
        </ul>
      </section>

      <section class="card">
        <h3>Novo lancamento</h3>
        <div class="grid-form">
          <label>Numero do documento <input [(ngModel)]="newTx.documentNumber"></label>
          <label>Data <input type="date" [(ngModel)]="newTx.transactionDate"></label>
          <label>Vencimento <input type="date" [(ngModel)]="newTx.dueDate"></label>
          <label>Tipo
            <select [(ngModel)]="newTx.type">
              <option value="RECEITA">RECEITA</option>
              <option value="DESPESA">DESPESA</option>
            </select>
          </label>
          <label>Conta
            <select [(ngModel)]="newTx.accountId">
              <option [ngValue]="''">Selecione...</option>
              <option *ngFor="let a of accounts" [ngValue]="a.id">{{ a.code }} - {{ a.name }}</option>
            </select>
          </label>
          <label>Descricao <input [(ngModel)]="newTx.description"></label>
          <label>Valor <input type="number" step="0.01" [(ngModel)]="newTx.amount"></label>
        </div>
        <div class="toolbar">
          <button (click)="createTx()">Criar lancamento</button>
        </div>
        <pre>{{ created | json }}</pre>
      </section>
    </app-enterprise-shell>
  `,
  styles: [`
    .card {
      background: rgba(255, 255, 255, 0.9);
      padding: 1.5rem;
      margin-bottom: 1.5rem;
      border-radius: 20px;
      box-shadow: 0 18px 42px rgba(17, 24, 39, 0.08);
      border: 1px solid rgba(17, 24, 39, 0.06);
    }

    .toolbar,
    .grid-form {
      display: flex;
      gap: 1rem;
      flex-wrap: wrap;
      align-items: end;
      margin-top: 1rem;
    }

    .grid-form label {
      display: flex;
      flex-direction: column;
      gap: 0.45rem;
      min-width: 220px;
      flex: 1 1 220px;
    }

    input,
    select,
    button {
      border-radius: 12px;
      border: 1px solid rgba(17, 24, 39, 0.12);
      padding: 0.8rem 0.9rem;
      background: white;
    }

    button {
      background: #111827;
      color: white;
      cursor: pointer;
    }

    .list {
      list-style: none;
      padding: 0;
      margin: 1rem 0 0;
      display: flex;
      flex-direction: column;
      gap: 0.8rem;
    }

    .list li {
      display: flex;
      justify-content: space-between;
      gap: 1rem;
      align-items: center;
      padding: 1rem;
      border-radius: 16px;
      background: rgba(17, 24, 39, 0.03);
    }

    .list li div {
      display: flex;
      flex-direction: column;
      gap: 0.25rem;
    }

    .list-actions {
      align-items: flex-end;
    }
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
    this.http.get(`${this.api}/fi/transactions/summary`, { params }).subscribe((res) => this.summary = res);
  }

  loadPending() {
    this.http.get<any[]>(`${this.api}/fi/transactions/pending`).subscribe((res) => this.pending = res);
  }

  pay(id: string) {
    const amount = prompt('Valor a pagar:');
    if (!amount) return;
    this.http.post(`${this.api}/fi/transactions/${id}/pay`, { amount: parseFloat(amount) }).subscribe(() => this.loadPending());
  }

  createTx() {
    const payload = {
      documentNumber: this.newTx.documentNumber,
      transactionDate: this.newTx.transactionDate,
      dueDate: this.newTx.dueDate || null,
      type: this.newTx.type,
      description: this.newTx.description,
      amount: this.newTx.amount,
      accountId: this.newTx.accountId
    };
    this.http.post(`${this.api}/fi/transactions`, payload).subscribe((res) => this.created = res);
  }

  loadAccounts() {
    this.http.get<any[]>(`${this.api}/fi/accounts`).subscribe((res) => this.accounts = res);
  }
}
