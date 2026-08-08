import { Component, inject, input } from '@angular/core';
import { MatDivider } from '@angular/material/list';
import { MatButton } from '@angular/material/button';
import { Router } from '@angular/router';

import { Mail } from '../../models/Mail';
import { Attachment } from '../../models/Attachment';
import { EmailStatus } from '../../enums/email-status';
import { AttachmentService } from '../../services/attachment-service';
import { AttachmentList } from '../attachment-list/attachment-list';

/**
 * Component for viewing email details and downloading attachments.
 */
@Component({
  selector: 'app-mail-viewer',
  imports: [
    MatDivider,
    MatButton,
    AttachmentList,
  ],
  templateUrl: './mail-viewer.html',
  styleUrl: './mail-viewer.css',
})
export class MailViewer {
  private readonly _router = inject(Router);
  private readonly attachmentService = inject(AttachmentService);

  public readonly mail = input.required<Mail>();

  /** Navigates to the draft edit view. */
  onEdit() {
    this._router.navigate(['/mails', this.mail().mailId, 'edit']);
  }

  /** Navigates to the email reply view. */
  onReply() {
    this._router.navigate(['/mails', this.mail().mailId, 'reply']);
  }

  /** Navigates to the email reply-all view. */
  onReplyAll() {
    this._router.navigate(['/mails', this.mail().mailId, 'reply-all']);
  }

  /** Navigates to the email forward view. */
  onForward() {
    this._router.navigate(['/mails', this.mail().mailId, 'forward']);
  }

  /**
   * Downloads an attachment file from a given Attachment domain model.
   *
   * @param attachment Attachment domain model.
   */
  onDownloadAttachment(attachment: Attachment): void {
    if (attachment.attachmentId) {
      this.attachmentService.downloadAndSave(attachment.attachmentId, attachment.fileName);
    }
  }

  /**
   * status of the Mail - used to select which Buttons are shown
   * @protected
   */
  protected readonly EmailStatus = EmailStatus;
}
