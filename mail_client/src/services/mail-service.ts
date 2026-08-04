import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Environment } from '../environment';
import { MailStatusEnum } from '../models/mail-status.enum';
import { MailPageDto } from '../dtos/mail-page.dto';

/**
 * Service for handling mail-related API calls
 */
@Injectable({ providedIn: 'root' })
export class MailService {
  private readonly httpClient: HttpClient = inject(HttpClient);
  private readonly apiBasePath: string = Environment.apiBasePath;

  /**
   * Get all mails
   *
   * @param status Mail status (INBOX, SENT, DRAFTS)
   * @param page Page number (default: 0)
   * @param size Number of mails per page (default: 10)
   * @param sort Sort order (default: 'sentAt,desc')
   * @returns Observable with mail page
   */
  public getMails(
    status: MailStatusEnum,
    page = 0,
    size = 10,
    sort = 'sentAt,desc'
  ): Observable<MailPageDto> {
    const params: Record<string, string> = {
      status,
      page: page.toString(),
      size: size.toString(),
      sort,
    };

    return this.httpClient.get<MailPageDto>(`${this.apiBasePath}/mails`, { params });
  }

  public deleteMail(id: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiBasePath}/mails/${id}`);
  }
}
