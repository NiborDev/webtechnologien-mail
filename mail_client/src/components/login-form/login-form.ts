import { Component, inject, output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { LoginData } from '../../dtos/LoginData';

/**
 * Form component for the login.
 *
 * Renders email and password fields with validation and emits user input
 * via the `login` output event.
 */
@Component({
  selector: 'app-login-form',
  imports: [ReactiveFormsModule, MatInputModule, MatFormFieldModule, MatButtonModule],
  templateUrl: './login-form.html',
  styleUrl: './login-form.css',
})
export class LoginForm {
  private readonly formBuilder = inject(FormBuilder);

  /** Event emitted when the login form is submitted with valid data. */
  public readonly login = output<LoginData>();

  /** Form validators group for login inputs. */
  protected readonly form = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  /**
   * Validates form and emits `LoginData` event on submission.
   */
  onLogin(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { email, password } = this.form.getRawValue();
    this.login.emit(new LoginData(email, password));
    this.form.reset();
  }
}

