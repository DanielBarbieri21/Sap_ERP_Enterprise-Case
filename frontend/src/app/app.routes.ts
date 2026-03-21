import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    title: 'Entrar | SAP ERP Enterprise',
    loadComponent: () => import('./auth/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: 'dashboard',
    title: 'Dashboard | SAP ERP Enterprise',
    loadComponent: () => import('./dashboard/dashboard.component').then((m) => m.DashboardComponent),
    canActivate: [authGuard]
  },
  {
    path: 'fi',
    title: 'Financeiro | SAP ERP Enterprise',
    loadComponent: () => import('./modules/fi/fi.component').then((m) => m.FiComponent),
    canActivate: [authGuard]
  },
  {
    path: 'co',
    title: 'Contabil | SAP ERP Enterprise',
    loadComponent: () => import('./modules/co/co.component').then((m) => m.CoComponent),
    canActivate: [authGuard]
  },
  {
    path: 'mm',
    title: 'Compras | SAP ERP Enterprise',
    loadComponent: () => import('./modules/mm/mm.component').then((m) => m.MmComponent),
    canActivate: [authGuard]
  },
  {
    path: 'sd',
    title: 'Vendas | SAP ERP Enterprise',
    loadComponent: () => import('./modules/sd/sd.component').then((m) => m.SdComponent),
    canActivate: [authGuard]
  },
  {
    path: 'wm',
    title: 'Estoque | SAP ERP Enterprise',
    loadComponent: () => import('./modules/wm/wm.component').then((m) => m.WmComponent),
    canActivate: [authGuard]
  },
  {
    path: 'hcm',
    title: 'RH | SAP ERP Enterprise',
    loadComponent: () => import('./modules/hcm/hcm.component').then((m) => m.HcmComponent),
    canActivate: [authGuard]
  },
  {
    path: 'fipe',
    title: 'FIPE | SAP ERP Enterprise',
    loadComponent: () => import('./modules/fipe/fipe-page.component').then((m) => m.FipePageComponent),
    canActivate: [authGuard]
  },
  {
    path: 'mercado-livre',
    title: 'Mercado Livre | SAP ERP Enterprise',
    loadComponent: () => import('./modules/mercado-livre/mercado-livre-page.component').then((m) => m.MercadoLivrePageComponent),
    canActivate: [authGuard]
  },
  {
    path: 'compras',
    title: 'Compras Integradas | SAP ERP Enterprise',
    loadComponent: () => import('./modules/compras/compras-page.component').then((m) => m.ComprasPageComponent),
    canActivate: [authGuard]
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
