import { UserCreate } from "./UserCreate";

export class RegistrationData {
  constructor(
    public firstName: string = '',
    public lastName: string = '',
    public email: string = '',
    public password: string = ''
  ) {}

  /**
   * Mapped RegistrationData auf das UserCreate DTO
   */
  public toUserCreate(): UserCreate {
    return {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      password: this.password,
    };
  }
}
