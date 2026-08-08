import { Component, effect, inject, input, output, signal } from '@angular/core';
import { HttpEventType } from '@angular/common/http';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { MailCreate } from '../../dtos/MailCreate';
import { RecipientInputComponent } from '../recipient-input/recipient-input';
import { Mail } from '../../models/Mail';
import { Attachment } from '../../models/Attachment';
import { AttachmentResponse } from '../../dtos/AttachmentResponse';
import { AttachmentService } from '../../services/attachment-service';
import { formatBytes } from '../../utils/file-formatter';

import { AttachmentList } from '../attachment-list/attachment-list';

/**
 * Validation: Checks if a Mail has at least one recipient
 * @returns ValidationError with noRecipient: true if the mail has no recipient, otherwise null
 * @param control Object for FormControl
 */
const recipientValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const to = control.get('to')?.value as string [] ?? []
  const cc = control.get('cc')?.value as string [] ?? []
  const bcc = control.get('bcc')?.value as string [] ?? []
  if (to.length+cc.length+bcc.length <1) {
    return { noRecipient: true };
  }
  return null;
};


/**
 * Component for composing, editing, saving, and sending email forms with attachment support.
 */
@Component({
  selector: 'app-mail-form',
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatProgressBarModule,
    RecipientInputComponent,
    AttachmentList,
  ],
  templateUrl: './mail-form.html',
  styleUrl: './mail-form.css',
})
export class MailForm {
  private readonly formBuilder = inject(FormBuilder);
  private readonly attachmentService = inject(AttachmentService);

  public readonly save = output<MailCreate>();
  public readonly send = output<MailCreate>();
  public readonly initialValue = input<Partial<Mail>>();

  protected readonly attachments = signal<Attachment[]>([]);
  protected readonly isUploading = signal<boolean>(false);
  protected readonly uploadProgress = signal<number>(0);

  /**
   * defines Form for Mail editing
   * @protected
   */
  protected readonly form = this.formBuilder.nonNullable.group({
    to: this.formBuilder.nonNullable.control<string[]>([]),
    cc: this.formBuilder.nonNullable.control<string[]>([]),
    bcc: this.formBuilder.nonNullable.control<string[]>([]),
    replyTo: this.formBuilder.nonNullable.control<string[]>([]),
    subject: [''],
    bodyText: [''],
  });

  /**
   * enables validation of the Form (sets validator as required to send a Mail)
   * @private
   */
  private enableValidation(){
    this.form.controls.subject.setValidators(Validators.required)
    this.form.setValidators(recipientValidator)
    this.form.updateValueAndValidity()
  }

  /**
   * disables validation of the Form
   * @private
   */
  private disableValidation(){
    this.form.controls.subject.setValidators([])
    this.form.setValidators([])
    this.form.updateValueAndValidity()
    }

  /**
   * Constructs a MailCreate payload object from current form values and attachments.
   *
   * @returns Formatted MailCreate DTO payload.
   */
  private buildMailCreatePayload(): MailCreate {
    const values = this.form.getRawValue();
    return {
      ...values,
      attachments: this.attachments().map((att) => att.toDto()),
    };
  }

  /**
   * Handles file selection from the file input element and uploads files with progress monitoring.
   *
   * @param event File input change event.
   */
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      return;
    }

    const files: File[] = Array.from(input.files);
    this.isUploading.set(true);
    this.uploadProgress.set(0);

    this.attachmentService.uploadWithProgress(files).subscribe({
      next: (event) => {
        if (event.type === HttpEventType.UploadProgress && event.total) {
          const progress = Math.round((100 * event.loaded) / event.total);
          this.uploadProgress.set(progress);
        } else if (event.type === HttpEventType.Response && event.body) {
          this.isUploading.set(false);
          this.uploadProgress.set(0);
          const newModels = event.body.map((dto) => Attachment.fromDto(dto));
          this.attachments.update((current) => [...current, ...newModels]);
        }
      },
      error: (err) => {
        this.isUploading.set(false);
        this.uploadProgress.set(0);
        console.error('Upload error:', err);
      },
    });

    input.value = '';
  }

  /**
   * Triggers downloading of the specified attachment.
   *
   * @param attachment Attachment object to download.
   */
  onDownloadAttachment(attachment: Attachment): void {
    if (attachment.attachmentId) {
      this.attachmentService.downloadAndSave(attachment.attachmentId, attachment.fileName);
    }
  }

  /**
   * Removes an attachment from the draft and deletes it on the server if it exists.
   *
   * @param attachment Attachment object to remove.
   */
  onRemoveAttachment(attachment: Attachment): void {
    if (attachment && attachment.attachmentId) {
      this.attachmentService.delete(attachment.attachmentId).subscribe({
        next: () => {
          this.attachments.update((current) =>
            current.filter((a) => a.attachmentId !== attachment.attachmentId),
          );
        },
        error: (err) => {
          console.error('Failed to delete attachment:', err);
        },
      });
    } else {
      this.attachments.update((current) =>
        current.filter((a) => a !== attachment),
      );
    }
  }

  /**
   * Formats raw bytes into a human-readable file size string.
   *
   * @param bytes Number of bytes to format.
   * @returns Formatted string representation.
   */
  protected formatBytes(bytes: number): string {
    return formatBytes(bytes);
  }

  /**
   * Validates form inputs and emits the `send` output event with the created mail payload.
   */
  onSend() {
    this.enableValidation();

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.send.emit(this.buildMailCreatePayload());
  }

  /**
   * Disables validation and emits the `save` output event to store draft mail payload.
   */
  onSave() {
    this.disableValidation();
    this.form.markAsUntouched();
    this.save.emit(this.buildMailCreatePayload());
  }

  /**
   * Constructor to initialise Form with existing data if required
   */
  constructor() {
    effect(() => {
      const value = this.initialValue();

      if (!value) {
        return;
      }

      this.form.reset({
        to: value.to ?? [],
        cc: value.cc ?? [],
        bcc: value.bcc ?? [],
        replyTo: value.replyTo ?? [],
        subject: value.subject ?? '',
        bodyText: value.bodyText ?? '',
      });

      if (value.attachments) {
        const atts: Attachment[] = value.attachments.map((att) =>
          att instanceof Attachment ? att : Attachment.fromDto(att as AttachmentResponse),
        );
        this.attachments.set(atts);
      }
    });
  }
}
