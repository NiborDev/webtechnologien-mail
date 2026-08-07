import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Regular expression matching backend password requirements:
 * At least 8 characters, containing a letter, a number, and a special character (@$!%*#?&).
 */
export const BACKEND_PASSWORD_PATTERN = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

/**
 * Factory function creating a password match validator for a FormGroup.
 * Sets `{ passwordMismatch: true }` on both the FormGroup and the confirm control
 * so Angular Material form fields enter error state correctly.
 *
 * @param passwordControlName - The name of the password control (default: 'password').
 * @param confirmControlName - The name of the confirm password control (default: 'passwordConfirm').
 * @returns ValidatorFn that validates password matching.
 */
export function passwordMatchValidator(
  passwordControlName = 'password',
  confirmControlName = 'passwordConfirm',
): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const password = control.get(passwordControlName);
    const passwordConfirm = control.get(confirmControlName);

    if (password && passwordConfirm) {
      if (passwordConfirm.value && password.value !== passwordConfirm.value) {
        passwordConfirm.setErrors({ ...passwordConfirm.errors, passwordMismatch: true });
        return { passwordMismatch: true };
      } else if (passwordConfirm.hasError('passwordMismatch')) {
        const errors = { ...passwordConfirm.errors };
        delete errors['passwordMismatch'];
        const remainingKeys = Object.keys(errors);
        passwordConfirm.setErrors(remainingKeys.length > 0 ? errors : null);
      }
    }
    return null;
  };
}
