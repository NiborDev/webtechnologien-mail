import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatSnackBar } from '@angular/material/snack-bar';
import { map, of } from 'rxjs';

import { MailService } from '../../services/mail-service';
import { AuthService } from '../../services/auth-service';
import { MailForm } from '../../components/mail-form/mail-form';
import { MailCreate } from '../../dtos/MailCreate';
import { Mail } from '../../models/Mail';

/**
 * Page component for creating, replying to, and forwarding email messages.
 */
@Component({
  selector: 'app-mail-creator',
  imports: [MailForm],
  templateUrl: './mail-creator.html',
  styleUrl: './mail-creator.css',
})
export class MailCreator {
  private readonly mailService = inject(MailService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly snackBar = inject(MatSnackBar);
  private readonly authService = inject(AuthService);

  private readonly id = this.route.snapshot.paramMap.get('id');
  private readonly mode = this.route.snapshot.url.at(-1)?.path;

  protected readonly initialValue = toSignal(
    this.id
      ? this.mailService.getById(this.id).pipe(map((mail) => this.initialise(mail)))
      : of(undefined),
    {
      initialValue: undefined,
    },
  );

  private initialise(mail: Mail): Partial<Mail> {
    switch (this.mode) {
      case 'reply':
        return {
          to: mail.replyTo.length ? mail.replyTo : [mail.from],
          subject: mail.subject.startsWith('Re:') ? mail.subject : `Re: ${mail.subject}`,
          bodyText: this.createQuote(mail),
        };
      case 'reply-all': {
        const ownMail = this.authService.currentUser()?.email.toLowerCase();
        const to = [
          ...new Set([...(mail.replyTo.length ? mail.replyTo : [mail.from]), ...mail.to]),
        ].filter((e) => e.toLowerCase() !== ownMail);

        return {
          to,
          cc: mail.cc.filter((e) => e.toLowerCase() !== ownMail && !to.includes(e)),
          subject: mail.subject.startsWith('Re:') ? mail.subject : `Re: ${mail.subject}`,
          bodyText: this.createQuote(mail),
        };
      }

      case 'forward':
        return {
          subject: mail.subject.startsWith('Fwd:') ? mail.subject : `Fwd: ${mail.subject}`,
          bodyText: this.createQuote(mail),
          attachments: mail.attachments,
        };

      default:
        return {};
    }
  }

  private createQuote(mail: Mail): string {
    return `\n\nAm ${mail.sentAt} schrieb ${mail.from}:\n\n${mail.bodyText}`;
  }

  handleCreate(mail: MailCreate) {
    this.mailService.create(mail).subscribe({
      next: (createdMail) => {
        this.snackBar.open('E-Mail wurde erfolgreich gespeichert', 'Close', { duration: 3000 });
        this.router.navigate(['/mails', createdMail.mailId]);
      },
      error: (err) => {
        this.snackBar.open(err?.error?.message || 'Speichern der E-Mail fehlgeschlagen', 'Close', {
          duration: 3000,
        });
      },
    });
  }

  handleSend(mail: MailCreate): void {
    this.mailService.create(mail).subscribe({
      next: (createdMail) => {
        this.mailService.send(createdMail.mailId).subscribe({
          next: () => {
            this.snackBar.open('E-Mail wurde erfolgreich gesendet', 'Close', { duration: 3000 });
            this.router.navigate(['/']);
          },
          error: (err) => {
            this.snackBar.open(err?.error?.message || 'Senden fehlgeschlagen!', 'Close', {
              duration: 3000,
            });
          },
        });
      },
      error: (err) => {
        this.snackBar.open(err?.error?.message || 'Fehler beim Erstellen der Mail:', 'Close', {
          duration: 3000,
        });
      },
    });
  }
}
