export class User {
  constructor(
    private _id: string,
    private _email: string,
    private _firstName: string,
    private _lastName: string,
  ) {}
  public get id(): string {
    return this._id
  }
  //TODO
}
