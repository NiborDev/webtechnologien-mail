import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatSnackBar } from '@angular/material/snack-bar';

import { MailService } from '../../services/mail-service';
import { MailForm } from '../../components/mail-form/mail-form';
import { MailCreate } from '../../dtos/MailCreate';
import { MailUpdate } from '../../dtos/MailUpdate';

/**
 * Page component for editing existing draft email messages.
 */
@Component({
  selector: 'app-mail-editor',
  imports: [MailForm],
  templateUrl: './mail-editor.html',
  styleUrl: './mail-editor.css',
})
export class MailEditor {
  private readonly router = inject(Router);
  private readonly mailService = inject(MailService);
  private readonly route = inject(ActivatedRoute);
  private readonly snackBar = inject(MatSnackBar);

  private readonly id = this.route.snapshot.paramMap.get('id')!;

  protected readonly mail = toSignal(this.mailService.getById(this.id), { initialValue: null });

  handleUpdate(mail: MailCreate) {
    const mailUpdate: MailUpdate = {
      ...mail,
      mailId: this.id,
    };

    this.mailService.updateData(this.id, mailUpdate).subscribe({
      next: (updatedMail) => {
        this.snackBar.open('E-Mail wurde erfolgreich gespeichert!', 'Close', { duration: 3000 });
        this.router.navigate(['/mails', updatedMail.mailId]);
      },
      error: (err) =>
        this.snackBar.open(err?.error?.message || 'Fehler beim Speichern der E-Mail', 'Close', {
          duration: 3000,
        }),
    });
  }

  handleSend(mail: MailCreate): void {
    const mailUpdate: MailUpdate = {
      ...mail,
      mailId: this.id,
    };

    this.mailService.updateData(this.id, mailUpdate).subscribe({
      next: (createdMail) => {
        this.mailService.send(createdMail.mailId).subscribe({
          next: () => {
            this.snackBar.open('E-Mail wurde erfolgreich gesendet', 'Close', { duration: 3000 });
            this.router.navigate(['/']);
          },
          error: (err) => {
            this.snackBar.open(err?.error?.message || 'Senden fehlgeschlagen', 'Close', {
              duration: 3000,
            });
          },
        });
      },
      error: (err) => {
        this.snackBar.open(err?.error?.message || 'Fehler beim Speichern der Mail:', 'Close', {
          duration: 3000,
        });
      },
    });
  }
}
