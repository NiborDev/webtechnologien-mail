import { Component, inject, OnInit, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../services/auth-service';
import { UserService } from '../../services/user-service';
import { fromUser } from '../../dtos/UserResponse';

/**
 * Regular expression matching backend password requirements:
 * At least 8 characters, containing a letter, a number, and a special character (@$!%*#?&).
 */
const BACKEND_PASSWORD_PATTERN = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

/**
 * Validator to verify the new password and confirm password match.
 *
 * @param control - The form group control.
 * @returns ValidationErrors object if mismatch, null otherwise.
 */
const passwordMatchValidator: ValidatorFn = (
  control: AbstractControl,
): ValidationErrors | null => {
  const newPassword = control.get('newPassword');
  const newPasswordConfirm = control.get('newPasswordConfirm');
  if (newPassword && newPasswordConfirm && newPassword.value !== newPasswordConfirm.value) {
    return { passwordMismatch: true };
  }
  return null;
};

/**
 * User Profile page component.
 *
 * Provides form interfaces for updating personal user information, changing account password,
 * and deleting the user account.
 */
@Component({
  selector: 'app-profile',
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  protected readonly authService = inject(AuthService);
  private readonly userService = inject(UserService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);

  /** Success message signal for profile update. */
  protected readonly profileSuccessMessage = signal<string | null>(null);
  /** Error message signal for profile update. */
  protected readonly profileErrorMessage = signal<string | null>(null);
  /** Success message signal for password change. */
  protected readonly passwordSuccessMessage = signal<string | null>(null);
  /** Error message signal for password change. */
  protected readonly passwordErrorMessage = signal<string | null>(null);
  /** Error message signal for account deletion. */
  protected readonly deleteErrorMessage = signal<string | null>(null);
  /** Signal governing display of account deletion confirmation prompt. */
  protected readonly showDeleteConfirm = signal<boolean>(false);

  /** Form group for user profile information. */
  protected readonly profileForm = this.fb.nonNullable.group({
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
  });

  /** Form group for changing user password. */
  protected readonly passwordForm = this.fb.nonNullable.group(
    {
      oldPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.pattern(BACKEND_PASSWORD_PATTERN)]],
      newPasswordConfirm: ['', [Validators.required]],
    },
    { validators: [passwordMatchValidator] },
  );

  /** Lifecycle hook to initialize form values from current user context. */
  ngOnInit(): void {
    const user = this.authService.currentUser();
    if (user) {
      this.populateForm(user);
    }
  }

  /**
   * Fills the form with the user's data.
   *
   * @param user The user data.
   */
  private populateForm(user: { firstName: string; lastName: string; email: string }): void {
    this.profileForm.patchValue({
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
    });
  }

  /**
   * Gets the user ID.
   *
   * @returns The user ID.
   */
  private getUserId(): string | null {
    const current = this.authService.currentUser();
    return current?.userId || null;
  }

  /**
   * Submits updated profile information (first name, last name, email) to the backend.
   */
  onUpdateProfile(): void {
    this.profileSuccessMessage.set(null);
    this.profileErrorMessage.set(null);

    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const userId = this.getUserId();
    if (!userId) {
      this.profileErrorMessage.set('Benutzer-ID nicht gefunden.');
      return;
    }

    const { firstName, lastName, email } = this.profileForm.getRawValue();
    this.userService.updateUser(userId, { firstName, lastName, email }).subscribe({
      next: (updatedUser) => {
        this.authService.setCurrentUser(fromUser(updatedUser));
        this.profileSuccessMessage.set('Profil erfolgreich aktualisiert.');
      },
      error: () => {
        this.profileErrorMessage.set('Fehler beim Aktualisieren des Profils.');
      },
    });
  }

  /**
   * Submits old password and new password to update account credentials.
   */
  onChangePassword(): void {
    this.passwordSuccessMessage.set(null);
    this.passwordErrorMessage.set(null);

    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }

    const userId = this.getUserId();
    if (!userId) {
      this.passwordErrorMessage.set('Benutzer-ID nicht gefunden.');
      return;
    }

    const { oldPassword, newPassword } = this.passwordForm.getRawValue();
    this.userService.updatePassword(userId, { oldPassword, newPassword }).subscribe({
      next: () => {
        this.passwordSuccessMessage.set('Passwort erfolgreich geändert.');
        this.passwordForm.reset();
      },
      error: () => {
        this.passwordErrorMessage.set('Fehler beim Ändern des Passworts. Bitte altes Passwort prüfen.');
      },
    });
  }

  /**
   * Toggles the delete account confirmation prompt UI.
   */
  toggleDeleteConfirm(): void {
    this.showDeleteConfirm.update((val) => !val);
  }

  /**
   * Deletes the user account permanently.
   */
  onDeleteAccount(): void {
    this.deleteErrorMessage.set(null);
    const userId = this.getUserId();
    if (!userId) {
      this.deleteErrorMessage.set('Benutzer-ID nicht gefunden.');
      return;
    }

    this.userService.deleteUser(userId).subscribe({
      next: () => {
        this.authService.logout();
        this.router.navigate(['/login']);
      },
      error: () => {
        this.deleteErrorMessage.set('Fehler beim Löschen des Kontos.');
      },
    });
  }
}

