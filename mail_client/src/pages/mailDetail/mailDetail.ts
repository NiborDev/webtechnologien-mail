import { Component, inject } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MailService} from '../../services/mail-service';
import {toSignal} from '@angular/core/rxjs-interop';
import {MailViewer} from '../../components/mail-viewer';

@Component({
  selector: 'app-mail-detail',
  imports: [MailViewer],
  templateUrl: './mailDetail.html',
  styleUrl: './mailDetail.css',
})
export class MailDetail {
  private readonly router: Router = inject(Router);
  private readonly mailService: MailService = inject(MailService)
  private readonly route = inject(ActivatedRoute);

  private readonly id = this.route.snapshot.paramMap.get('id')!;

  protected readonly mail = toSignal(
    this.mailService.getById(this.id),
    { initialValue: null }
  );


}
