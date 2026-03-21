import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface MercadoLivreListing {
  id: string;
  title: string;
  price: number;
  thumbnail: string;
  seller?: {
    nickname?: string;
    seller_reputation: {
      level_id: string;
      power_seller_status: string;
      metrics: {
        sales: number;
        claims: number;
        delayed: number;
      };
    };
  };
  condition: string;
  available_quantity: number;
  permalink: string;
}

@Injectable({
  providedIn: 'root'
})
export class MercadoLivreService {
  private baseUrl = 'https://api.mercadolibre.com/sites/MLB';

  constructor(private http: HttpClient) {}

  /**
   * Busca produtos no Mercado Livre
   */
  searchProducts(query: string, limit: number = 50): Observable<MercadoLivreListing[]> {
    const url = `${this.baseUrl}/search?q=${encodeURIComponent(query)}&limit=${limit}`;
    return this.http
      .get<any>(url)
      .pipe(
        catchError(error => {
          console.error('Erro ao buscar no Mercado Livre:', error);
          return of([]);
        })
      );
  }

  /**
   * Busca detalhes de um produto específico
   */
  getProductDetails(itemId: string): Observable<MercadoLivreListing | null> {
    const url = `${this.baseUrl}/items/${itemId}`;
    return this.http
      .get<any>(url)
      .pipe(
        catchError(() => of(null))
      );
  }

  /**
   * Busca peças de carros no Mercado Livre
   */
  searchAutoParts(query: string): Observable<MercadoLivreListing[]> {
    return this.searchProducts(`${query} automovel peça`);
  }
}
