/**
 * Data Transfer Object for changing a user's password.
 *
 * @property {string} oldPassword - The current password for verification.
 * @property {string} newPassword - The new password to set.
 */
export type UserPasswordUpdate = {
  oldPassword: string;
  newPassword: string;
};

