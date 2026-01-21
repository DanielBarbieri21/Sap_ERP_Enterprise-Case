import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-hcm',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, FormsModule],
  template: `
    <div class="module-page">
      <h2>RH (HCM)</h2>
      <a routerLink="/dashboard">Voltar ao Dashboard</a>

      <section class="card">
        <h3>Novo Funcionário</h3>
        <textarea [(ngModel)]="employeeJson" rows="4" style="width:100%" placeholder='{"name":"Fulano","email":"fulano@empresa.com","role":"Analista"}'></textarea>
        <button (click)="createEmployee()">Criar</button>
        <pre>{{ createdEmployee | json }}</pre>
      </section>

      <section class="card">
        <h3>Folha de Pagamento</h3>
        <textarea [(ngModel)]="payrollJson" rows="4" style="width:100%" placeholder='{"employeeId":"...","date":"2026-01-21","amount":5000}'></textarea>
        <button (click)="createPayroll()">Gerar</button>
        <pre>{{ createdPayroll | json }}</pre>
      </section>

      <section class="card">
        <h3>Registro de Ponto</h3>
        <textarea [(ngModel)]="timeJson" rows="4" style="width:100%" placeholder='{"employeeId":"...","date":"2026-01-21","type":"IN","time":"08:00"}'></textarea>
        <button (click)="createTimeRecord()">Registrar</button>
        <pre>{{ createdTime | json }}</pre>
      </section>

      <section class="card">
        <h3>Ponto por Funcionário</h3>
        <div class="row">
          <label>Emp ID: <input [(ngModel)]="empId"></label>
          <label>Início: <input type="date" [(ngModel)]="startDate"></label>
          <label>Fim: <input type="date" [(ngModel)]="endDate"></label>
          <button (click)="loadTimeRecords()">Carregar</button>
        </div>
        <pre>{{ timeRecords | json }}</pre>
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
export class HcmComponent {
  api = environment.apiUrl;
  employeeJson = '';
  payrollJson = '';
  timeJson = '';
  empId = '';
  startDate = '';
  endDate = '';
  createdEmployee: any = null;
  createdPayroll: any = null;
  createdTime: any = null;
  timeRecords: any = null;

  constructor(private http: HttpClient) {}

  createEmployee() {
    let payload: any; try { payload = JSON.parse(this.employeeJson); } catch { alert('JSON inválido'); return; }
    this.http.post(`${this.api}/hcm/employees`, payload).subscribe(res => this.createdEmployee = res);
  }

  createPayroll() {
    let payload: any; try { payload = JSON.parse(this.payrollJson); } catch { alert('JSON inválido'); return; }
    this.http.post(`${this.api}/hcm/payrolls`, payload).subscribe(res => this.createdPayroll = res);
  }

  createTimeRecord() {
    let payload: any; try { payload = JSON.parse(this.timeJson); } catch { alert('JSON inválido'); return; }
    this.http.post(`${this.api}/hcm/time-records`, payload).subscribe(res => this.createdTime = res);
  }

  loadTimeRecords() {
    if (!this.empId || !this.startDate || !this.endDate) return;
    this.http.get(`${this.api}/hcm/employees/${this.empId}/time-records`, { params: { startDate: this.startDate, endDate: this.endDate } })
      .subscribe(res => this.timeRecords = res);
  }
}
