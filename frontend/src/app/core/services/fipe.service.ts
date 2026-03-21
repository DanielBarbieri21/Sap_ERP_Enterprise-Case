import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError, shareReplay } from 'rxjs/operators';

export interface FipeBrand {
  id: number;
  name: string;
}

export interface FipeModel {
  id: number;
  name: string;
}

export interface FipeYear {
  id: number;
  name: string;
}

export interface FipeVehicle {
  id: number;
  name: string;
  brand: string;
  model: string;
  modelYear: number;
  fuel: string;
  transmission: string;
  value: string;
}

@Injectable({
  providedIn: 'root'
})
export class FipeService {
  private baseUrl = 'https://parallelum.com.br/fipe/api/v1';
  private brandsCache$: Observable<FipeBrand[]> | null = null;

  constructor(private http: HttpClient) {}

  /**
   * Busca todas as marcas de veículos
   */
  getBrands(): Observable<FipeBrand[]> {
    if (!this.brandsCache$) {
      this.brandsCache$ = this.http
        .get<FipeBrand[]>(`${this.baseUrl}/carros/marcas`)
        .pipe(shareReplay(1), catchError(() => of([])));
    }
    return this.brandsCache$;
  }

  /**
   * Busca modelos de uma marca específica
   */
  getModels(brandId: number): Observable<FipeModel[]> {
    return this.http
      .get<FipeModel[]>(`${this.baseUrl}/carros/marcas/${brandId}/modelos`)
      .pipe(catchError(() => of([])));
  }

  /**
   * Busca anos disponíveis para um modelo específico
   */
  getYears(brandId: number, modelId: number): Observable<FipeYear[]> {
    return this.http
      .get<FipeYear[]>(`${this.baseUrl}/carros/marcas/${brandId}/modelos/${modelId}/anos`)
      .pipe(catchError(() => of([])));
  }

  /**
   * Busca dados de um veículo específico
   */
  getVehicleData(
    brandId: number,
    modelId: number,
    yearId: number
  ): Observable<FipeVehicle | null> {
    return this.http
      .get<any>(
        `${this.baseUrl}/carros/marcas/${brandId}/modelos/${modelId}/anos/${yearId}`
      )
      .pipe(
        map(data => ({
          id: data.id,
          name: data.nome,
          brand: data.marca,
          model: data.modelo,
          modelYear: parseInt(data.anoModelo),
          fuel: data.combustivel,
          transmission: data.transmissao || 'N/A',
          value: data.valor
        })),
        catchError(() => of(null))
      );
  }

  /**
   * Busca peças relacionadas a um veículo no banco de dados local
   */
  searchParts(vehicleModel: string): Observable<any[]> {
    // Este endpoint pode ser criado no backend para buscar peças
    // baseado no modelo do veículo
    return of([]);
  }
}
