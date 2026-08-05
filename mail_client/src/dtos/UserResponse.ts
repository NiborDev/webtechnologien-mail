import { User } from '../models/User';

/**
 * Data Transfer Object representing user data.
 *
 * @property {string} userId - The unique identifier of the user.
 * @property {string} firstName - The first name of the user.
 * @property {string} lastName - The last name of the user.
 * @property {string} email - The email address of the user.
 */
export type UserResponse = {
  userId: string;
  firstName: string;
  lastName: string;
  email: string;
};

/**
 * Helper function to convert a User model into a UserResponse DTO.
 *
 * @param user - The User model instance.
 * @returns A UserResponse object.
 */
export function fromUser(user: User): UserResponse {
  return {
    userId: user.id,
    email: user.email,
    firstName: user.firstName,
    lastName: user.lastName,
  };
}

