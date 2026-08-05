/**
 * Model representing a user.
 */
export class User {
  /**
   * Constructs a User instance.
   *
   * @param _id - Unique user ID string.
   * @param _email - User email address.
   * @param _firstName - User first name.
   * @param _lastName - User last name.
   */
  constructor(
    private _id: string,
    private _email: string,
    private _firstName: string,
    private _lastName: string,
  ) {}

  /** Gets the user ID. */
  public get id(): string {
    return this._id;
  }

  /** Gets the email address. */
  public get email(): string {
    return this._email;
  }

  /** Gets the first name. */
  public get firstName(): string {
    return this._firstName;
  }

  /** Gets the last name. */
  public get lastName(): string {
    return this._lastName;
  }

  /**
   * It shows the user's full name or their email address.
   *
   * @returns Formatted full name or email string.
   */
  public get fullName(): string {
    return `${this._firstName || ''} ${this._lastName || ''}`.trim() || this._email;
  }

  /** Sets the email address. */
  public set email(email: string) {
    this._email = email;
  }

  /** Sets the first name. */
  public set firstName(firstName: string) {
    this._firstName = firstName;
  }

  /** Sets the last name. */
  public set lastName(lastName: string) {
    this._lastName = lastName;
  }
}

