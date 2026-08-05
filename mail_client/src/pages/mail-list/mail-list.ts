import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { forkJoin, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { MailStatusTabsComponent } from '../../components/mail-status-tabs/mail-status-tabs';
import { MailActionsToolbarComponent } from '../../components/mail-toolbar/mail-toolbar';
import { MailListComponent } from '../../components/mail-list/mail-list';
import { MailService } from '../../services/mail-service';
import { UserService } from '../../services/user-service';
import { EmailStatus } from '../../enums/email-status';
import { Mail } from '../../models/Mail';

/**
 * Page component for orchestrating the email list view.
 *
 * Serves as a smart container component managing state, handling API calls,
 * and passing state down to child presentational components.
 */
@Component({
  selector: 'app-mail-list-page',
  imports: [
    MailStatusTabsComponent,
    MailActionsToolbarComponent,
    MailListComponent,
    MatSnackBarModule,
  ],
  templateUrl: './mail-list.html',
  styleUrl: './mail-list.css',
})
export class MailListPageComponent implements OnInit {
  private readonly mailService = inject(MailService);
  private readonly userService = inject(UserService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  readonly activeStatusTab = signal<EmailStatus>(EmailStatus.RECEIVED);
  readonly pageIndex = signal<number>(0);
  readonly pageSize = signal<number>(10);
  readonly totalElements = signal<number>(0);

  /** List of email models for the current page. */
  readonly mails = signal<Mail[]>([]);
  readonly isLoading = signal<boolean>(false);
  readonly selectedMailIds = signal<Set<string>>(new Set());
  readonly errorMessage = signal<string | null>(null);

  /** Map linking email addresses to user display names (managed on page). */
  readonly userMap = signal<Map<string, string>>(new Map());

  /** Field used for sorting (date, from, subject). */
  readonly sortField = signal<string>('date');
  readonly sortDirection = signal<'asc' | 'desc'>('desc');

  readonly selectedMailCount = computed(() => this.selectedMailIds().size);
  readonly hasSelection = computed(() => this.selectedMailCount() > 0);

  readonly isAllSelected = computed(() => {
    const list = this.mails();
    const sel = this.selectedMailIds();
    return list.length > 0 && list.every((m) => sel.has(m.mailId));
  });

  /** Checks if the active folder is the Drafts folder. */
  get isDraftTab(): boolean {
    return this.activeStatusTab() === EmailStatus.DRAFT;
  }

  /** Constructs the sorting parameter string for API requests. */
  get sortParam(): string {
    let field = this.sortField();
    if (field === 'date') {
      field = this.activeStatusTab() === EmailStatus.RECEIVED ? 'receivedAt' : 'sentAt';
    }
    return `${field},${this.sortDirection()}`;
  }

  /** Lifecycle hook called after component initialization. */
  ngOnInit(): void {
    this.loadUsers();
    this.loadMails();
  }

  /**
   * Fetches user name mapping from UserService and stores it in page state.
   */
  loadUsers(): void {
    if (this.userMap().size > 0) return;
    this.userService.getUserMap().subscribe((map) => this.userMap.set(map));
  }

  /**
   * Fetches emails from the backend for the current status tab, pagination, and sorting settings.
   */
  loadMails(): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.mailService
      .getMails(this.activeStatusTab(), this.pageIndex(), this.pageSize(), this.sortParam)
      .pipe(
        catchError((err) => {
          this.errorMessage.set(err?.error?.message || 'Failed to fetch emails.');
          return of({
            content: [],
            totalElements: 0,
            totalPages: 0,
            size: this.pageSize(),
            number: 0,
          });
        }),
        finalize(() => this.isLoading.set(false)),
      )
      .subscribe((pageDto) => {
        const domainMails = pageDto.content.map((dto) => Mail.fromDto(dto));
        this.mails.set(domainMails);
        this.totalElements.set(pageDto.totalElements);
        this.selectedMailIds.set(new Set());
      });
  }

  /**
   * Handles status tab changes (Inbox, Sent, Drafts).
   *
   * @param status - The newly selected email status.
   */
  onTabChange(status: EmailStatus): void {
    if (this.activeStatusTab() === status) return;
    this.activeStatusTab.set(status);
    this.pageIndex.set(0);
    this.loadMails();
  }

  /**
   * Handles pagination changes from the child paginator component.
   *
   * @param event - Object containing new page index and size.
   */
  onPageChange(event: { pageIndex: number; pageSize: number }): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadMails();
  }

  /**
   * Handles sort parameter changes from the child list header control.
   *
   * @param event - Object containing new sort field and direction.
   */
  onSortChange(event: { field: string; direction: 'asc' | 'desc' }): void {
    this.sortField.set(event.field);
    this.sortDirection.set(event.direction);
    this.pageIndex.set(0);
    this.loadMails();
  }

  /**
   * Toggles the selection state of a single email.
   *
   * @param mailId - The unique ID of the target email.
   */
  onToggleSelect(mailId: string): void {
    const currentSet = new Set(this.selectedMailIds());
    if (currentSet.has(mailId)) {
      currentSet.delete(mailId);
    } else {
      currentSet.add(mailId);
    }
    this.selectedMailIds.set(currentSet);
  }

  /**
   * Selects or deselects all emails on the current page.
   *
   * @param selectAll - True to select all, false to deselect all.
   */
  onToggleAll(selectAll: boolean): void {
    const newSet = new Set<string>();
    if (selectAll) {
      this.mails().forEach((m) => newSet.add(m.mailId));
    }
    this.selectedMailIds.set(newSet);
  }

  /**
   * Navigates to the compose view for creating a new email.
   */
  onCreateMail(): void {
    this.router.navigate(['/mails/new']);
  }

  /**
   * Navigates to the detailed view of a selected email.
   *
   * @param mailId - The unique ID of the target email.
   */
  onOpenMail(mailId: string): void {
    this.router.navigate(['/mails', mailId]);
  }

  /**
   * Navigates to edit the draft email.
   */
  onEditSelectedMail(): void {
    const selectedIds = Array.from(this.selectedMailIds());
    if (selectedIds.length === 1) {
      this.router.navigate(['/mails', selectedIds[0]]);
    }
  }

  /**
   * Deletes a single email by ID.
   *
   * @param mailId - The unique ID of the email to delete.
   */
  onDeleteSingleMail(mailId: string): void {
    this.isLoading.set(true);
    this.mailService
      .deleteMail(mailId)
      .pipe(
        catchError((err) => {
          this.errorMessage.set(err?.error?.message || 'Failed to delete email.');
          return of(null);
        }),
        finalize(() => this.isLoading.set(false)),
      )
      .subscribe(() => {
        this.loadMails();
        this.snackBar.open('Email deleted successfully.', 'Close', { duration: 3000 });
      });
  }

  /**
   * Deletes all currently selected emails in parallel.
   */
  onDeleteSelectedMails(): void {
    const idsToDelete = Array.from(this.selectedMailIds());
    if (idsToDelete.length === 0) return;

    this.isLoading.set(true);
    const deleteObservables = idsToDelete.map((id) =>
      this.mailService.deleteMail(id).pipe(catchError(() => of(null))),
    );

    forkJoin(deleteObservables)
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe(() => {
        this.selectedMailIds.set(new Set());
        this.loadMails();
        this.snackBar.open(`${idsToDelete.length} email(s) deleted successfully.`, 'Close', {
          duration: 3000,
        });
      });
  }
}
