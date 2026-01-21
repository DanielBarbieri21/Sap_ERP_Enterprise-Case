import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { authGuard } from './core/guards/auth.guard';
import { FiComponent } from './modules/fi/fi.component';
import { CoComponent } from './modules/co/co.component';
import { MmComponent } from './modules/mm/mm.component';
import { SdComponent } from './modules/sd/sd.component';
import { WmComponent } from './modules/wm/wm.component';
import { HcmComponent } from './modules/hcm/hcm.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { 
    path: 'dashboard', 
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  { path: 'fi', component: FiComponent, canActivate: [authGuard] },
  { path: 'co', component: CoComponent, canActivate: [authGuard] },
  { path: 'mm', component: MmComponent, canActivate: [authGuard] },
  { path: 'sd', component: SdComponent, canActivate: [authGuard] },
  { path: 'wm', component: WmComponent, canActivate: [authGuard] },
  { path: 'hcm', component: HcmComponent, canActivate: [authGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
