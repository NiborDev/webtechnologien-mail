import { UserListResponseDto } from './user-list-response.dto';

/**
 * Data Transfer Object to list Users with pagination data
 *
 * @property {UserListResponseDto[]} content - An array of {@link UserListResponseDto}.
 * @property {number} totalElements - The total number of users.
 * @property {number} totalPages - The total number of pages.
 * @property {number} size - The size of the page.
 * @property {number} number - The current page number.
 */
export interface UserPageResponseDto {
  content: UserListResponseDto[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
