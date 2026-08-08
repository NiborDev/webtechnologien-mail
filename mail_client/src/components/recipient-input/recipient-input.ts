import {
  Component,
  forwardRef,
  input,
  signal
} from '@angular/core';

import {
  ControlValueAccessor, FormControl,
  NG_VALUE_ACCESSOR,
  Validators
} from '@angular/forms';

import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {MatChipInputEvent, MatChipsModule} from '@angular/material/chips';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import { MatFormFieldModule } from "@angular/material/form-field";

/**
 * Component for managing input of multiple mail-addresses
 */
@Component({
  selector: 'app-recipient-input',
  standalone: true,
  imports: [
    MatChipsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule
  ],
  templateUrl: './recipient-input.html',
  styleUrl: './recipient-input.css',

  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RecipientInputComponent),
      multi: true
    }
  ]
})
export class RecipientInputComponent
  implements ControlValueAccessor {

  readonly label = input.required<string>();

  readonly separatorKeys = [ENTER, COMMA];

  readonly recipients = signal<string[]>([]);

  private readonly emailControl = new FormControl('', Validators.email);

  disabled = false;

  private onChange = (_: string[]) => {};
  private onTouched = () => {};

  writeValue(value: string[]): void {
    this.recipients.set(value ?? []);
  }

  registerOnChange(fn: (value: string[]) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(disabled: boolean): void {
    this.disabled = disabled;
  }

  /**
   * Validate and add a Mail-Address
   * @param event
   */
  add(event: MatChipInputEvent): void {

    const value = event.value.trim();

    if (!value) {
      return;
    }

    if (!this.isValidMail(value)) {
      event.chipInput?.clear();
      return;
    }

    if (!this.recipients().includes(value)) {

      const mails = [...this.recipients(), value];

      this.recipients.set(mails);

      this.onChange(mails);
    }

    event.chipInput?.clear();

    this.onTouched();
  }

  /**
   * Remove a mail-address
   * @param mail
   */
  remove(mail: string): void {

    const mails = this.recipients()
      .filter(m => m !== mail);

    this.recipients.set(mails);

    this.onChange(mails);

    this.onTouched();
  }

  private isValidMail(mail: string): boolean {
    this.emailControl.setValue(mail);
    return this.emailControl.valid;

  }

}
