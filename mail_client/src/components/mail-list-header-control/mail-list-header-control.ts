import { Component, input, output } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatMenuModule } from '@angular/material/menu';

/**
 * Component for mail header control.
 *
 * This component handles:
 * - Selection of all emails
 * - Sorting of emails by date, sender, or subject
 * - Direction of sorting (ascending or descending)
 */
@Component({
  selector: 'app-mail-header-control',
  imports: [
    MatCheckboxModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatMenuModule,
  ],
  templateUrl: './mail-list-header-control.html',
  styleUrl: './mail-list-header-control.css',
})
export class MailHeaderControlComponent {
  readonly isAllSelected = input<boolean>(false);
  readonly isSomeSelected = input<boolean>(false);
  readonly sortField = input<string>('date');
  readonly sortDirection = input<'asc' | 'desc'>('desc');

  readonly toggleAll = output<boolean>();
  readonly sortChange = output<{ field: string; direction: 'asc' | 'desc' }>();

  /**
   * Getter for the sort field label.
   * @returns The sort field label.
   */
  get sortFieldLabel(): string {
    switch (this.sortField()) {
      case 'from':
        return 'Name';
      case 'subject':
        return 'Subject';
      case 'date':
      default:
        return 'Date';
    }
  }

  /**
   * Method called when the select all checkbox is toggled.
   * @param checked - The checked state of the checkbox.
   */
  onToggleAll(checked: boolean): void {
    this.toggleAll.emit(checked);
  }

  /**
   * Method called when a sort field is selected.
   * @param field - The field to sort by.
   */
  onSelectSortField(field: string): void {
    this.sortChange.emit({ field, direction: this.sortDirection() });
  }

  /**
   * Method called when the sort direction is toggled.
   */
  onToggleSortDirection(): void {
    const newDir = this.sortDirection() === 'desc' ? 'asc' : 'desc';
    this.sortChange.emit({ field: this.sortField(), direction: newDir });
  }
}
