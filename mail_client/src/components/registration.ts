import {Component, output, signal} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import {RegistrationData} from '../dtos/RegistrationData';

@Component({
  selector: 'app-registration',
  imports: [ReactiveFormsModule, FormsModule, MatInputModule, MatFormFieldModule, MatButtonModule],
  templateUrl: './registration.html',
  styleUrl: './registration.css',
})

//TODO validierung (1.) Passwort validierung muss der im Backend entsprechen, 2.) Confirm Passwort wird noch ignoriert)

export class Registration {
  // Define output event to notify parent
  public register = output<RegistrationData>()
  // Form data signals
  protected firstName = signal('')
  protected lastName = signal('')
  protected email = signal('')
  protected password = signal('')
  protected passwordConfirm = signal('')
  // Handle Register button click
  onRegister() {
    const userData = new RegistrationData(
      this.firstName(),
      this.lastName(),
      this.email(),
      this.password()
    );
    this.register.emit(userData) // Emit event to parent
    this.resetForm() // Optional: Reset form
  }
  private resetForm() {
    this.firstName.set('')
    this.lastName.set('')
    this.email.set('')
    this.password.set('')
  }
}

