import { TestBed } from '@angular/core/testing';

import { VerifyStatePollingService } from './verify-state-polling.service';

describe('VerifyStatePollingService', () => {
  let service: VerifyStatePollingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VerifyStatePollingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
