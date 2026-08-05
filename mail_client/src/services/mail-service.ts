import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import { Environment } from '../environment';

import { Mail } from '../models/Mail';

import { MailResponse } from '../dtos/MailResponse';
import { MailCreate } from '../dtos/MailCreate';
import { MailUpdate } from '../dtos/MailUpdate';
import { MailUpdateStatus } from '../dtos/MailUpdateStatus';
import { MailStatusEnum } from '../models/mail-status.enum';
import { MailPageDto } from '../dtos/mail-page.dto';

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

  public getById(id: string): Observable<Mail> {
    return this.httpClient
      .get<MailResponse>(`${this.apiBasePath}/mails/${id}`)
      .pipe(map(this.mapToMail));
  }

  public create(mail: MailCreate): Observable<Mail> {
    return this.httpClient
      .post<MailResponse>(`${this.apiBasePath}/mails`, mail)
      .pipe(map(this.mapToMail));
  }

  public updateData(id: string, mailUpdateData: MailUpdate): Observable<Mail> {
    return this.httpClient
      .patch<MailResponse>(`${this.apiBasePath}/mails/${id}`, mailUpdateData)
      .pipe(map(this.mapToMail));
  }

  public updateStatus(id: string, mailUpdateStatus: MailUpdateStatus): Observable<Mail> {
    return this.httpClient
      .patch<MailResponse>(`${this.apiBasePath}/mails/${id}/status`, mailUpdateStatus)
      .pipe(map(this.mapToMail));
  }

  public send(id: string): Observable<Mail> {
    return this.httpClient
      .post<MailResponse>(`${this.apiBasePath}/mails/${id}/transmissions`, {})
      .pipe(map(this.mapToMail));
  }

  public delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiBasePath}/mails/${id}`);
  }


  private mapToMail(res: MailResponse): Mail {
    return new Mail(
      res.mailId,
      res.from,
      res.to,
      res.cc,
      res.bcc,
      res.replyTo,
      res.subject,
      res.bodyText,
      res.status,
      res.attachments,
      res.isRead,
      res.isMarked,
      res.sentAt,
      res.receivedAt,
      res.source
    );
  }
}
