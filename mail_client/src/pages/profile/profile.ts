import { Component, inject, OnInit, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../services/auth-service';
import { UserService } from '../../services/user-service';
import { fromUser } from '../../dtos/UserResponse';

// TODO: Redesign and Controll Code!!!

const BACKEND_PASSWORD_PATTERN = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

const passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const newPassword = control.get('newPassword');
  const newPasswordConfirm = control.get('newPasswordConfirm');
  if (newPassword && newPasswordConfirm && newPassword.value !== newPasswordConfirm.value) {
    return { passwordMismatch: true };
  }
  return null;
};

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

  protected readonly profileSuccessMessage = signal<string | null>(null);
  protected readonly profileErrorMessage = signal<string | null>(null);
  protected readonly passwordSuccessMessage = signal<string | null>(null);
  protected readonly passwordErrorMessage = signal<string | null>(null);
  protected readonly deleteErrorMessage = signal<string | null>(null);
  protected readonly showDeleteConfirm = signal<boolean>(false);

  protected readonly profileForm = this.fb.nonNullable.group({
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
  });

  protected readonly passwordForm = this.fb.nonNullable.group(
    {
      oldPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.pattern(BACKEND_PASSWORD_PATTERN)]],
      newPasswordConfirm: ['', [Validators.required]],
    },
    { validators: [passwordMatchValidator] },
  );

  ngOnInit(): void {
    const user = this.authService.currentUser();
    if (user) {
      this.populateForm(user);
    }
  }

  private populateForm(user: { firstName: string; lastName: string; email: string }): void {
    this.profileForm.patchValue({
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
    });
  }

  private getUserId(): string | null {
    const current = this.authService.currentUser();
    return current?.userId || null;
  }

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
      error: (err: unknown) => {
        console.error('Error updating profile:', err);
        this.profileErrorMessage.set('Fehler beim Aktualisieren des Profils.');
      },
    });
  }

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
      error: (err: unknown) => {
        console.error('Error changing password:', err);
        this.passwordErrorMessage.set('Fehler beim Ändern des Passworts. Bitte altes Passwort prüfen.');
      },
    });
  }

  toggleDeleteConfirm(): void {
    this.showDeleteConfirm.update((val) => !val);
  }

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
      error: (err: unknown) => {
        console.error('Error deleting user:', err);
        this.deleteErrorMessage.set('Fehler beim Löschen des Kontos.');
      },
    });
  }
}
