import { UserListResponse } from './UserListResponse';

/**
 * Data Transfer Object to list Users with pagination data
 *
 * @property {UserListResponse[]} content - An array of {@link UserListResponse}.
 * @property {number} totalElements - The total number of users.
 * @property {number} totalPages - The total number of pages.
 * @property {number} size - The size of the page.
 * @property {number} number - The current page number.
 */
export interface UserPageResponse {
  content: UserListResponse[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
