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

import {COMMA, ENTER, SEMICOLON, SPACE} from '@angular/cdk/keycodes';
import {MatChipInputEvent, MatChipsModule} from '@angular/material/chips';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import { MatFormFieldModule } from "@angular/material/form-field";

/**
 * Component for managing input of multiple mail-addresses
 *
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

  /**
   * The label for the input
   */
  readonly label = input.required<string>();

  /**
   * Keys that can be used to separate mail-addresses in the input
   */
  readonly separatorKeys = [ENTER, COMMA, SPACE];

  /**
   * List of mail-addresses
   */
  readonly recipients = signal<string[]>([]);

  /**
   * Form control for validating the email address
   */
  private readonly emailControl = new FormControl('', Validators.email);

  /**
   * Disabled state of the component
   */
  disabled = false;

  /**
   * Callback function for when the value changes
   */
  private onChange = (_: string[]) => {};

  /**
   * Callback function for when the component is touched
   */
  private onTouched = () => {};

  /**
   * Writes a value to the component
   * @param value list of mail-addresses
   */
  writeValue(value: string[]): void {
    this.recipients.set(value ?? []);
  }

  /**
   * Registers a callback function for when the value changes
   * @param fn callback function
   */
  registerOnChange(fn: (value: string[]) => void): void {
    this.onChange = fn;
  }

  /**
   * Registers a callback function for when the component is touched
   * @param fn callback function
   */
  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  /**
   * Sets the disabled state of the component
   * @param disabled state of the component
   */
  setDisabledState(disabled: boolean): void {
    this.disabled = disabled;
  }

  /**
   * Validate and add a Mail-Address
   * @param event input event
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
   * @param mail mail-address to remove
   */
  remove(mail: string): void {

    const mails = this.recipients()
      .filter(m => m !== mail);

    this.recipients.set(mails);

    this.onChange(mails);

    this.onTouched();
  }

  /**
   * Validate if a string is a valid mail-address
   * @param mail mail-address to validate
   * @returns true if the mail-address is valid, false otherwise
   */
  private isValidMail(mail: string): boolean {
    this.emailControl.setValue(mail);
    return this.emailControl.valid;

  }

}
