import { Component, input, output } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MailStatusEnum } from '../../models/mail-status.enum';

/**
 * Component for switching between mail status tabs.
 *
 * This component handles:
 * - Displaying different mail status tabs (Inbox, Sent, Drafts)
 * - Emitting the selected tab change event
 */
@Component({
  selector: 'app-mail-status-tabs',
  imports: [MatTabsModule, MatIconModule],
  templateUrl: './mail-status-tabs.html',
  styleUrl: './mail-status-tabs.css',
})
export class MailStatusTabsComponent {
  readonly activeStatusTab = input.required<MailStatusEnum>();
  readonly tabChange = output<MailStatusEnum>();

  readonly statusOptions = [
    { status: MailStatusEnum.RECEIVED, label: 'Inbox / Received', icon: 'inbox' },
    { status: MailStatusEnum.SENT, label: 'Sent', icon: 'send' },
    { status: MailStatusEnum.DRAFT, label: 'Drafts', icon: 'drafts' },
  ];

  /**
   * Gets the index of the currently active tab based on the activeStatusTab input.
   *
   * @returns The index of the active tab, or 0 if not found.
   */
  get selectedIndex(): number {
    const current = this.activeStatusTab();
    const index = this.statusOptions.findIndex((opt) => opt.status === current);
    return index >= 0 ? index : 0;
  }

  /**
   * Handler method for when the tab selection changes.
   * Emits the tabChange output event with the new status.
   *
   * @param index - The index of the newly selected tab.
   */
  onTabChange(index: number): void {
    const selectedStatus = this.statusOptions[index].status;
    if (selectedStatus !== this.activeStatusTab()) {
      this.tabChange.emit(selectedStatus);
    }
  }
}
