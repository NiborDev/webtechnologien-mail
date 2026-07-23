import {Component, output, signal} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import {LoginData} from '../dtos/LoginData';

@Component({
  selector: 'app-login-form',
  imports: [ReactiveFormsModule, FormsModule, MatInputModule, MatFormFieldModule, MatButtonModule],
  templateUrl: './login-form.html',
  styleUrl: './login-form.css',
})
export class LoginForm {
  public login = output<LoginData>()

  protected email = signal('')
  protected password = signal('')

  onLogin() {
    const loginData = new LoginData(
      this.email(),
      this.password()
    );
    this.login.emit(loginData)
    this.resetForm()
  }

  private resetForm() {
    this.email.set('')
    this.password.set('')
  }
}
