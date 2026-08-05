import { UserResponse } from './UserResponse';

/**
 * Response payload returned upon successful login.
 *
 * @property {string} token - The issued JSON Web Token (JWT).
 * @property {UserResponse} user - The authenticated user's profile details.
 */
export type LoginResponse = {
  token: string;
  user: UserResponse;
};

