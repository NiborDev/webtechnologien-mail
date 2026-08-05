import { MailResponse } from './MailResponse';

/**
 * Data Transfer Object to list Mails with pagination data
 *
 * @property {MailResponse[]} content - An array of {@link MailResponse}.
 * @property {number} totalElements - The total number of mails.
 * @property {number} totalPages - The total number of pages.
 * @property {number} size - The size of the page.
 * @property {number} number - The current page number.
 */
export interface MailPage {
  content: MailResponse[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first?: boolean;
  last?: boolean;
  numberOfElements?: number;
  empty?: boolean;
}
