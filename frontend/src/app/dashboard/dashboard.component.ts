import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AuthResponse, AuthService } from '../core/services/auth.service';
import { environment } from '../../environments/environment';
import { EnterpriseShellComponent } from '../shared/shell/enterprise-shell.component';

interface DashboardData {
  monthlyRevenue: number;
  monthlyExpenses: number;
  monthlyProfit: number;
  salesOrdersCount: number;
  purchaseOrdersCount: number;
  lowStockProducts: number;
  period: {
    start: string;
    end: string;
  };
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, EnterpriseShellComponent],
  template: `
    <app-enterprise-shell
      section="Home"
      title="Dashboard executivo"
      subtitle="Visao consolidada do tenant autenticado com foco em performance financeira, operacao de compras, vendas e disponibilidade de estoque."
    >
      <section class="hero-card">
        <div>
          <p class="hero-label">Tenant ativo</p>
          <h3>{{ user?.companyName || 'Empresa atual' }}</h3>
          <p class="hero-copy">
            Monitoramento centralizado para apoiar demonstracoes de portfolio com narrativa enterprise clara e indicadores orientados ao negocio.
          </p>
        </div>
        <div class="hero-badge">
          <span>Stack principal</span>
          <strong>Spring Boot 3.2 + Angular 17</strong>
        </div>
      </section>

      <div *ngIf="loading" class="state-card">Carregando indicadores do dashboard...</div>
      <div *ngIf="!loading && !dashboardData" class="state-card error-state">
        Nao foi possivel carregar os indicadores agora. Tente novamente em instantes.
      </div>

      <div class="kpi-grid" *ngIf="dashboardData">
        <div class="kpi-card revenue">
          <div class="kpi-icon">R$</div>
          <div class="kpi-content">
            <h3>Receita do Mes</h3>
            <p class="kpi-value">{{ formatCurrency(dashboardData.monthlyRevenue) }}</p>
          </div>
        </div>

        <div class="kpi-card expense">
          <div class="kpi-icon">-</div>
          <div class="kpi-content">
            <h3>Despesas do Mes</h3>
            <p class="kpi-value">{{ formatCurrency(dashboardData.monthlyExpenses) }}</p>
          </div>
        </div>

        <div class="kpi-card profit">
          <div class="kpi-icon">%</div>
          <div class="kpi-content">
            <h3>Lucro do Mes</h3>
            <p class="kpi-value">{{ formatCurrency(dashboardData.monthlyProfit) }}</p>
          </div>
        </div>

        <div class="kpi-card sales">
          <div class="kpi-icon">SD</div>
          <div class="kpi-content">
            <h3>Pedidos de Venda</h3>
            <p class="kpi-value">{{ dashboardData.salesOrdersCount }}</p>
          </div>
        </div>

        <div class="kpi-card purchase">
          <div class="kpi-icon">MM</div>
          <div class="kpi-content">
            <h3>Pedidos de Compra</h3>
            <p class="kpi-value">{{ dashboardData.purchaseOrdersCount }}</p>
          </div>
        </div>

        <div class="kpi-card stock">
          <div class="kpi-icon">WM</div>
          <div class="kpi-content">
            <h3>Estoque Baixo</h3>
            <p class="kpi-value">{{ dashboardData.lowStockProducts }}</p>
          </div>
        </div>
      </div>

      <div class="modules-grid">
        <div class="module-card">
          <h3>Arquitetura modular</h3>
          <p>Modulos separados por contexto de negocio para facilitar evolucao e demonstracao tecnica.</p>
        </div>
        <div class="module-card">
          <h3>Seguranca e multi-tenancy</h3>
          <p>JWT, contexto de tenant e contratos mais previsiveis para uso em ambientes reais.</p>
        </div>
        <div class="module-card">
          <h3>Preparado para evoluir</h3>
          <p>Flyway, CI, documentacao e base visual unificada para portfolio e GitHub.</p>
        </div>
      </div>
    </app-enterprise-shell>
  `,
  styles: [`
    .hero-card {
      display: flex;
      justify-content: space-between;
      gap: 1.5rem;
      align-items: center;
      background: rgba(255, 255, 255, 0.9);
      border: 1px solid rgba(17, 24, 39, 0.08);
      border-radius: 24px;
      padding: 1.75rem 1.8rem;
      box-shadow: 0 20px 60px rgba(17, 24, 39, 0.08);
      margin-bottom: 1.5rem;
    }

    .hero-label {
      text-transform: uppercase;
      letter-spacing: 0.14em;
      font-size: 0.74rem;
      color: #9f6c00;
      font-weight: 700;
      margin-bottom: 0.75rem;
    }

    .hero-card h3 {
      margin: 0 0 0.6rem 0;
      font-size: 1.6rem;
      color: #111827;
    }

    .hero-copy {
      margin: 0;
      color: #4b5563;
      max-width: 720px;
      line-height: 1.6;
    }

    .hero-badge {
      min-width: 220px;
      border-radius: 20px;
      background: linear-gradient(135deg, #111827 0%, #273449 100%);
      color: #f8fafc;
      padding: 1.2rem 1.25rem;
      display: flex;
      flex-direction: column;
      gap: 0.45rem;
    }

    .hero-badge span {
      color: rgba(248, 250, 252, 0.68);
      text-transform: uppercase;
      letter-spacing: 0.14em;
      font-size: 0.72rem;
    }

    .state-card {
      background: rgba(255, 255, 255, 0.86);
      border: 1px solid rgba(17, 24, 39, 0.08);
      border-radius: 18px;
      padding: 1rem 1.1rem;
      margin-bottom: 1.5rem;
      color: #374151;
      box-shadow: 0 14px 36px rgba(17, 24, 39, 0.06);
    }

    .error-state {
      color: #991b1b;
      background: rgba(254, 242, 242, 0.92);
    }

    .kpi-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 1.5rem;
      margin-bottom: 2rem;
    }

    .kpi-card,
    .module-card {
      background: rgba(255, 255, 255, 0.88);
      padding: 1.5rem;
      border-radius: 20px;
      box-shadow: 0 20px 48px rgba(17, 24, 39, 0.08);
      border: 1px solid rgba(17, 24, 39, 0.06);
    }

    .kpi-card {
      display: flex;
      align-items: center;
      gap: 1rem;
      transition: transform 0.2s, box-shadow 0.2s;
    }

    .kpi-card:hover,
    .module-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 24px 56px rgba(17, 24, 39, 0.12);
    }

    .kpi-icon {
      width: 3.25rem;
      height: 3.25rem;
      border-radius: 18px;
      background: rgba(17, 24, 39, 0.06);
      display: grid;
      place-items: center;
      font-size: 1rem;
      font-weight: 800;
      letter-spacing: 0.08em;
      color: #111827;
    }

    .kpi-content h3,
    .module-card h3 {
      margin: 0 0 0.5rem 0;
      color: #333;
    }

    .kpi-value {
      margin: 0;
      font-size: 1.8rem;
      font-weight: bold;
      color: #333;
    }

    .kpi-card.revenue .kpi-value { color: #10b981; }
    .kpi-card.expense .kpi-value { color: #ef4444; }
    .kpi-card.profit .kpi-value { color: #3b82f6; }

    .modules-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
      gap: 1.5rem;
    }

    .module-card p {
      margin: 0;
      color: #666;
      line-height: 1.55;
    }

    @media (max-width: 960px) {
      .hero-card {
        flex-direction: column;
        align-items: flex-start;
      }

      .hero-badge {
        min-width: auto;
        width: 100%;
      }
    }
  `]
})
export class DashboardComponent implements OnInit {
  user: AuthResponse | null = null;
  dashboardData: DashboardData | null = null;
  loading = false;

  constructor(
    private authService: AuthService,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.user = this.authService.getUser();
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.loading = true;
    this.http.get<DashboardData>(`${environment.apiUrl}/dashboard/data`).subscribe({
      next: (data) => {
        this.dashboardData = data;
        this.loading = false;
      },
      error: () => {
        this.dashboardData = null;
        this.loading = false;
      }
    });
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value);
  }
}
