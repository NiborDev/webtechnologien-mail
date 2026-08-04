import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Environment } from '../environment';

import { AttachmentResponse } from '../dtos/AttachmentResponse';

@Injectable({ providedIn: 'root' })
export class AttachmentService {
  private readonly httpClient: HttpClient = inject(HttpClient);
  private readonly apiBasePath: string = Environment.apiBasePath;

  public upload(files: File[]): Observable<AttachmentResponse[]> {
    const formData = new FormData();

    files.forEach(file => formData.append('files', file));

    return this.httpClient.post<AttachmentResponse[]>(
      `${this.apiBasePath}/attachments`,
      formData
    );
  }


  public download(id: string): Observable<Blob> {
    return this.httpClient.get(
      `${this.apiBasePath}/attachments/${id}`,
      {
        responseType: 'blob'
      }
    );
  }
}
