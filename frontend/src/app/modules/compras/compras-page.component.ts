import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ComprasCardComponent } from './compras-card.component';
import { EnterpriseShellComponent } from '../../shared/shell/enterprise-shell.component';

@Component({
  selector: 'app-compras-page',
  standalone: true,
  imports: [CommonModule, ComprasCardComponent, EnterpriseShellComponent],
  template: `
    <app-enterprise-shell
      section="OPS"
      title="Compras integradas"
      subtitle="Camada complementar para demonstrar gestao operacional de compras e fornecedores."
    >
      <app-compras-card></app-compras-card>
    </app-enterprise-shell>
  `
})
export class ComprasPageComponent {}
