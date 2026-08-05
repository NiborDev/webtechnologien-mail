import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { describe, it, expect, beforeEach, afterEach } from 'vitest';
import { MailService } from './mail-service';
import { EmailStatus } from '../enums/email-status';
import { MailPage } from '../dtos/MailPage';
import { Environment } from '../environment';

describe('MailService', () => {
  let service: MailService;
  let httpMock: HttpTestingController;

  const mockPageDto: MailPage = {
    content: [
      {
        mailId: '123-abc',
        from: 'sender@example.com',
        to: ['receiver@example.com'],
        cc: [],
        bcc: [],
        replyTo: [],
        subject: 'Test Subject',
        bodyText: 'Test Body Content',
        status: EmailStatus.RECEIVED,
        attachments: [],
        isRead: false,
        isMarked: false,
        createdAt: '2026-08-04T10:00:00.000Z',
        updatedAt: '2026-08-04T10:00:00.000Z',
      },
    ],
    totalElements: 1,
    totalPages: 1,
    size: 10,
    number: 0,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MailService, provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(MailService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch mails via getMails with query parameters', () => {
    service.getMails(EmailStatus.RECEIVED, 0, 10, 'receivedAt,desc').subscribe((page) => {
      expect(page).toEqual(mockPageDto);
      expect(page.content.length).toBe(1);
      expect(page.content[0].subject).toBe('Test Subject');
    });

    const req = httpMock.expectOne((request) => {
      return (
        request.url === `${Environment.apiBasePath}/mails` &&
        request.params.get('status') === EmailStatus.RECEIVED &&
        request.params.get('page') === '0' &&
        request.params.get('size') === '10' &&
        request.params.get('sort') === 'receivedAt,desc'
      );
    });

    expect(req.request.method).toBe('GET');
    req.flush(mockPageDto);
  });

  it('should delete a mail via deleteMail', () => {
    const mailId = '123-abc';
    service.deleteMail(mailId).subscribe();

    const req = httpMock.expectOne(`${Environment.apiBasePath}/mails/${mailId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
