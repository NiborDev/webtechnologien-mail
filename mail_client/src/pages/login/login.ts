import { Component, inject } from '@angular/core';
import { LoginForm } from '../../components/login-form';
import { AuthService } from '../../services/auth-service';
import { LoginData } from '../../dtos/LoginData';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [
    LoginForm
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  private readonly authService: AuthService = inject(AuthService);
  private readonly router: Router = inject(Router);

  handleLogin(loginData: LoginData) {
    this.authService.loginWithCredentials(loginData).subscribe({
      next: (response) => {
        console.log('Login successful', response);
        this.router.navigate(['/']); // navigate to home
      },
      error: (err) => {
        const errorMessage = err.error?.error || 'An unexpected error occurred.';
        console.error('Error logging in:', errorMessage);
        // TODO: Show error message to user (Alert-Component)
      },
      complete: () => console.log('Login request completed')
    });
  }
}
