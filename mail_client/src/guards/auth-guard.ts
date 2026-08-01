import {inject, Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AuthService} from '../services/auth-service';

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {
  private _authService: AuthService = inject(AuthService)
  private _router: Router = inject(Router)

  public canActivate(): boolean {
    if (this._authService.isAuthenticated) {
      return true;
    } else {
      this._router.navigate(['/login']);
      return false;
    }
  }
}
