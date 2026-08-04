export class User {
  constructor(
    private _id: string,
    private _email: string,
    private _firstName: string,
    private _lastName: string,
  ) {}

  public get id(): string {
    return this._id;
  }

  public get email(): string {
    return this._email;
  }

  public get firstName(): string {
    return this._firstName;
  }

  public get lastName(): string {
    return this._lastName;
  }

  public get fullName(): string {
    return `${this._firstName || ''} ${this._lastName || ''}`.trim() || this._email;
  }

  public set email(email: string) {
    this._email = email;
  }

  public set firstName(firstName: string) {
    this._firstName = firstName;
  }

  public set lastName(lastName: string) {
    this._lastName = lastName;
  }
}
