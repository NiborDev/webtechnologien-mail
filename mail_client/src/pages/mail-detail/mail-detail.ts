import { Component, inject } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MailService} from '../../services/mail-service';
import {toSignal} from '@angular/core/rxjs-interop';
import {MailViewer} from '../../components/mail-viewer/mail-viewer';
import {tap} from 'rxjs';
import {MailUpdateIsRead} from '../../dtos/MailUpdateIsRead';

/**
 * Component for the page to view a Mail
 */
@Component({
  selector: 'app-mail-detail',
  imports: [MailViewer],
  templateUrl: './mail-detail.html',
  styleUrl: './mail-detail.css',
})
export class MailDetail {
  private readonly mailService: MailService = inject(MailService)
  private readonly route = inject(ActivatedRoute);

  private readonly id = this.route.snapshot.paramMap.get('id')!;

  /**
   * Gets the Mail-Object and sets isRead status to true if currently false
   * @protected
   */
  protected readonly mail = toSignal(
    this.mailService.getById(this.id).pipe(
      tap(mail =>{
        if (mail && !mail.isRead){
          const mailUpdateIsRead : MailUpdateIsRead ={
            isRead : true
          }
          this.mailService.updateIsRead(this.id, mailUpdateIsRead).subscribe()
        }
      })
    ),
    { initialValue: null }
  );


}
