import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../core/services/auth.service';
import { environment } from '../../environments/environment';

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
  imports: [CommonModule, RouterModule],
  template: `
    <div class="dashboard-container">
      <header class="dashboard-header">
        <h1>SAP ERP - Dashboard</h1>
        <div class="user-info">
          <span>Bem-vindo, {{ user?.name }}</span>
          <button (click)="logout()" class="btn-logout">Sair</button>
        </div>
      </header>
      
      <main class="dashboard-content">
        <div class="kpi-grid">
          <div class="kpi-card revenue">
            <div class="kpi-icon">💰</div>
            <div class="kpi-content">
              <h3>Receita do Mês</h3>
              <p class="kpi-value">{{ formatCurrency(dashboardData?.monthlyRevenue || 0) }}</p>
            </div>
          </div>
          
          <div class="kpi-card expense">
            <div class="kpi-icon">💸</div>
            <div class="kpi-content">
              <h3>Despesas do Mês</h3>
              <p class="kpi-value">{{ formatCurrency(dashboardData?.monthlyExpenses || 0) }}</p>
            </div>
          </div>
          
          <div class="kpi-card profit">
            <div class="kpi-icon">📊</div>
            <div class="kpi-content">
              <h3>Lucro do Mês</h3>
              <p class="kpi-value">{{ formatCurrency(dashboardData?.monthlyProfit || 0) }}</p>
            </div>
          </div>
          
          <div class="kpi-card sales">
            <div class="kpi-icon">🛒</div>
            <div class="kpi-content">
              <h3>Pedidos de Venda</h3>
              <p class="kpi-value">{{ dashboardData?.salesOrdersCount || 0 }}</p>
            </div>
          </div>
          
          <div class="kpi-card purchase">
            <div class="kpi-icon">📦</div>
            <div class="kpi-content">
              <h3>Pedidos de Compra</h3>
              <p class="kpi-value">{{ dashboardData?.purchaseOrdersCount || 0 }}</p>
            </div>
          </div>
          
          <div class="kpi-card stock">
            <div class="kpi-icon">⚠️</div>
            <div class="kpi-content">
              <h3>Estoque Baixo</h3>
              <p class="kpi-value">{{ dashboardData?.lowStockProducts || 0 }}</p>
            </div>
          </div>
        </div>
        
        <div class="modules-grid">
          <div class="module-card" (click)="navigateTo('/fi')">
            <h3>💰 Financeiro (FI)</h3>
            <p>Contas a pagar/receber, fluxo de caixa</p>
          </div>
          
          <div class="module-card" (click)="navigateTo('/co')">
            <h3>📋 Contábil (CO)</h3>
            <p>Lançamentos, balancete, DRE, balanço</p>
          </div>
          
          <div class="module-card" (click)="navigateTo('/mm')">
            <h3>🛒 Compras (MM)</h3>
            <p>Requisições, cotações, pedidos</p>
          </div>
          
          <div class="module-card" (click)="navigateTo('/sd')">
            <h3>💼 Vendas (SD)</h3>
            <p>Orçamentos, pedidos, notas fiscais</p>
          </div>
          
          <div class="module-card" (click)="navigateTo('/wm')">
            <h3>📦 Estoque (WM)</h3>
            <p>Produtos, movimentações, inventário</p>
          </div>
          
          <div class="module-card" (click)="navigateTo('/hcm')">
            <h3>👥 RH (HCM)</h3>
            <p>Funcionários, folha, ponto</p>
          </div>
        </div>
      </main>
    </div>
  `,
  styles: [`
    .dashboard-container {
      min-height: 100vh;
      background: #f5f5f5;
    }
    
    .dashboard-header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 1.5rem 2rem;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .dashboard-header h1 {
      margin: 0;
      font-size: 1.5rem;
    }
    
    .user-info {
      display: flex;
      align-items: center;
      gap: 1rem;
    }
    
    .btn-logout {
      padding: 0.5rem 1rem;
      background: rgba(255,255,255,0.2);
      color: white;
      border: 1px solid rgba(255,255,255,0.3);
      border-radius: 4px;
      cursor: pointer;
      transition: background 0.3s;
    }
    
    .btn-logout:hover {
      background: rgba(255,255,255,0.3);
    }
    
    .dashboard-content {
      padding: 2rem;
      max-width: 1400px;
      margin: 0 auto;
    }
    
    .kpi-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 1.5rem;
      margin-bottom: 2rem;
    }
    
    .kpi-card {
      background: white;
      padding: 1.5rem;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      display: flex;
      align-items: center;
      gap: 1rem;
      transition: transform 0.2s, box-shadow 0.2s;
    }
    
    .kpi-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0,0,0,0.15);
    }
    
    .kpi-icon {
      font-size: 2.5rem;
    }
    
    .kpi-content h3 {
      margin: 0 0 0.5rem 0;
      color: #666;
      font-size: 0.9rem;
      font-weight: 500;
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
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 1.5rem;
    }
    
    .module-card {
      background: white;
      padding: 2rem;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      cursor: pointer;
      transition: transform 0.2s, box-shadow 0.2s;
      border-left: 4px solid #667eea;
    }
    
    .module-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0,0,0,0.15);
    }
    
    .module-card h3 {
      margin: 0 0 0.5rem 0;
      color: #333;
    }
    
    .module-card p {
      margin: 0;
      color: #666;
      font-size: 0.9rem;
    }
  `]
})
export class DashboardComponent implements OnInit {
  user: any = null;
  dashboardData: DashboardData | null = null;
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
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
      error: (err) => {
        console.error('Erro ao carregar dashboard:', err);
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

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  navigateTo(path: string) {
    // TODO: Implementar navegação para módulos
    console.log('Navegar para:', path);
  }
}
