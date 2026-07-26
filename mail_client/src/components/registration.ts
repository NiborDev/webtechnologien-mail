import { Component, inject, output } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { RegistrationData } from '../dtos/RegistrationData';

// TODO: Redesign and controll Code!

// Backend regex pattern: >= 8 characters, min 1 letter, 1 number, and 1 special character (@$!%*#?&)
const BACKEND_PASSWORD_PATTERN = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

const passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('password');
  const passwordConfirm = control.get('passwordConfirm');
  if (password && passwordConfirm && password.value !== passwordConfirm.value) {
    return { passwordMismatch: true };
  }
  return null;
};

@Component({
  selector: 'app-registration',
  imports: [ReactiveFormsModule, MatInputModule, MatFormFieldModule, MatButtonModule],
  templateUrl: './registration.html',
  styleUrl: './registration.css',
})
export class Registration {
  private readonly formBuilder = inject(FormBuilder);

  public readonly register = output<RegistrationData>();

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
