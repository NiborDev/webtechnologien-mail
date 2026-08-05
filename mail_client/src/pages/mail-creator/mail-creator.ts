import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import {MailService} from '../../services/mail-service';
import {MailForm} from '../../components/mail-form/mail-form';
import {MailCreate} from '../../dtos/MailCreate';

@Component({
  selector: 'app-mailCreator',
  imports: [MailForm],
  templateUrl: './mail-creator.html',
  styleUrl: './mail-creator.css',
})
export class MailCreator {
  private readonly mailService = inject(MailService);
  private readonly router = inject(Router);

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
