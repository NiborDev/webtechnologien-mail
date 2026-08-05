import { UserCreate } from './UserCreate';

/**
 * Data Transfer Object for user registration input.
 *
 * @property {string} firstName - The user's first name.
 * @property {string} lastName - The user's last name.
 * @property {string} email - The user's email address.
 * @property {string} password - The user's password.
 */
export class RegistrationData {
  constructor(
    public firstName: string = '',
    public lastName: string = '',
    public email: string = '',
    public password: string = ''
  ) {}

  /**
   * Maps RegistrationData to the UserCreate DTO expected by the API.
   *
   * @returns A UserCreate object.
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

