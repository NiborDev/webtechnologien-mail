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
import {EmailStatus} from '../../enums/email-status';
import {Source} from '../../enums/source';
import {RecipientInputComponent} from '../recipient-input/recipient-input';
import {Mail} from '../../models/Mail';

//validate if there is a recipient
const recipientValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const to = control.get('to')?.value as string [] ?? []
  const cc = control.get('cc')?.value as string [] ?? []
  const bcc = control.get('bcc')?.value as string [] ?? []
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
  public readonly send = output<MailCreate>();
  public readonly initialValue = input<Partial<Mail>>();

  protected readonly form = this.formBuilder.nonNullable.group({
    from: [''],
    to: this.formBuilder.nonNullable.control<string[]>([]),
    cc: this.formBuilder.nonNullable.control<string[]>([]),
    bcc: this.formBuilder.nonNullable.control<string[]>([]),
    replyTo: this.formBuilder.nonNullable.control<string[]>([]),
    subject: [''],
    bodyText: ['']
  });

  private enableValidation(){
    this.form.controls.from.setValidators([Validators.required, Validators.email])
    this.form.controls.subject.setValidators(Validators.required)
    this.form.setValidators(recipientValidator)
    this.form.updateValueAndValidity()
  }

  private disableValidation(){
    this.form.controls.from.setValidators([])
    this.form.controls.subject.setValidators([])
    this.form.setValidators([])
    this.form.updateValueAndValidity()
  }

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
      status: EmailStatus.DRAFT,
      attachments: [],
      source: Source.INTERN

    }

    this.send.emit(mail)

  }

  onSave(){

    this.disableValidation()
    this.form.markAsUntouched()

    const values = this.form.getRawValue()


//TODO attachments
    const mail: MailCreate = {
      ...values,
      status: EmailStatus.DRAFT,
      attachments: [],
      source: Source.INTERN

    }

    this.save.emit(mail)

  }

  constructor() {
    effect(() => {
      const value = this.initialValue();

      if (!value) {
        return;
      }

      this.form.reset({
        from: value.from ?? '',
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
