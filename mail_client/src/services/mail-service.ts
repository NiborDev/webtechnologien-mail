import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import { Environment } from '../environment';

import { Mail } from '../models/Mail';

import { MailResponse } from '../dtos/MailResponse';
import { MailCreate } from '../dtos/MailCreate';
import { MailUpdate } from '../dtos/MailUpdate';
import { MailUpdateIsRead } from '../dtos/MailUpdateIsRead';
import { EmailStatus } from '../enums/email-status';
import { MailPage } from '../dtos/MailPage';

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
    status: EmailStatus,
    page = 0,
    size = 10,
    sort = 'sentAt,desc'
  ): Observable<MailPage> {
    const params: Record<string, string> = {
      status,
      page: page.toString(),
      size: size.toString(),
      sort,
    };

    return this.httpClient.get<MailPage>(`${this.apiBasePath}/mails`, { params });
  }

  public getById(id: string): Observable<Mail> {
    return this.httpClient
      .get<MailResponse>(`${this.apiBasePath}/mails/${id}`)
      .pipe(map(Mail.fromDto));
  }

  public create(mail: MailCreate): Observable<Mail> {
    return this.httpClient
      .post<MailResponse>(`${this.apiBasePath}/mails`, mail)
      .pipe(map(Mail.fromDto));
  }

  public updateData(id: string, mailUpdateData: MailUpdate): Observable<Mail> {
    return this.httpClient
      .patch<MailResponse>(`${this.apiBasePath}/mails/${id}`, mailUpdateData)
      .pipe(map(Mail.fromDto));
  }

  public updateIsRead(id: string, mailUpdateIsRead: MailUpdateIsRead): Observable<Mail> {
    return this.httpClient
      .patch<MailResponse>(`${this.apiBasePath}/mails/${id}/isRead`, mailUpdateIsRead)
      .pipe(map(Mail.fromDto));
  }

  public send(id: string): Observable<Mail> {
    return this.httpClient
      .post<MailResponse>(`${this.apiBasePath}/mails/${id}/transmissions`, {})
      .pipe(map(Mail.fromDto));
  }

  public deleteMail(id: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiBasePath}/mails/${id}`);
  }
}
