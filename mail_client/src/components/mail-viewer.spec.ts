import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MailViewer } from './mail-viewer';

describe('MailViewer', () => {
  let component: MailViewer;
  let fixture: ComponentFixture<MailViewer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MailViewer],
    }).compileComponents();

    fixture = TestBed.createComponent(MailViewer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
