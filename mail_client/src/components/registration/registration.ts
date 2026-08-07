import { Component, inject, output } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { RegistrationData } from '../../dtos/RegistrationData';
import {
  BACKEND_PASSWORD_PATTERN,
  passwordMatchValidator,
} from '../../validators/password-validators';

/**
 * Form component for the user registration.
 *
 * Captures user details (first name, last name, email, password) with client-side
 * validation and emits `RegistrationData` on submission.
 */
@Component({
  selector: 'app-registration',
  imports: [ReactiveFormsModule, MatInputModule, MatFormFieldModule, MatButtonModule],
  templateUrl: './registration.html',
  styleUrl: './registration.css',
})
export class Registration {
  private readonly formBuilder = inject(FormBuilder);

  /** Event emitted when the registration form is submitted with valid data. */
  public readonly register = output<RegistrationData>();

  /** Form group for user registration inputs. */
  protected readonly form = this.formBuilder.nonNullable.group(
    {
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.pattern(BACKEND_PASSWORD_PATTERN)]],
      passwordConfirm: ['', [Validators.required]],
    },
    { validators: [passwordMatchValidator()] },
  );

  /**
   * Validates form inputs and emits `RegistrationData` on submission.
   */
  onRegister(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { firstName, lastName, email, password } = this.form.getRawValue();
    const userData = new RegistrationData(firstName, lastName, email, password);
    this.register.emit(userData);
  }
}

