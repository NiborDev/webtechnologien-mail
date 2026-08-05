import { inject, Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';

/**
 * Route guard preventing unauthorized access to protected routes.
 *
 * Checks if the user is authenticated via AuthService. If not, redirects to the login route.
 */
@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  private _authService: AuthService = inject(AuthService);
  private _router: Router = inject(Router);

  /**
   * This shows if the route can be used.
   *
   * @returns True if authenticated, false (navigation to /login) otherwise.
   */
  public canActivate(): boolean {
    if (this._authService.isAuthenticated) {
      return true;
    } else {
      this._router.navigate(['/login']);
      return false;
    }
  }
}

