import { Component, input, output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatTooltipModule } from '@angular/material/tooltip';

/**
 * Component for mail toolbar.
 *
 * This component handles:
 * - Creation of new mails
 * - Editing of selected drafts
 * - Deletion of selected mails
 */
@Component({
  selector: 'app-mail-toolbar',
  imports: [MatButtonModule, MatIconModule, MatBadgeModule, MatTooltipModule],
  templateUrl: './mail-toolbar.html',
  styleUrl: './mail-toolbar.css',
})
export class MailActionsToolbarComponent {
  readonly selectedCount = input<number>(0);
  readonly isDraftTab = input<boolean>(false);

  readonly createMail = output<void>();
  readonly editSelected = output<void>();
  readonly deleteSelected = output<void>();

  /**
   * Handler method for when the create mail button is clicked.
   * Emits the createMail output event.
   */
  onCreateMail(): void {
    this.createMail.emit();
  }

  /**
   * Handler method for when the edit selected button is clicked.
   * Emits the editSelected output event.
   */
  onEditSelected(): void {
    this.editSelected.emit();
  }

  /**
   * Handler method for when the delete selected button is clicked.
   * Emits the deleteSelected output event only if there are any selected mails.
   */
  onDeleteSelected(): void {
    if (this.selectedCount() > 0) {
      this.deleteSelected.emit();
    }
  }
}
