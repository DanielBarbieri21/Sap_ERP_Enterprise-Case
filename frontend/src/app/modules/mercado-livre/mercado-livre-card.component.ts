import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MercadoLivreService, MercadoLivreListing } from '../../core/services/mercado-livre.service';

@Component({
  selector: 'app-mercado-livre-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="mercado-livre-card bg-white dark:bg-slate-900 rounded-lg shadow-md p-6 border-l-4 border-yellow-500">
      <h2 class="text-xl font-bold text-black dark:text-white mb-4 flex items-center gap-2">
        <span class="text-2xl">💳</span>
        Mercado Livre - Auto Peças
      </h2>

      <div class="space-y-4">
        <!-- Campo de Busca -->
        <div class="flex gap-2">
          <input
            [(ngModel)]="searchQuery"
            type="text"
            placeholder="Buscar peças no Mercado Livre..."
            class="flex-1 px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md dark:bg-slate-800 dark:text-white"
            (keyup.enter)="search()"
          />
          <button
            (click)="search()"
            [disabled]="loading"
            class="px-4 py-2 bg-yellow-500 hover:bg-yellow-600 text-white font-medium rounded-md disabled:opacity-50"
          >
            {{ loading ? '⏳' : '🔍' }} Buscar
          </button>
        </div>

        <!-- Resultados -->
        <div *ngIf="results.length > 0" class="space-y-3 max-h-96 overflow-y-auto">
          <div *ngFor="let item of results" class="border border-yellow-200 rounded-lg p-3 hover:bg-yellow-50 dark:hover:bg-slate-800">
            <div class="flex gap-3">
              <img
                *ngIf="item.thumbnail"
                [src]="item.thumbnail"
                alt="{{ item.title }}"
                class="w-16 h-16 object-cover rounded"
              />
              <div class="flex-1">
                <h4 class="font-semibold text-black dark:text-white line-clamp-2">
                  {{ item.title }}
                </h4>
                <p class="text-lg font-bold text-green-600 dark:text-green-400">
                  {{ item.price | currency: 'BRL' }}
                </p>
                <p class="text-xs text-gray-600 dark:text-gray-400 mt-1">
                  Vendedor: {{ item.seller?.nickname || 'Anônimo' }}
                </p>
                <p class="text-xs text-gray-600 dark:text-gray-400">
                  Disponível: {{ item.available_quantity }} un.
                </p>
                <a
                  [href]="item.permalink"
                  target="_blank"
                  class="text-xs text-blue-500 hover:underline mt-1 inline-block"
                >
                  Ver no Mercado Livre →
                </a>
              </div>
            </div>
          </div>
        </div>

        <!-- Mensagem de vazio -->
        <div *ngIf="!loading && results.length === 0 && hasSearched" class="text-center py-6 text-gray-500 dark:text-gray-400">
          Nenhuma peça encontrada. Tente buscar por outro termo.
        </div>

        <!-- Loading -->
        <div *ngIf="loading" class="text-center py-4">
          <span class="inline-block animate-spin">⏳</span> Buscando produtos...
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class MercadoLivreCardComponent implements OnInit {
  searchQuery = '';
  results: MercadoLivreListing[] = [];
  loading = false;
  hasSearched = false;

  constructor(private mercadoLivreService: MercadoLivreService) {}

  ngOnInit() {}

  search() {
    if (!this.searchQuery.trim()) return;

    this.loading = true;
    this.hasSearched = true;
    this.results = [];

    this.mercadoLivreService.searchAutoParts(this.searchQuery).subscribe({
      next: (data: any) => {
        this.results = data.results || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao buscar no Mercado Livre:', err);
        this.loading = false;
      }
    });
  }
}
