import { Component, inject, output } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { RegistrationData } from '../dtos/RegistrationData';

/** Backend password pattern: >= 8 characters, min 1 letter, 1 number, and 1 special character (@$!%*#?&) */
const BACKEND_PASSWORD_PATTERN = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

/**
 * Validator to verify password matching.
 *
 * @param control - AbstractControl representing the form group.
 * @returns ValidationErrors object if mismatch, null otherwise.
 */
const passwordMatchValidator: ValidatorFn = (
  control: AbstractControl,
): ValidationErrors | null => {
  const password = control.get('password');
  const passwordConfirm = control.get('passwordConfirm');
  if (password && passwordConfirm && password.value !== passwordConfirm.value) {
    return { passwordMismatch: true };
  }
  return null;
};

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
    { validators: [passwordMatchValidator] },
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
    this.form.reset();
  }
}

