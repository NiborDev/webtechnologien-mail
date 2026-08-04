import { Routes } from '@angular/router';
import { AuthGuard } from '../guards/auth-guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'mails',
    pathMatch: 'full',
  },
  {
    path: 'mails',
    loadComponent: () =>
      import('../pages/mail-list-page/mail-list-page.component').then((m) => m.MailListPageComponent),
    canActivate: [AuthGuard],
  },
  {
    path: 'profile',
    loadComponent: () => import('../pages/profile/profile').then((m) => m.Profile),
    canActivate: [AuthGuard],
  },
  {
    path: 'login',
    loadComponent: () => import('../pages/login/login').then((m) => m.Login),
  },
  {
    path: 'register',
    loadComponent: () => import('../pages/register/register').then((m) => m.Register),
  },
];
