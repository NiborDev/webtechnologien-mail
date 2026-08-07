import {Component, effect, inject, input, output} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import {MailCreate} from '../../dtos/MailCreate';
import {RecipientInputComponent} from '../recipient-input/recipient-input';
import {Mail} from '../../models/Mail';

/**
 * Validation: Checks if a Mail has at least one recipient
 * @return ValidationError with noRecipient: true if the mail has no recipient, otherwise null
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
 * Component for editing a Mail
 */
@Component({
  selector: 'app-mail-form',
  imports: [ReactiveFormsModule, MatInputModule, MatFormFieldModule, MatButtonModule, RecipientInputComponent],
  templateUrl: './mail-form.html',
  styleUrl: './mail-form.css',
})
export class MailForm {
  private readonly formBuilder = inject(FormBuilder);

  public readonly save = output<MailCreate>();
  public readonly send = output<MailCreate>();
  public readonly initialValue = input<Partial<Mail>>();

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
    bodyText: ['']
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
   * Callback for send-Button
   * Validates Form before emitting
   */
  onSend(){

    this.enableValidation()

    if(this.form.invalid){
      this.form.markAllAsTouched()
      return
    }

    const values = this.form.getRawValue()


//TODO attachments
    const mail: MailCreate = {
      ...values,
      attachments: []

    }

    this.send.emit(mail)

  }

  /**
   * Callback for save-as-draft-Button
   */
  onSave(){

    this.disableValidation()
    this.form.markAsUntouched()

    const values = this.form.getRawValue()


//TODO attachments
    const mail: MailCreate = {
      ...values,
      attachments: [],

    }

    this.save.emit(mail)

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
        bodyText: value.bodyText ?? ''
      });
    });
  }


}
