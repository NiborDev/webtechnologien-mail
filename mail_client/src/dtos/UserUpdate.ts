/**
 * Data Transfer Object for updating an existing user's profile information.
 *
 * @property {string} [firstName] - The updated first name.
 * @property {string} [lastName] - The updated last name.
 * @property {string} [email] - The updated email address.
 */
export type UserUpdate = {
  firstName?: string;
  lastName?: string;
  email?: string;
};

