import { Component, input, output } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { Attachment } from '../../models/Attachment';
import { formatBytes } from '../../utils/file-formatter';

/**
 * Reusable standalone component for rendering a list of mail attachments.
 * Supports optional download and removal actions.
 */
@Component({
  selector: 'app-attachment-list',
  imports: [MatIconButton, MatIcon],
  templateUrl: './attachment-list.html',
  styleUrl: './attachment-list.css',
})
export class AttachmentList {
  /** List of attachment domain models to display. */
  public readonly attachments = input<Attachment[]>([]);

  /** Controls whether the remove/delete action button is shown. Defaults to false. */
  public readonly removable = input<boolean>(false);

  /** Controls whether the download action button is shown. Defaults to true. */
  public readonly downloadable = input<boolean>(true);

  /** Event emitted when the user clicks the download button for an attachment. */
  public readonly download = output<Attachment>();

  /** Event emitted when the user clicks the remove/delete button for an attachment. */
  public readonly remove = output<Attachment>();

  /**
   * Formats raw byte sizes into human-readable strings.
   *
   * @param bytes Size in bytes.
   * @returns Formatted file size string.
   */
  protected formatBytes(bytes: number): string {
    return formatBytes(bytes);
  }
}
