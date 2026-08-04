import { Component, inject, signal } from '@angular/core';
import { Registration } from '../../components/registration';
import { UserService } from '../../services/user-service';
import { RegistrationData } from '../../dtos/RegistrationData';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

/**
 * Register page component.
 *
 * The form for registering as a user is displayed and the user is created using UserService.
 * If the user registration is successful, the user is logged in and navigated to the home route automatically.
 */
@Component({
  selector: 'app-register',
  imports: [Registration, RouterLink, MatSnackBarModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private readonly router: Router = inject(Router);
  private readonly userService: UserService = inject(UserService);
  private readonly authService: AuthService = inject(AuthService);
  private readonly snackBar: MatSnackBar = inject(MatSnackBar);

  /** Error message to display when registration fails. */
  protected readonly errorMessage = signal<string | null>(null);

  /**
   * Handles the registration form submission.
   * Creates a new user account and automatically login on success.
   *
   * @param userData - The user's registration data.
   */
  handleRegistration(userData: RegistrationData): void {
    this.errorMessage.set(null);
    this.userService.create(userData.toUserCreate()).subscribe({
      next: () => {
        this.authService
          .loginWithCredentials({ email: userData.email, password: userData.password })
          .subscribe({
            next: () => {
              this.router.navigate(['/']);
            },
            error: () => {
              this.snackBar.open(
                'Konto erstellt. Bitte melden Sie sich an.',
                'Schließen',
                { duration: 5000 },
              );
              this.router.navigate(['/login']);
            },
          });
      },
      error: (err: unknown) => {
        const message =
          (err as { error?: { message?: string } })?.error?.message ||
          'Registrierung fehlgeschlagen. Bitte versuchen Sie es erneut.';
        this.errorMessage.set(message);
        this.snackBar.open(message, 'Schließen', { duration: 5000 });
      },
    });
  }
}

