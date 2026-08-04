import { Component, inject, output } from '@angular/core';
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
import {MailCreate} from '../dtos/MailCreate';
import {EMailStatus} from '../enums/email-status';
import {Source} from '../enums/source';
import {RecipientInputComponent} from './recipient-input';

//validate if there is a recipient
const recipientValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const to = control.get('to')?.value as string []
  const cc = control.get('cc')?.value as string []
  const bcc = control.get('bcc')?.value as string []
  if (to.length+cc.length+bcc.length <1) {
    return { noRecipient: true };
  }
  return null;
};

@Component({
  selector: 'app-mail-form',
  imports: [ReactiveFormsModule, MatInputModule, MatFormFieldModule, MatButtonModule, RecipientInputComponent],
  templateUrl: './mail-form.html',
  styleUrl: './mail-form.css',
})
export class MailForm {
  private readonly formBuilder = inject(FormBuilder);

  public readonly save = output<MailCreate>();

  protected readonly form = this.formBuilder.nonNullable.group({
    from: ['', [Validators.required, Validators.email]],
    to: this.formBuilder.nonNullable.control<string[]>([]),
    cc: this.formBuilder.nonNullable.control<string[]>([]),
    bcc: this.formBuilder.nonNullable.control<string[]>([]),
    replyTo: this.formBuilder.nonNullable.control<string[]>([]),
    subject: ['', Validators.required],
    bodyText: ['']
  },{
    validators: [recipientValidator]
  });

  onSend(){
    if(this.form.invalid){
      this.form.markAllAsTouched()
      return
    }

    const values = this.form.getRawValue()

//TODO attachments
    const mail: MailCreate = {
      ...values,
      status: EMailStatus.DRAFT,
      attachments: [],
      source: Source.INTERN

    }

    this.save.emit(mail)

  }


}
