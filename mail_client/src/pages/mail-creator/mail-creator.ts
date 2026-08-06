import { Component, inject } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MailService} from '../../services/mail-service';
import {MailForm} from '../../components/mail-form/mail-form';
import {MailCreate} from '../../dtos/MailCreate';
import {toSignal} from '@angular/core/rxjs-interop';
import {map, of} from 'rxjs';
import {Mail} from '../../models/Mail';

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

  private readonly id = this.route.snapshot.paramMap.get('id');
  private readonly mode = this.route.snapshot.url.at(-1)?.path

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

  private initialise(mail: Mail): Partial<Mail>{

    console.log("semt:"+mail.sentAt+"recieved"+mail.receivedAt+"created"+mail.createdAt+"updated"+mail.updatedAt)

    switch (this.mode) {

      case 'reply':
        return {
          to: mail.replyTo.length ? mail.replyTo : [mail.from],
          subject: (mail.subject.startsWith("Re: ")? mail.subject: "Re:" + mail.subject),
          bodyText: this.createQuote(mail)
        }
      case 'reply-all':

        return {
          to: [...new Set([...(mail.replyTo.length? mail.replyTo: [mail.from] ), ...mail.to])],
          cc: mail.cc,
          subject: (mail.subject.startsWith("Re: ")? mail.subject: "Re:" + mail.subject),
          bodyText: this.createQuote(mail)
        }

      case 'forward':
        return {
          subject: (mail.subject.startsWith("Fwd: ")? mail.subject: "Fwd:" + mail.subject),
          bodyText: this.createQuote(mail)
        }

      default:
        return {}
    }

  }

  private createQuote(mail: Mail): string {
    return `

Am ${mail.sentAt} schrieb ${mail.from}:

${mail.bodyText}`;
  }

  handleCreate(mail: MailCreate) {
    this.mailService.create(mail).subscribe({
      next: (createdMail) => {
        console.log(createdMail.mailId) //TODO only for testing
        this.router.navigate(['/']); //TODO navigiere zur Mail nicht zu home
      },
      error: (err) => {
        console.error("Fehler beim speichern der Mail", err)
      }
    });
  }

  handleSend(mail: MailCreate): void {
    this.mailService.create(mail).subscribe({
      next: (createdMail) => {
        this.mailService.send(createdMail.mailId).subscribe({
          next: () => {
            console.log('Mail erfolgreich gesendet:', createdMail.mailId);
            this.router.navigate(['/']);
          },
          error: (err) => {
            console.error('Fehler beim Senden der Mail:', err);
          }
        });
      },
      error: (err) => {
        console.error('Fehler beim Erstellen der Mail:', err);
      }
    });
  }

}
