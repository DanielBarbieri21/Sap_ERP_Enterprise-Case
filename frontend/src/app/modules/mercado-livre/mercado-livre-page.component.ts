import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MercadoLivreCardComponent } from './mercado-livre-card.component';
import { EnterpriseShellComponent } from '../../shared/shell/enterprise-shell.component';

@Component({
  selector: 'app-mercado-livre-page',
  standalone: true,
  imports: [CommonModule, MercadoLivreCardComponent, EnterpriseShellComponent],
  template: `
    <app-enterprise-shell
      section="Integracoes"
      title="Mercado Livre"
      subtitle="Consulta de anuncios e comparativos para pecas automotivas em marketplaces."
    >
      <app-mercado-livre-card></app-mercado-livre-card>
    </app-enterprise-shell>
  `
})
export class MercadoLivrePageComponent {}
