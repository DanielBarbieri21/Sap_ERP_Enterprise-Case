import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FipeService, FipeBrand, FipeModel, FipeYear, FipeVehicle } from '../../core/services/fipe.service';

@Component({
  selector: 'app-fipe-search',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="fipe-search-card bg-white dark:bg-slate-900 rounded-lg shadow-md p-6 border-l-4 border-yellow-500">
      <h2 class="text-xl font-bold text-black dark:text-white mb-4 flex items-center gap-2">
        <span class="text-2xl">🚗</span>
        Consultar Veículo (FIPE)
      </h2>

      <div class="space-y-4">
        <!-- Selecionar Marca -->
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Marca do Veículo
          </label>
          <select
            [(ngModel)]="selectedBrandId"
            (change)="onBrandChange()"
            class="w-full px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md dark:bg-slate-800 dark:text-white"
          >
            <option value="">-- Selecione uma marca --</option>
            <option *ngFor="let brand of brands" [value]="brand.id">
              {{ brand.name }}
            </option>
          </select>
        </div>

        <!-- Selecionar Modelo -->
        <div *ngIf="selectedBrandId">
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Modelo
          </label>
          <select
            [(ngModel)]="selectedModelId"
            (change)="onModelChange()"
            class="w-full px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md dark:bg-slate-800 dark:text-white"
            [disabled]="models.length === 0"
          >
            <option value="">-- Selecione um modelo --</option>
            <option *ngFor="let model of models" [value]="model.id">
              {{ model.name }}
            </option>
          </select>
        </div>

        <!-- Selecionar Ano -->
        <div *ngIf="selectedModelId">
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Ano
          </label>
          <select
            [(ngModel)]="selectedYearId"
            (change)="onYearChange()"
            class="w-full px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md dark:bg-slate-800 dark:text-white"
            [disabled]="years.length === 0"
          >
            <option value="">-- Selecione um ano --</option>
            <option *ngFor="let year of years" [value]="year.id">
              {{ year.name }}
            </option>
          </select>
        </div>

        <!-- Resultado FIPE -->
        <div *ngIf="vehicleData" class="bg-gradient-to-r from-yellow-50 to-yellow-100 dark:from-slate-800 dark:to-slate-700 rounded-lg p-4 border border-yellow-200">
          <h3 class="font-bold text-black dark:text-white mb-3">Dados do Veículo:</h3>
          <div class="grid grid-cols-2 gap-3 text-sm">
            <div>
              <span class="font-semibold text-gray-700 dark:text-gray-300">Marca:</span>
              <p class="text-black dark:text-white">{{ vehicleData.brand }}</p>
            </div>
            <div>
              <span class="font-semibold text-gray-700 dark:text-gray-300">Modelo:</span>
              <p class="text-black dark:text-white">{{ vehicleData.model }}</p>
            </div>
            <div>
              <span class="font-semibold text-gray-700 dark:text-gray-300">Ano:</span>
              <p class="text-black dark:text-white">{{ vehicleData.modelYear }}</p>
            </div>
            <div>
              <span class="font-semibold text-gray-700 dark:text-gray-300">Combustível:</span>
              <p class="text-black dark:text-white">{{ vehicleData.fuel }}</p>
            </div>
            <div class="col-span-2">
              <span class="font-semibold text-gray-700 dark:text-gray-300">Valor Referência:</span>
              <p class="text-lg font-bold text-green-600 dark:text-green-400">{{ vehicleData.value }}</p>
            </div>
          </div>
        </div>

        <!-- Loading -->
        <div *ngIf="loading" class="text-center py-4">
          <span class="inline-block animate-spin">⏳</span> Carregando dados...
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class FipeSearchComponent implements OnInit {
  brands: FipeBrand[] = [];
  models: FipeModel[] = [];
  years: FipeYear[] = [];
  vehicleData: FipeVehicle | null = null;

  selectedBrandId: any = '';
  selectedModelId: any = '';
  selectedYearId: any = '';
  loading = false;

  constructor(private fipeService: FipeService) {}

  ngOnInit() {
    this.loadBrands();
  }

  loadBrands() {
    this.loading = true;
    this.fipeService.getBrands().subscribe({
      next: (data) => {
        this.brands = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar marcas:', err);
        this.loading = false;
      }
    });
  }

  onBrandChange() {
    if (this.selectedBrandId) {
      this.loading = true;
      this.models = [];
      this.years = [];
      this.vehicleData = null;
      this.selectedModelId = '';
      this.selectedYearId = '';

      this.fipeService.getModels(this.selectedBrandId).subscribe({
        next: (data) => {
          this.models = data;
          this.loading = false;
        },
        error: (err) => {
          console.error('Erro ao carregar modelos:', err);
          this.loading = false;
        }
      });
    }
  }

  onModelChange() {
    if (this.selectedModelId && this.selectedBrandId) {
      this.loading = true;
      this.years = [];
      this.vehicleData = null;
      this.selectedYearId = '';

      this.fipeService.getYears(this.selectedBrandId, this.selectedModelId).subscribe({
        next: (data) => {
          this.years = data;
          this.loading = false;
        },
        error: (err) => {
          console.error('Erro ao carregar anos:', err);
          this.loading = false;
        }
      });
    }
  }

  onYearChange() {
    if (this.selectedYearId && this.selectedBrandId && this.selectedModelId) {
      this.loading = true;
      this.vehicleData = null;

      this.fipeService
        .getVehicleData(this.selectedBrandId, this.selectedModelId, this.selectedYearId)
        .subscribe({
          next: (data) => {
            this.vehicleData = data;
            this.loading = false;
          },
          error: (err) => {
            console.error('Erro ao carregar dados do veículo:', err);
            this.loading = false;
          }
        });
    }
  }
}
