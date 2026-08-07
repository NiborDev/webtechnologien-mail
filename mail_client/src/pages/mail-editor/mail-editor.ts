import { Component, inject } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MailService} from '../../services/mail-service';
import {MailForm} from '../../components/mail-form/mail-form';
import {toSignal} from '@angular/core/rxjs-interop';
import {MailCreate} from '../../dtos/MailCreate';
import {MailUpdate} from '../../dtos/MailUpdate';
import {MatSnackBar} from '@angular/material/snack-bar';

/**
 * Component for the page to edit an existing Draft-Mail
 */
@Component({
  selector: 'app-mailEditor',
  imports: [MailForm],
  templateUrl: './mail-editor.html',
  styleUrl: './mail-editor.css',
})
export class MailEditor {
  private readonly router: Router = inject(Router);
  private readonly mailService: MailService = inject(MailService)
  private readonly route = inject(ActivatedRoute);
  private readonly snackBar = inject(MatSnackBar)


  private readonly id = this.route.snapshot.paramMap.get('id')!;

  /**
   * Gets the Mail-Object
   * @protected
   */
  protected readonly mail = toSignal(
    this.mailService.getById(this.id),
    { initialValue: null }
  );

  /**
   * Handler for saving the updated Mail (Saving as Draft)
   * Saves the Mail and navigates to Detail-View if successful, creates success/error messages
   * @param mail The Mail to save
   */
  handleUpdate(mail: MailCreate) {

    const mailUpdate: MailUpdate = {
      ...mail,
      mailId: this.id
    }

    this.mailService.updateData(this.id, mailUpdate).subscribe({
      next: (updatedMail) => {
        this.snackBar.open("E-Mail wurde erfolgreich gespeichert!", "Close", {duration: 3000})
        this.router.navigate(['/mails', updatedMail.mailId])
      },
      error: (err) => this.snackBar.open(err?.error.message ||"Fehler beim speichern der E-Mail", "Close", {duration: 3000})
    });
  }

  /**
   * Handler for sending the updated Mail
   * Saves the Mail and trigger the send operation if successful, navigates to Home/Mail-List if successfully send
   * creates success/error messages
   * @param mail The Mail to send
   */
  handleSend(mail: MailCreate): void {

    const mailUpdate: MailUpdate = {
      ...mail,
      mailId: this.id
    }

    this.mailService.updateData(this.id, mailUpdate).subscribe({
      next: (createdMail) => {
        this.mailService.send(createdMail.mailId).subscribe({
          next: () => {
            this.snackBar.open("E-Mail wurde erfolgreich gesendet", "Close", {duration: 3000})
            this.router.navigate(['/']);
          },
          error: (err) => {
            this.snackBar.open(err?.error?.message || "Senden fehlgeschlagen", "Close", {duration: 3000})
          }
        });
      },
      error: (err) => {
        this.snackBar.open(err?.error?.message ||'Fehler beim speichern der Mail:', "Close", {duration: 3000});
      }
    });
  }

}
