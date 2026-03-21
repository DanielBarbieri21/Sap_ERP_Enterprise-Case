import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

export interface PurchaseOrder {
  id?: string;
  supplier: string;
  itemCode: string;
  itemDescription: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  status: 'pending' | 'ordered' | 'received' | 'cancelled';
  createdDate: string;
  estimatedDelivery?: string;
  notes?: string;
}

@Component({
  selector: 'app-compras-card',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  template: `
    <div class="compras-card bg-white dark:bg-slate-900 rounded-lg shadow-md p-6 border-l-4 border-yellow-500">
      <h2 class="text-xl font-bold text-black dark:text-white mb-4 flex items-center gap-2">
        <span class="text-2xl">📦</span>
        Sistema de Compras - Integração
      </h2>

      <div class="space-y-6">
        <!-- Form Nova Compra -->
        <div class="bg-yellow-50 dark:bg-slate-800 rounded-lg p-4">
          <h3 class="font-semibold text-black dark:text-white mb-3">Nova Ordem de Compra</h3>
          <form [formGroup]="purchaseForm" (ngSubmit)="addPurchase()" class="space-y-3">
            <div class="grid grid-cols-2 gap-3">
              <input
                formControlName="supplier"
                type="text"
                placeholder="Fornecedor"
                class="px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md dark:bg-slate-700 dark:text-white"
              />
              <input
                formControlName="itemCode"
                type="text"
                placeholder="Código da Peça"
                class="px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md dark:bg-slate-700 dark:text-white"
              />
              <input
                formControlName="itemDescription"
                type="text"
                placeholder="Descrição da Peça"
                class="col-span-2 px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md dark:bg-slate-700 dark:text-white"
              />
              <input
                formControlName="quantity"
                type="number"
                placeholder="Quantidade"
                min="1"
                class="px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md dark:bg-slate-700 dark:text-white"
              />
              <input
                formControlName="unitPrice"
                type="number"
                placeholder="Preço Unitário"
                step="0.01"
                class="px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md dark:bg-slate-700 dark:text-white"
              />
              <input
                formControlName="estimatedDelivery"
                type="date"
                class="col-span-2 px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md dark:bg-slate-700 dark:text-white"
              />
              <textarea
                formControlName="notes"
                placeholder="Observações (opcional)"
                rows="2"
                class="col-span-2 px-3 py-2 border border-gray-300 dark:border-slate-600 rounded-md dark:bg-slate-700 dark:text-white"
              ></textarea>
            </div>
            <button
              type="submit"
              class="w-full px-4 py-2 bg-yellow-500 hover:bg-yellow-600 text-white font-medium rounded-md transition"
            >
              ➕ Adicionar Compra
            </button>
          </form>
        </div>

        <!-- Lista de Compras -->
        <div>
          <h3 class="font-semibold text-black dark:text-white mb-3">Ordens de Compra</h3>
          <div class="space-y-2 max-h-80 overflow-y-auto">
            <div *ngIf="purchases.length === 0" class="text-center py-4 text-gray-500 dark:text-gray-400">
              Nenhuma ordem de compra adicionada.
            </div>
            <div *ngFor="let purchase of purchases" class="border border-yellow-200 dark:border-slate-700 rounded-lg p-3">
              <div class="flex justify-between items-start mb-2">
                <div>
                  <h4 class="font-semibold text-black dark:text-white">{{ purchase.itemDescription }}</h4>
                  <p class="text-sm text-gray-600 dark:text-gray-400">Código: {{ purchase.itemCode }}</p>
                  <p class="text-sm text-gray-600 dark:text-gray-400">Fornecedor: {{ purchase.supplier }}</p>
                </div>
                <span [ngClass]="getStatusClass(purchase.status)" class="px-2 py-1 text-xs font-semibold rounded">
                  {{ getStatusLabel(purchase.status) }}
                </span>
              </div>
              <div class="grid grid-cols-2 gap-2 text-sm mb-2">
                <div>
                  <span class="text-gray-600 dark:text-gray-400">Quantidade:</span>
                  <p class="font-semibold text-black dark:text-white">{{ purchase.quantity }} un.</p>
                </div>
                <div>
                  <span class="text-gray-600 dark:text-gray-400">Unitário:</span>
                  <p class="font-semibold text-black dark:text-white">{{ purchase.unitPrice | currency: 'BRL' }}</p>
                </div>
                <div class="col-span-2">
                  <span class="text-gray-600 dark:text-gray-400">Total:</span>
                  <p class="font-bold text-lg text-green-600 dark:text-green-400">
                    {{ purchase.totalPrice | currency: 'BRL' }}
                  </p>
                </div>
              </div>
              <div *ngIf="purchase.estimatedDelivery" class="text-xs text-gray-600 dark:text-gray-400 mb-2">
                Entrega Estimada: {{ purchase.estimatedDelivery }}
              </div>
              <div *ngIf="purchase.notes" class="text-xs italic text-gray-600 dark:text-gray-400 mb-2 bg-gray-100 dark:bg-slate-800 p-2 rounded">
                {{ purchase.notes }}
              </div>
              <div class="flex gap-2">
                <button
                  (click)="updateStatus(purchase, 'ordered')"
                  class="flex-1 px-2 py-1 text-xs bg-blue-500 hover:bg-blue-600 text-white rounded"
                >
                  Pedido Realizado
                </button>
                <button
                  (click)="updateStatus(purchase, 'received')"
                  class="flex-1 px-2 py-1 text-xs bg-green-500 hover:bg-green-600 text-white rounded"
                >
                  Recebido
                </button>
                <button
                  (click)="removePurchase(purchase.id!)"
                  class="flex-1 px-2 py-1 text-xs bg-red-500 hover:bg-red-600 text-white rounded"
                >
                  ✕ Remover
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Resumo Total -->
        <div class="bg-gradient-to-r from-yellow-50 to-yellow-100 dark:from-slate-800 dark:to-slate-700 rounded-lg p-4 border border-yellow-200">
          <h4 class="font-bold text-black dark:text-white mb-2">Resumo Financeiro</h4>
          <div class="grid grid-cols-3 gap-2 text-sm">
            <div>
              <span class="text-gray-700 dark:text-gray-300">Total Itens:</span>
              <p class="text-lg font-bold text-black dark:text-white">{{ getTotalItems() }}</p>
            </div>
            <div>
              <span class="text-gray-700 dark:text-gray-300">Pendentes:</span>
              <p class="text-lg font-bold text-yellow-600 dark:text-yellow-400">{{ getPendingCount() }}</p>
            </div>
            <div>
              <span class="text-gray-700 dark:text-gray-300">Total Investido:</span>
              <p class="text-lg font-bold text-green-600 dark:text-green-400">
                {{ getTotalInvested() | currency: 'BRL' }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class ComprasCardComponent implements OnInit {
  purchaseForm: FormGroup;
  purchases: PurchaseOrder[] = [];

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.purchaseForm = this.fb.group({
      supplier: [''],
      itemCode: [''],
      itemDescription: [''],
      quantity: [1],
      unitPrice: [0],
      estimatedDelivery: [''],
      notes: ['']
    });
  }

  ngOnInit() {
    this.loadPurchases();
  }

  addPurchase() {
    const formData = this.purchaseForm.value;
    const totalPrice = formData.quantity * formData.unitPrice;

    const purchase: PurchaseOrder = {
      id: Date.now().toString(),
      ...formData,
      totalPrice,
      status: 'pending',
      createdDate: new Date().toISOString().split('T')[0]
    };

    this.purchases.push(purchase);
    this.purchaseForm.reset({ quantity: 1, unitPrice: 0 });
    this.savePurchases();
  }

  removePurchase(id: string) {
    this.purchases = this.purchases.filter(p => p.id !== id);
    this.savePurchases();
  }

  updateStatus(purchase: PurchaseOrder, newStatus: PurchaseOrder['status']) {
    const index = this.purchases.findIndex(p => p.id === purchase.id);
    if (index > -1) {
      this.purchases[index].status = newStatus;
      this.savePurchases();
    }
  }

  getTotalItems(): number {
    return this.purchases.reduce((sum, p) => sum + p.quantity, 0);
  }

  getTotalInvested(): number {
    return this.purchases.reduce((sum, p) => sum + p.totalPrice, 0);
  }

  getPendingCount(): number {
    return this.purchases.filter(p => p.status === 'pending').length;
  }

  getStatusLabel(status: string): string {
    const labels: { [key: string]: string } = {
      pending: 'Pendente',
      ordered: 'Pedido Realizado',
      received: 'Recebido',
      cancelled: 'Cancelado'
    };
    return labels[status] || status;
  }

  getStatusClass(status: string): string {
    const classes: { [key: string]: string } = {
      pending: 'bg-yellow-200 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200',
      ordered: 'bg-blue-200 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
      received: 'bg-green-200 text-green-800 dark:bg-green-900 dark:text-green-200',
      cancelled: 'bg-red-200 text-red-800 dark:bg-red-900 dark:text-red-200'
    };
    return classes[status] || '';
  }

  savePurchases() {
    localStorage.setItem('purchases', JSON.stringify(this.purchases));
  }

  loadPurchases() {
    const saved = localStorage.getItem('purchases');
    if (saved) {
      this.purchases = JSON.parse(saved);
    }
  }
}
