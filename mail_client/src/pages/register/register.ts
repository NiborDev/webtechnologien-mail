import { Component, inject } from '@angular/core';
import { Registration } from '../../components/registration';
import { UserService } from '../../services/user-service';
import { RegistrationData } from '../../dtos/RegistrationData';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-register',
  imports: [Registration, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private readonly router: Router = inject(Router);
  private readonly userService: UserService = inject(UserService);
  private readonly authService: AuthService = inject(AuthService);

  handleRegistration(userData: RegistrationData) {
    this.userService.create(userData.toUserCreate()).subscribe({
      next: (user) => {
        console.log('Registration successful', user);
        this.authService.loginWithCredentials({ email: userData.email, password: userData.password }).subscribe({
          next: () => {
            this.router.navigate(['/']);
          },
          error: (err) => {
            console.error('Auto-login failed after registration:', err);
            this.router.navigate(['/login']);
          },
        });
      },
      error: (err) => {
        console.error('Error creating user:', err);
      },
      complete: () => console.log('User creation completed'),
    });
  }
}
