import { UserResponse } from './UserResponse';

export type LoginResponse = {
  token: string;
  user: UserResponse;
}
