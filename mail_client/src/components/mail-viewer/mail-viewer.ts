import {Component, inject, input} from '@angular/core';
import {MatDivider} from '@angular/material/list';
import {Mail} from '../../models/Mail';
import {MatButton} from '@angular/material/button';
import {Router} from '@angular/router';
import {EmailStatus} from '../../enums/email-status';

@Component({
  selector: 'app-mail-viewer',
  imports: [
    MatDivider,
    MatButton
  ],
  templateUrl: './mail-viewer.html',
  styleUrl: './mail-viewer.css',
})
export class MailViewer {

  private readonly _router = inject(Router)
  public readonly mail = input.required<Mail>()

  onEdit() {
    this._router.navigate(['/mails', this.mail().mailId, 'edit'])
  }

  protected readonly EmailStatus = EmailStatus;
}
