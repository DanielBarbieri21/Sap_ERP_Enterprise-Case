import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FipeSearchComponent } from './fipe-search.component';
import { EnterpriseShellComponent } from '../../shared/shell/enterprise-shell.component';

@Component({
  selector: 'app-fipe-page',
  standalone: true,
  imports: [CommonModule, FipeSearchComponent, EnterpriseShellComponent],
  template: `
    <app-enterprise-shell
      section="Integracoes"
      title="Consulta FIPE"
      subtitle="Consulta de referencias automotivas para apoiar cenarios comerciais, compras e validacao de mercado."
    >
      <app-fipe-search></app-fipe-search>
    </app-enterprise-shell>
  `
})
export class FipePageComponent {}
