import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../services/auth-service';

// TODO: Redesign

@Component({
  selector: 'app-navbar',
  imports: [MatToolbarModule, MatButtonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  protected readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
