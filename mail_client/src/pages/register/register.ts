import { Component, inject } from '@angular/core';
import { Registration } from '../../components/registration';
import { UserService } from '../../services/user-service';
import { RegistrationData } from '../../types/RegistrationData';

@Component({
  selector: 'app-home',
  imports: [
    Registration
  ],
  templateUrl: './regsiter.html',
  styleUrl: './register.css',
})
export class Register {

  private userService: UserService = inject(UserService);

  handleRegistration(userData: RegistrationData) {
    this.userService.create(userData.toUserCreate()).subscribe({
      next: (user) => {
        // TODO: Navigate to Home and store token
      },
      error: (err) => {
        console.error('Error creating user:', err);
      },
      complete: () => console.log('User creation completed') 
    })
  }
}
