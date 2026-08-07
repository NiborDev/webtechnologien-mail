import { Component, inject } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MailService} from '../../services/mail-service';
import {MailForm} from '../../components/mail-form/mail-form';
import {MailCreate} from '../../dtos/MailCreate';
import {toSignal} from '@angular/core/rxjs-interop';
import {map, of} from 'rxjs';
import {Mail} from '../../models/Mail';
import {MatSnackBar} from '@angular/material/snack-bar';
import {AuthService} from '../../services/auth-service';

/**
 * Component for the page to create a mail (new, reply, reply-all, forward)
 */
@Component({
  selector: 'app-mailCreator',
  imports: [MailForm],
  templateUrl: './mail-creator.html',
  styleUrl: './mail-creator.css',
})
export class MailCreator {
  private readonly mailService = inject(MailService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute)
  private readonly snackBar = inject(MatSnackBar)
  private readonly authService = inject(AuthService)

  private readonly id = this.route.snapshot.paramMap.get('id');
  private readonly mode = this.route.snapshot.url.at(-1)?.path

  /**
   * Signal for initialing the MailForm if required (reply, reply-all, forward)
   * @protected
   */
  protected readonly initialValue = toSignal(
    this.id
      ? this.mailService.getById(this.id).pipe(
        map(mail => this.initialise(mail))
      )
      : of(undefined),
    {
      initialValue: undefined
    }
  );

  /**
   * Function creating/setting the initial values for the MailForm if mail-creator was called via reply, reply-all or forward
   * Sets recipients, subject and BodyText, creates prefixes for subject
   * @param mail The Mail for which reply/reply-all/forward was called
   * @returns Partial of the Mail with the initial values for the Form
   * @private
   */
  private initialise(mail: Mail): Partial<Mail>{


    switch (this.mode) {

      case 'reply':
        return {
          to: mail.replyTo.length ? mail.replyTo : [mail.from],
          subject: (mail.subject.startsWith("Re:")? mail.subject: "Re: " + mail.subject),
          bodyText: this.createQuote(mail)
        }
      case 'reply-all':
        const ownMail = this.authService.currentUser()?.email.toLowerCase()
        const to = [...new Set([...(mail.replyTo.length? mail.replyTo: [mail.from] ), ...mail.to])].filter(e => e.toLowerCase() != ownMail)

        return {
          to: to,
          cc: mail.cc.filter(e => e.toLowerCase() != ownMail && !to.includes(e)),
          subject: (mail.subject.startsWith("Re:")? mail.subject: "Re: " + mail.subject),
          bodyText: this.createQuote(mail)
        }

      case 'forward':
        return {
          subject: (mail.subject.startsWith("Fwd:")? mail.subject: "Fwd: " + mail.subject),
          bodyText: this.createQuote(mail)
        }

      default:
        return {}
    }

  }

  /**
   * Helper Function: creates quote for Body-Text (for reply/reply-all/forward)
   * @param mail The Mail for which reply/reply-all/forward was called
   * @returns string with the quoted Text of the original Mail
   * @private
   */
  private createQuote(mail: Mail): string {
    return `

Am ${mail.sentAt} schrieb ${mail.from}:

${mail.bodyText}`;
  }

  /**
   * Handler for creating a new mail (saving as draft)
   * Saves the Mail and creates success/error messages, navigates to detail-view if successful
   * @param mail The Mail-Object to create
   */
  handleCreate(mail: MailCreate) {
    this.mailService.create(mail).subscribe({
      next: (createdMail) => {
        this.snackBar.open("E-Mail wurde erfolgreich gespeichert", "Close", {duration: 3000})
        this.router.navigate(['/mails', createdMail.mailId]);
      },
      error: (err) => {
        this.snackBar.open(err?.error?.message || "Speichern der E-Mail fehlgeschlagen", "Close", {duration: 3000})
      }
    });
  }

  /**
   * Handler for sending a Mail
   * saves Mail as draft and triggers send operation when successful, navigates to Home/Mail-List if successfully send
   * creates success/error messages
   * @param mail The Mail-Object to send
   */
  handleSend(mail: MailCreate): void {
    this.mailService.create(mail).subscribe({
      next: (createdMail) => {
        this.mailService.send(createdMail.mailId).subscribe({
          next: () => {
            this.snackBar.open("E-Mail wurde erfolgreich gesendet", "Close", {duration: 3000})
            this.router.navigate(['/']);
          },
          error: (err) => {
            this.snackBar.open(err?.error?.message || "Senden fehlgeschlagen!", "Close", {duration: 3000})
          }
        });
      },
      error: (err) => {
        this.snackBar.open(err?.error?.message||'Fehler beim Erstellen der Mail:', "Close", {duration: 3000});
      }
    });
  }

}
