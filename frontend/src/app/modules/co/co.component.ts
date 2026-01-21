import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-co',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, FormsModule],
  template: `
    <div class="module-page">
      <h2>Contábil (CO)</h2>
      <a routerLink="/dashboard">Voltar ao Dashboard</a>

      <section class="card">
        <h3>DRE e Balancete</h3>
        <div class="row">
          <label>Início: <input type="date" [(ngModel)]="startDate"></label>
          <label>Fim: <input type="date" [(ngModel)]="endDate"></label>
          <button (click)="loadTrialBalance()">Balancete</button>
          <button (click)="loadIncomeStatement()">DRE</button>
        </div>
        <h4>Balancete</h4>
        <pre>{{ trialBalance | json }}</pre>
        <h4>DRE</h4>
        <pre>{{ incomeStatement | json }}</pre>
      </section>

      <section class="card">
        <h3>Balanço</h3>
        <div class="row">
          <label>Data: <input type="date" [(ngModel)]="balanceDate"></label>
          <button (click)="loadBalanceSheet()">Carregar</button>
        </div>
        <pre>{{ balanceSheet | json }}</pre>
      </section>

      <section class="card">
        <h3>Novo Documento</h3>
        <textarea [(ngModel)]="documentJson" rows="6" style="width:100%" placeholder='{"date":"2026-01-21","description":"Lançamento","entries":[{"accountId":"...","debit":100},{"accountId":"...","credit":100}]}'></textarea>
        <button (click)="createDocument()">Criar</button>
        <pre>{{ created | json }}</pre>
      </section>

      <section class="card">
        <h3>Postar Documento</h3>
        <div class="row">
          <label>Documento ID: <input [(ngModel)]="postId"></label>
          <button (click)="postDocument()">Postar</button>
        </div>
        <pre>{{ posted | json }}</pre>
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
export class CoComponent {
  api = environment.apiUrl;
  startDate = '';
  endDate = '';
  balanceDate = '';
  trialBalance: any = null;
  balanceSheet: any = null;
  incomeStatement: any = null;
  documentJson = '';
  created: any = null;
  postId = '';
  posted: any = null;

  constructor(private http: HttpClient) {}

  loadTrialBalance() {
    if (!this.startDate || !this.endDate) return;
    this.http.get(`${this.api}/co/documents/trial-balance`, { params: { startDate: this.startDate, endDate: this.endDate } })
      .subscribe(res => this.trialBalance = res);
  }

  loadBalanceSheet() {
    if (!this.balanceDate) return;
    this.http.get(`${this.api}/co/documents/balance-sheet`, { params: { date: this.balanceDate } })
      .subscribe(res => this.balanceSheet = res);
  }

  loadIncomeStatement() {
    if (!this.startDate || !this.endDate) return;
    this.http.get(`${this.api}/co/documents/income-statement`, { params: { startDate: this.startDate, endDate: this.endDate } })
      .subscribe(res => this.incomeStatement = res);
  }

  createDocument() {
    if (!this.documentJson) return;
    let payload: any;
    try { payload = JSON.parse(this.documentJson); } catch { alert('JSON inválido'); return; }
    this.http.post(`${this.api}/co/documents`, payload)
      .subscribe(res => this.created = res);
  }

  postDocument() {
    if (!this.postId) return;
    this.http.post(`${this.api}/co/documents/${this.postId}/post`, {})
      .subscribe(res => this.posted = res);
  }
}
