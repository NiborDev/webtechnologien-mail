import { Component, inject } from '@angular/core';
import { Registration } from '../../components/registration';
import { UserService } from '../../services/user-service';
import { RegistrationData } from '../../dtos/RegistrationData';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-register',
  imports: [Registration],
  templateUrl: './regsiter.html',
  styleUrl: './register.css',
})
export class Register {
  private readonly router: Router = inject(Router);
  private readonly userService: UserService = inject(UserService);
  private readonly authService: AuthService = inject(AuthService);

  handleRegistration(userData: RegistrationData) {
    this.userService.create(userData.toUserCreate()).subscribe({
      next: (user) => {
        // Registration successful. Automatically login the user to get and store the token.
        console.log('Registration successful', user);
        this.authService.loginWithCredentials({ email: userData.email, password: userData.password }).subscribe({
          next: () => {
             this.router.navigate(['/']); // navigate to home
          },
          error: (err) => {
             console.error('Auto-login failed after registration:', err);
             this.router.navigate(['/login']); // fallback to login page
          }
        });
      },
      error: (err) => {
        const errorMessage = err.error?.error || 'An unexpected error occurred.';
        console.error('Error creating user:', errorMessage);
        // alert(errorMessage);
      },
      complete: () => console.log('User creation completed'),
    });
  }
}
