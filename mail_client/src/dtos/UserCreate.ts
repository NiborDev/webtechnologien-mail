/**
 * Data Transfer Object for creating a new user on the server.
 *
 * @property {string} firstName - The user's first name.
 * @property {string} lastName - The user's last name.
 * @property {string} email - The user's email address.
 * @property {string} password - The user's password.
 */
export type UserCreate = {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
};

