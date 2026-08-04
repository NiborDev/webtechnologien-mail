import { Component, input, output } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MailModel } from '../../models/mail.model';

/**
 * Mail Item Component
 *
 * This component is used to display a single email in the mail list.
 */
@Component({
  selector: 'app-mail-item',
  imports: [
    MatCheckboxModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
  ],
  templateUrl: './mail-item.html',
  styleUrl: './mail-item.css',
})
export class MailItemComponent {
  readonly mail = input.required<MailModel>();

  readonly isSelected = input<boolean>(false);
  readonly isReceivedTab = input<boolean>(true);
  readonly isDraftTab = input<boolean>(false);
  readonly displayName = input<string>('');

  readonly toggleSelect = output<string>();
  readonly editMail = output<string>();
  readonly deleteMail = output<string>();
  readonly openMail = output<string>();

  /**
   * Method called when the mail is toggled.
   */
  onToggleMail(): void {
    this.toggleSelect.emit(this.mail().mailId);
  }

  /**
   * Method called when the mail is edited.
   *
   * @param event - The mouse event.
   */
  onEditSingle(event: MouseEvent): void {
    event.stopPropagation();
    this.editMail.emit(this.mail().mailId);
  }

  /**
   * Method called when the mail is deleted.
   * @param event - The mouse event.
   */
  onDeleteSingle(event: MouseEvent): void {
    event.stopPropagation();
    this.deleteMail.emit(this.mail().mailId);
  }

  /**
   * Method called when the mail is clicked.
   */
  onRowClick(): void {
    this.openMail.emit(this.mail().mailId);
  }
}
