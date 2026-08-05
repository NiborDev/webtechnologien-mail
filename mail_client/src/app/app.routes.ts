import { Routes } from '@angular/router';
import { AuthGuard } from '../guards/auth-guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('../pages/home/home').then((m) => m.Home),
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
  {
    path: 'edit/:id',
    loadComponent: () => import('../pages/mailEditor/mailEditor').then((m) => m.MailEditor)
  },
  {
    path: 'new',
    loadComponent: () => import('../pages/mailCreator/mailCreator').then((m) => m.MailCreator)
  },
  {
    path: 'mails/:id',
    loadComponent: () => import('../pages/mailDetail/mailDetail').then((m) => m.MailDetail)
  }

];
