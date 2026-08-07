import {Component, inject, input} from '@angular/core';
import {MatDivider} from '@angular/material/list';
import {Mail} from '../../models/Mail';
import {MatButton} from '@angular/material/button';
import {Router} from '@angular/router';
import {EmailStatus} from '../../enums/email-status';

/**
 * Component for displaying a Mail
 * Shows Author, Recipients, Subject and BodyText allows navigation to edit, forward, or reply if possible
 */
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

  /**
   * Handler for edit-Button
   */
  onEdit() {
    this._router.navigate(['/mails', this.mail().mailId, 'edit'])
  }

  /**
   * Handler for reply-Button
   */
  onReply() {
    this._router.navigate(['/mails', this.mail().mailId, 'reply'])
  }

  /**
   * Handler for reply-all-Button
   */
  onReplyAll() {
    this._router.navigate(['/mails', this.mail().mailId, 'reply-all'])
  }

  /**
   * Handler for forward Button
   */
  onForward() {
    this._router.navigate(['/mails', this.mail().mailId, 'forward'])
  }

  /**
   * status of the Mail - used to select which Buttons are shown
   * @protected
   */
  protected readonly EmailStatus = EmailStatus;
}
