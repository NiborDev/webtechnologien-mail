import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { describe, it, expect, beforeEach } from 'vitest';
import { UserService } from './user-service';

describe('UserService', () => {
  let service: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserService, provideHttpClient()],
    });
    service = TestBed.inject(UserService);
  });

  it('should create an instance', () => {
    expect(service).toBeTruthy();
  });
});
