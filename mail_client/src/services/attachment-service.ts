import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Environment } from '../environment';

import { AttachmentResponse } from '../dtos/AttachmentResponse';

/**
 * Service for managing file attachments including uploading, downloading, and deleting files.
 */
@Injectable({ providedIn: 'root' })
export class AttachmentService {
  private readonly httpClient: HttpClient = inject(HttpClient);
  private readonly apiBasePath: string = Environment.apiBasePath;

  /**
   * Helper method to construct FormData from an array of files.
   *
   * @param files Array of files to attach.
   * @returns FormData object containing files.
   */
  private createFormData(files: File[]): FormData {
    const formData = new FormData();
    files.forEach((file) => formData.append('files', file));
    return formData;
  }

  /**
   * Uploads multiple files to the backend server.
   *
   * @param files Array of files to upload.
   * @returns Observable emitting the list of created AttachmentResponse objects.
   */
  public upload(files: File[]): Observable<AttachmentResponse[]> {
    return this.httpClient.post<AttachmentResponse[]>(
      `${this.apiBasePath}/attachments`,
      this.createFormData(files),
    );
  }

  /**
   * Uploads files with progress events reporting.
   *
   * @param files Array of files to upload.
   * @returns Observable of HttpEvent emitting upload progress and response data.
   */
  public uploadWithProgress(files: File[]): Observable<HttpEvent<AttachmentResponse[]>> {
    return this.httpClient.post<AttachmentResponse[]>(
      `${this.apiBasePath}/attachments`,
      this.createFormData(files),
      {
        reportProgress: true,
        observe: 'events',
      },
    );
  }

  /**
   * Downloads an attachment file by its ID as a Blob.
   *
   * @param id The unique identifier of the attachment.
   * @returns Observable emitting the raw file Blob.
   */
  public download(id: string): Observable<Blob> {
    return this.httpClient.get(`${this.apiBasePath}/attachments/${id}`, {
      responseType: 'blob',
    });
  }

  /**
   * Downloads an attachment and triggers a browser file download dialog.
   *
   * @param id The unique identifier of the attachment.
   * @param fileName The desired filename for the downloaded file.
   */
  public downloadAndSave(id: string, fileName: string): void {
    this.download(id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
      },
    });
  }

  /**
   * Deletes an attachment by its ID.
   *
   * @param id The unique identifier of the attachment to delete.
   * @returns Observable emitting when deletion is complete.
   */
  public delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiBasePath}/attachments/${id}`);
  }
}
