import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { EnterpriseShellComponent } from '../../shared/shell/enterprise-shell.component';

@Component({
  selector: 'app-co',
  standalone: true,
  imports: [CommonModule, FormsModule, EnterpriseShellComponent],
  template: `
    <app-enterprise-shell
      section="CO"
      title="Contabil"
      subtitle="Acompanhamento de demonstrativos, criacao de documentos e apoio a rotinas contabeis."
    >
      <section class="card">
        <h3>DRE e balancete</h3>
        <div class="toolbar">
          <label>Inicio: <input type="date" [(ngModel)]="startDate"></label>
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
        <h3>Balanco patrimonial</h3>
        <div class="toolbar">
          <label>Data: <input type="date" [(ngModel)]="balanceDate"></label>
          <button (click)="loadBalanceSheet()">Carregar</button>
        </div>
        <pre>{{ balanceSheet | json }}</pre>
      </section>

      <section class="card">
        <h3>Novo documento contabil</h3>
        <textarea [(ngModel)]="documentJson" rows="6"></textarea>
        <div class="toolbar">
          <button (click)="createDocument()">Criar documento</button>
        </div>
        <pre>{{ created | json }}</pre>
      </section>
    </app-enterprise-shell>
  `,
  styles: [`
    .card { background: rgba(255,255,255,0.9); padding: 1.5rem; margin-bottom: 1.5rem; border-radius: 20px; box-shadow: 0 18px 42px rgba(17,24,39,0.08); border: 1px solid rgba(17,24,39,0.06); }
    .toolbar { display:flex; gap:1rem; flex-wrap:wrap; align-items:end; margin-top:1rem; }
    label { display:flex; flex-direction:column; gap:.45rem; }
    input, textarea, button { border-radius:12px; border:1px solid rgba(17,24,39,0.12); padding:.8rem .9rem; background:white; width:100%; }
    button { width:auto; background:#111827; color:white; cursor:pointer; }
    textarea { min-height: 160px; }
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

  constructor(private http: HttpClient) {}

  loadTrialBalance() {
    if (!this.startDate || !this.endDate) return;
    this.http.get(`${this.api}/co/documents/trial-balance`, { params: { startDate: this.startDate, endDate: this.endDate } })
      .subscribe((res) => this.trialBalance = res);
  }

  loadBalanceSheet() {
    if (!this.balanceDate) return;
    this.http.get(`${this.api}/co/documents/balance-sheet`, { params: { date: this.balanceDate } })
      .subscribe((res) => this.balanceSheet = res);
  }

  loadIncomeStatement() {
    if (!this.startDate || !this.endDate) return;
    this.http.get(`${this.api}/co/documents/income-statement`, { params: { startDate: this.startDate, endDate: this.endDate } })
      .subscribe((res) => this.incomeStatement = res);
  }

  createDocument() {
    if (!this.documentJson) return;
    let payload: any;
    try { payload = JSON.parse(this.documentJson); } catch { alert('JSON invalido'); return; }
    this.http.post(`${this.api}/co/documents`, payload).subscribe((res) => this.created = res);
  }
}
