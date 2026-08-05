import { Component, inject } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MailService} from '../../services/mail-service';
import {MailForm} from '../../components/mail-form';
import {toSignal} from '@angular/core/rxjs-interop';
import {MailCreate} from '../../dtos/MailCreate';
import {MailUpdate} from '../../dtos/MailUpdate';

@Component({
  selector: 'app-mailEditor',
  imports: [MailForm],
  templateUrl: './mailEditor.html',
  styleUrl: './mailEditor.css',
})
export class MailEditor {
  private readonly router: Router = inject(Router);
  private readonly mailService: MailService = inject(MailService)
  private readonly route = inject(ActivatedRoute);


  private readonly id = this.route.snapshot.paramMap.get('id')!;

  protected readonly mail = toSignal(
    this.mailService.getById(this.id),
    { initialValue: null }
  );

  handleUpdate(mail: MailCreate) {

    const mailUpdate: MailUpdate = {
      ...mail,
      mailId: this.id
    }

    this.mailService.updateData(this.id, mailUpdate).subscribe({
      next: () => console.log("Mail erfolgreich gespeichert"),
      error: (err) => console.error("Fehler beim speichern der Mail", err)
    });
  }

  handleSend(mail: MailCreate): void {

    const mailUpdate: MailUpdate = {
      ...mail,
      mailId: this.id
    }

    this.mailService.updateData(this.id, mailUpdate).subscribe({
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
        console.error('Fehler beim speichern der Mail:', err);
      }
    });
  }

}
