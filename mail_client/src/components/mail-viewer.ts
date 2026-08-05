import {Component, inject, input} from '@angular/core';
import {MatDivider} from '@angular/material/list';
import {Mail} from '../models/Mail';
import {MatButton} from '@angular/material/button';
import {Router} from '@angular/router';
import {EMailStatus} from '../enums/email-status';

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

  private readonly router = inject(Router)
  public readonly mail = input.required<Mail>()

  onEdit() {
    this.router.navigate(['/edit', this.mail().mailId])
  }

  protected readonly EMailStatus = EMailStatus;
}
