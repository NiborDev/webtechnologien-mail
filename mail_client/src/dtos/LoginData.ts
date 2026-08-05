/**
 * Data Transfer Object containing login credentials.
 *
 * @property {string} email - The user's email address.
 * @property {string} password - The user's password.
 */
export class LoginData {
  constructor(
    public email: string,
    public password: string
  ) {}
}

