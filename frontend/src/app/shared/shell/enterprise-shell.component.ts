import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

type NavItem = {
  label: string;
  route: string;
  tag: string;
};

@Component({
  selector: 'app-enterprise-shell',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <div class="shell-layout">
      <aside class="sidebar">
        <div class="brand">
          <div class="brand-badge">ERP</div>
          <div>
            <p class="brand-label">Enterprise Case</p>
            <h1>SAP ERP</h1>
          </div>
        </div>

        <nav class="nav">
          <a
            *ngFor="let item of navItems"
            [routerLink]="item.route"
            routerLinkActive="active"
            class="nav-item"
          >
            <span class="nav-tag">{{ item.tag }}</span>
            <span>{{ item.label }}</span>
          </a>
        </nav>

        <div class="sidebar-footer">
          <div class="user-card">
            <strong>{{ user?.name || 'Usuario autenticado' }}</strong>
            <span>{{ user?.companyName || 'Tenant atual' }}</span>
          </div>
          <button type="button" class="logout-button" (click)="logout()">Sair</button>
        </div>
      </aside>

      <div class="content-area">
        <header class="topbar">
          <div>
            <p class="section-tag">{{ section }}</p>
            <h2>{{ title }}</h2>
            <p class="subtitle" *ngIf="subtitle">{{ subtitle }}</p>
          </div>
        </header>

        <main class="page-content">
          <ng-content></ng-content>
        </main>
      </div>
    </div>
  `,
  styles: [`
    .shell-layout {
      min-height: 100vh;
      display: grid;
      grid-template-columns: 280px 1fr;
    }

    .sidebar {
      background: linear-gradient(180deg, #0f172a 0%, #111827 100%);
      color: #f8fafc;
      padding: 1.5rem;
      display: flex;
      flex-direction: column;
      gap: 1.5rem;
      border-right: 1px solid rgba(255, 255, 255, 0.06);
    }

    .brand {
      display: flex;
      gap: 0.9rem;
      align-items: center;
    }

    .brand-badge {
      width: 3rem;
      height: 3rem;
      border-radius: 1rem;
      display: grid;
      place-items: center;
      background: linear-gradient(135deg, #d6a022 0%, #9f6c00 100%);
      color: #111827;
      font-weight: 800;
      letter-spacing: 0.08em;
    }

    .brand-label,
    .section-tag {
      text-transform: uppercase;
      letter-spacing: 0.16em;
      font-size: 0.72rem;
      color: #d6a022;
      margin: 0 0 0.35rem 0;
      font-weight: 700;
    }

    .brand h1,
    .topbar h2 {
      margin: 0;
    }

    .nav {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }

    .nav-item {
      display: flex;
      align-items: center;
      gap: 0.8rem;
      padding: 0.9rem 1rem;
      border-radius: 1rem;
      color: rgba(248, 250, 252, 0.84);
      text-decoration: none;
      transition: background 0.2s, transform 0.2s, color 0.2s;
    }

    .nav-item:hover,
    .nav-item.active {
      background: rgba(255, 255, 255, 0.08);
      color: #ffffff;
      transform: translateX(2px);
    }

    .nav-tag {
      min-width: 2.75rem;
      padding: 0.2rem 0.45rem;
      border-radius: 999px;
      background: rgba(214, 160, 34, 0.14);
      color: #f8fafc;
      font-size: 0.7rem;
      text-align: center;
      letter-spacing: 0.08em;
      font-weight: 700;
    }

    .sidebar-footer {
      margin-top: auto;
      display: flex;
      flex-direction: column;
      gap: 0.8rem;
    }

    .user-card {
      display: flex;
      flex-direction: column;
      gap: 0.2rem;
      padding: 1rem;
      border-radius: 1rem;
      background: rgba(255, 255, 255, 0.06);
      color: rgba(248, 250, 252, 0.82);
    }

    .logout-button {
      border: 1px solid rgba(255, 255, 255, 0.14);
      background: transparent;
      color: #f8fafc;
      border-radius: 0.9rem;
      padding: 0.85rem 1rem;
      cursor: pointer;
      transition: background 0.2s;
    }

    .logout-button:hover {
      background: rgba(255, 255, 255, 0.08);
    }

    .content-area {
      display: flex;
      flex-direction: column;
      min-width: 0;
    }

    .topbar {
      padding: 1.75rem 2rem 1rem;
    }

    .subtitle {
      margin: 0.55rem 0 0;
      color: #4b5563;
      max-width: 900px;
      line-height: 1.6;
    }

    .page-content {
      padding: 0 2rem 2rem;
    }

    @media (max-width: 1100px) {
      .shell-layout {
        grid-template-columns: 1fr;
      }

      .sidebar {
        border-right: none;
        border-bottom: 1px solid rgba(255, 255, 255, 0.06);
      }

      .nav {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
      }
    }
  `]
})
export class EnterpriseShellComponent {
  @Input() title = '';
  @Input() subtitle = '';
  @Input() section = 'Modulo';

  user: ReturnType<AuthService['getUser']> = null;

  readonly navItems: NavItem[] = [
    { label: 'Dashboard', route: '/dashboard', tag: 'HOME' },
    { label: 'Financeiro', route: '/fi', tag: 'FI' },
    { label: 'Contabil', route: '/co', tag: 'CO' },
    { label: 'Compras', route: '/mm', tag: 'MM' },
    { label: 'Vendas', route: '/sd', tag: 'SD' },
    { label: 'Estoque', route: '/wm', tag: 'WM' },
    { label: 'RH', route: '/hcm', tag: 'HCM' },
    { label: 'Consulta FIPE', route: '/fipe', tag: 'EXT' },
    { label: 'Mercado Livre', route: '/mercado-livre', tag: 'EXT' },
    { label: 'Compras Integradas', route: '/compras', tag: 'OPS' }
  ];

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.user = this.authService.getUser();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
