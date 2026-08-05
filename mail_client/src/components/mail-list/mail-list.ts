import { Component, input, output } from '@angular/core';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { Mail } from '../../models/Mail';
import { EmailStatus } from '../../enums/email-status';
import { formatSenderName, formatRecipientNames } from '../../utils/user-formatter';
import { MailHeaderControlComponent } from '../mail-list-header-control/mail-list-header-control';
import { MailItemComponent } from '../mail-item/mail-item';

/**
 * Mail List Component
 *
 * Displays list that is sortable, selectable and paginated.
 */
@Component({
  selector: 'app-mail-list',
  imports: [
    MatPaginatorModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MailHeaderControlComponent,
    MailItemComponent,
  ],
  templateUrl: './mail-list.html',
  styleUrl: './mail-list.css',
})
export class MailListComponent {
  // Inputs
  readonly mails = input.required<Mail[]>();
  readonly activeStatusTab = input.required<EmailStatus>();
  readonly selectedMailIds = input.required<Set<string>>();
  readonly userMap = input<Map<string, string>>(new Map());
  readonly isLoading = input<boolean>(false);

  // Pagination
  readonly totalElements = input<number>(0);
  readonly pageIndex = input<number>(0);
  readonly pageSize = input<number>(10);

  // Sort
  readonly sortField = input<string>('date');
  readonly sortDirection = input<'asc' | 'desc'>('desc');

  // Outputs
  readonly toggleSelect = output<string>();
  readonly toggleAll = output<boolean>();

  // Pagination
  readonly pageChange = output<{ pageIndex: number; pageSize: number }>();

  // Sort
  readonly sortChange = output<{ field: string; direction: 'asc' | 'desc' }>();

  // Mail Actions
  readonly editMail = output<string>();
  readonly deleteMail = output<string>();
  readonly openMail = output<string>();

  readonly pageSizeOptions = [5, 10, 25, 50];

  /**
   * Checks if the active status tab is the received tab.
   * @returns True if the active status tab is the received tab.
   */
  get isReceivedTab(): boolean {
    return this.activeStatusTab() === EmailStatus.RECEIVED;
  }

  /**
   * Checks if the active status tab is the draft tab.
   * @returns True if the active status tab is the draft tab.
   */
  get isDraftTab(): boolean {
    return this.activeStatusTab() === EmailStatus.DRAFT;
  }

  /**
   * Checks if all emails in the list are selected.
   * @returns True if all emails in the list are selected.
   */
  get isAllSelected(): boolean {
    const list = this.mails();
    const selected = this.selectedMailIds();
    return list.length > 0 && list.every((m) => selected.has(m.mailId));
  }

  /**
   * Checks if some emails in the list are selected.
   * @returns True if some emails in the list are selected.
   */
  get isSomeSelected(): boolean {
    const list = this.mails();
    const selected = this.selectedMailIds();
    const count = list.filter((m) => selected.has(m.mailId)).length;
    return count > 0 && count < list.length;
  }

  /**
   * Checks if a specific mail is selected.
   *
   * @param mailId - The ID of the mail to check.
   * @returns True if the mail is selected.
   */
  isMailSelected(mailId: string): boolean {
    return this.selectedMailIds().has(mailId);
  }

  /**
   * Gets the display name for a mail.
   * Displays sender name for received emails and recipient names for sent emails.
   *
   * @param mail - The mail to get the display name for.
   * @returns The display name for the mail.
   */
  getMailDisplayName(mail: Mail): string {
    return this.isReceivedTab
      ? formatSenderName(mail.from, this.userMap())
      : formatRecipientNames(mail.to, this.userMap());
  }

  /**
   * Method called when the select all checkbox is toggled.
   *
   * @param checked - True if the select all checkbox is checked, false otherwise.
   */
  onToggleAll(checked: boolean): void {
    this.toggleAll.emit(checked);
  }

  /**
   * Method called when a mail is toggled.
   * @param mailId - The ID of the mail to toggle.
   */
  onToggleMail(mailId: string): void {
    this.toggleSelect.emit(mailId);
  }

  /**
   * Method called when the sort is changed.
   *
   * @param event - The sort change event.
   */
  onSortChange(event: { field: string; direction: 'asc' | 'desc' }): void {
    this.sortChange.emit(event);
  }

  /**
   * Method called when a mail is edited.
   *
   * @param mailId - The ID of the mail to edit.
   */
  onEditSingle(mailId: string): void {
    this.editMail.emit(mailId);
  }

  /**
   * Method called when a mail is deleted.
   *
   * @param mailId - The ID of the mail to delete.
   */
  onDeleteSingle(mailId: string): void {
    this.deleteMail.emit(mailId);
  }

  /**
   * Method called when a mail is clicked.
   *
   * @param mailId - The ID of the mail to open.
   */
  onRowClick(mailId: string): void {
    this.openMail.emit(mailId);
  }

  /**
   * Method called when the paginator is changed.
   *
   * @param event - The page change event.
   */
  onPaginatorChange(event: PageEvent): void {
    this.pageChange.emit({
      pageIndex: event.pageIndex,
      pageSize: event.pageSize,
    });
  }
}
