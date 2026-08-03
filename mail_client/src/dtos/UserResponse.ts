import { User } from '../models/User';

export type UserResponse = {
  userId: string;
  firstName: string;
  lastName: string;
  email: string;
};

export function fromUser(user: User): UserResponse {
  return {
    userId: user.id,
    email: user.email,
    firstName: user.firstName,
    lastName: user.lastName,
  };
}
