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
      import('../pages/mail-list/mail-list').then((m) => m.MailListPageComponent),
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
    path: 'mails/:id/edit',
    loadComponent: () => import('../pages/mail-editor/mail-editor').then((m) => m.MailEditor),
    canActivate: [AuthGuard]
  },
  {
    path: 'mails/new',
    loadComponent: () => import('../pages/mail-creator/mail-creator').then((m) => m.MailCreator),
    canActivate: [AuthGuard]
  },
  {
    path: 'mails/:id',
    loadComponent: () => import('../pages/mail-detail/mail-detail').then((m) => m.MailDetail),
    canActivate: [AuthGuard]
  },
  {
    path: 'mails/:id/reply',
    loadComponent: () => import('../pages/mail-creator/mail-creator').then((m) => m.MailCreator),
    canActivate: [AuthGuard]
  },
  {
    path: 'mails/:id/reply-all',
    loadComponent: () => import('../pages/mail-creator/mail-creator').then((m) => m.MailCreator),
    canActivate: [AuthGuard]
  },
  {
    path: 'mails/:id/forward',
    loadComponent: () => import('../pages/mail-creator/mail-creator').then((m) => m.MailCreator),
    canActivate: [AuthGuard]
  }

];
