import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { EnterpriseShellComponent } from '../../shared/shell/enterprise-shell.component';

@Component({
  selector: 'app-hcm',
  standalone: true,
  imports: [CommonModule, FormsModule, EnterpriseShellComponent],
  template: `
    <app-enterprise-shell
      section="HCM"
      title="Recursos humanos"
      subtitle="Operacoes de cadastro, folha e ponto para demonstracao do modulo de pessoas."
    >
      <section class="card">
        <h3>Novo funcionario</h3>
        <textarea [(ngModel)]="employeeJson" rows="5"></textarea>
        <div class="toolbar">
          <button (click)="createEmployee()">Criar funcionario</button>
        </div>
        <pre>{{ createdEmployee | json }}</pre>
      </section>

      <section class="card">
        <h3>Folha de pagamento</h3>
        <textarea [(ngModel)]="payrollJson" rows="5"></textarea>
        <div class="toolbar">
          <button (click)="createPayroll()">Gerar folha</button>
        </div>
        <pre>{{ createdPayroll | json }}</pre>
      </section>

      <section class="card">
        <h3>Registro de ponto</h3>
        <textarea [(ngModel)]="timeJson" rows="5"></textarea>
        <div class="toolbar">
          <button (click)="createTimeRecord()">Registrar ponto</button>
        </div>
        <pre>{{ createdTime | json }}</pre>
      </section>
    </app-enterprise-shell>
  `,
  styles: [`
    .card { background: rgba(255,255,255,0.9); padding: 1.5rem; margin-bottom: 1.5rem; border-radius: 20px; box-shadow: 0 18px 42px rgba(17,24,39,0.08); border: 1px solid rgba(17,24,39,0.06); }
    .toolbar { display:flex; gap:1rem; flex-wrap:wrap; align-items:end; margin-top:1rem; }
    textarea, button { border-radius:12px; border:1px solid rgba(17,24,39,0.12); padding:.8rem .9rem; background:white; width:100%; }
    button { width:auto; background:#111827; color:white; cursor:pointer; }
  `]
})
export class HcmComponent {
  api = environment.apiUrl;
  employeeJson = '';
  payrollJson = '';
  timeJson = '';
  createdEmployee: any = null;
  createdPayroll: any = null;
  createdTime: any = null;

  constructor(private http: HttpClient) {}

  createEmployee() {
    let payload: any; try { payload = JSON.parse(this.employeeJson); } catch { alert('JSON invalido'); return; }
    this.http.post(`${this.api}/hcm/employees`, payload).subscribe((res) => this.createdEmployee = res);
  }

  createPayroll() {
    let payload: any; try { payload = JSON.parse(this.payrollJson); } catch { alert('JSON invalido'); return; }
    this.http.post(`${this.api}/hcm/payrolls`, payload).subscribe((res) => this.createdPayroll = res);
  }

  createTimeRecord() {
    let payload: any; try { payload = JSON.parse(this.timeJson); } catch { alert('JSON invalido'); return; }
    this.http.post(`${this.api}/hcm/time-records`, payload).subscribe((res) => this.createdTime = res);
  }
}
