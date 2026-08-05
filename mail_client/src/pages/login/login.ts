import { Component, inject, signal } from '@angular/core';
import { LoginForm } from '../../components/login-form/login-form';
import { AuthService } from '../../services/auth-service';
import { LoginData } from '../../dtos/LoginData';
import { Router, RouterLink } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

/**
 * Login page component.
 *
 * It shows the login form and handles authentication by passing the request to the AuthService.
 * If successful, it navigates to the home route.
 */
@Component({
  selector: 'app-login',
  imports: [LoginForm, RouterLink, MatSnackBarModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private readonly authService: AuthService = inject(AuthService);
  private readonly router: Router = inject(Router);
  private readonly snackBar: MatSnackBar = inject(MatSnackBar);

  /** Error message to display when login fails. */
  protected readonly errorMessage = signal<string | null>(null);

  /**
   * Handles the login form submission.
   *
   * @param loginData - The user's login credentials.
   */
  handleLogin(loginData: LoginData): void {
    this.errorMessage.set(null);
    this.authService.loginWithCredentials(loginData).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (err: unknown) => {
        const message =
          (err as { error?: { message?: string } })?.error?.message ||
          'Login fehlgeschlagen. Bitte überprüfen Sie Ihre Zugangsdaten.';
        this.errorMessage.set(message);
        this.snackBar.open(message, 'Schließen', { duration: 5000 });
      },
    });
  }
}

