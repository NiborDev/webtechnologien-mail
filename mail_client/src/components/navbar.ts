import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../services/auth-service';

/**
 * Navigation bar component displayed at the top of the application layout.
 *
 * Renders navigation links and controls authentication state actions (login/logout).
 */
@Component({
  selector: 'app-navbar',
  imports: [MatToolbarModule, MatButtonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  /** AuthService instance exposing authentication state. */
  protected readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  /**
   * Logs out the current user and navigates to the login screen.
   */
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

